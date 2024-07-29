package com.choi.service.server;

import com.choi.Enums.JobStatusEnum;
import com.choi.mapper.*;
import com.choi.pojo.*;
import com.choi.utils.DateUtil;
import com.choi.utils.MyUUID;
import com.choi.utils.ScheduleTime;
import com.choi.utils.StringAndInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class MasterJobHandler {
    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private JobResultMapper jobResultMapper;
    @Qualifier("taskExecutor")
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private ScheduleTime scheduleTime;
    @Autowired
    private MyUUID myUUID;
    private boolean shutDown = true;
    private int runningJobSize = 0;
    private final List<JobTimeInfo> runningJob = new ArrayList<>();
    private final PriorityQueue<JobTimeInfo> waitingJob = new PriorityQueue<>();
    private final List<JobTimeInfo> preparedJob = new ArrayList<>();
    private final Map<String, JobTimeInfo> jobTimes = new HashMap<>();
    private final Map<String, JobResult> jobResultMap = new HashMap<>();

    public void start() {
        shutDown = false;
        //开启线程对任务队列分配
        taskExecutor.submit(this.new changeJob());
        initJob();
    }

    public void stop() {
        //设置shutdown回收线程
        shutDown = true;
    }
    public void initJob() {
        //只有启动时从数据库中重新读取一遍任务
        List<JobInfo> jobInfoList = jobMapper.getAllUseful();
        if (jobInfoList.isEmpty()) {
            System.out.println("数据库中没有剩余任务");
            return;
        }
        List<JobResult> jobResultList = jobResultMapper.getAllJobResult();
        for (JobResult jobResult : jobResultList) {
            jobResultMap.put(jobResult.getUuid(), jobResult);
        }
        for (JobInfo jobinfo : jobInfoList) {
            if (jobTimes.containsKey(jobinfo.getUuid()))
                continue;
            JobTimeInfo jobTimeInfo = createJobTimeInfo(jobinfo);
            jobTimes.put(jobinfo.getUuid(), jobTimeInfo);
            if (jobinfo.getScheduleType().equals("Daily")) {
                Date lastRunTime = DateUtil.DateToCST(jobinfo.getLastRunTime());
                if (lastRunTime != null) {
                    Date date = scheduleTime.GetNextScheduleTime(jobinfo);
                    long timeDiff = lastRunTime.getTime() - date.getTime();
                    if (timeDiff >= 24 * 60 * 60 * 1000) {
                        //Daily任务相差有一天了，可以重做
                        moveToWaiting(jobTimeInfo);
                    } else {
                        continue;
                    }
                }
            }
            if (jobResultMap.containsKey(jobinfo.getUuid())) {
                //jobResultMap中存在任务说明该任务之前就被写入过，判断任务状态分配队列
                switch (jobResultMapper.getJobStatus(jobinfo.getUuid())) {
                    case 3:
                    case 4: {
                        continue;
                    }
                    case 5: {
                        moveToWaiting(jobTimeInfo);
                    }
                    break;
                    case 6: {
                        moveToPrepared(jobTimeInfo);
                    }
                    break;
                    default: {
                        if (runningJobSize > 0 && runningJob.size() < runningJobSize)
                            moveToRunning(jobTimeInfo);
                        else
                            moveToPrepared(jobTimeInfo);
                    }
                }
            } else {
                JobResult jobResult = new JobResult();
                jobResult.setUuid(jobinfo.getUuid());
                jobResult.setName(jobinfo.getName());
                jobResultMapper.addResult(jobResult);
                moveToWaiting(jobTimeInfo);
            }
        }
    }

    //将waiting队列中RunTime到达的任务添加到prepared中
    public void timeCheck() {
        if (!waitingJob.isEmpty()) {
            Date runTime = waitingJob.peek().getRunTime();
            Date nowTime = scheduleTime.Now();
            if (runTime.before(nowTime) || runTime.equals(nowTime)) {
                moveToPrepared(waitingJob.poll());
            }
        }
        //检查一下runningJob队列中的任务是否已经执行完成
        if (runningJob.isEmpty()) {
            return;
        }
        for (int i = 0; i < runningJob.size(); i++) {
            JobTimeInfo jobTimeInfo = runningJob.get(i);
            String uuid = jobTimeInfo.getJobInfo().getUuid();
            switch (jobResultMapper.getJobStatus(uuid)) {
                //查询到之前一次到任务执行失败，尝试重做
                case 4: {
                    JobResult jobResult = jobResultMapper.getJobResultById(uuid);
                    int failureTimes = jobResult.getFailureTimes();
                    failureTimes += 1;
                    if (failureTimes <= 3)
                        //log 重做次数小于等于3则继续重做
                        moveToWaiting(jobTimeInfo);
                    else {
                        //log 重做大于3次，最终落库状态为失败
                        jobResultMap.remove(uuid);
                        jobTimes.remove(uuid);
                        runningJob.remove(i);
                        System.out.println("任务执行完成移出队列");
                        i--;
                    }
                    break;
                }
                case 3: {
                    //任务执行成功
                    jobResultMap.remove(uuid);
                    jobTimes.remove(uuid);
                    runningJob.remove(i);
                    i--;
                    break;
                }
                default:
                    break;
            }
        }
    }
    public void moveToPrepared(JobTimeInfo jobTimeInfo) {
        System.out.println("任务 " + jobTimeInfo.getJobInfo().getName() + " 添加到准备队列" + "执行时间为" + jobTimeInfo.getRunTime());
        preparedJob.add(jobTimeInfo);
        jobTimeInfo.setStatus(JobStatusEnum.Prepared);
        jobResultMapper.setJobStatus(JobStatusEnum.Prepared.getValue(), jobTimeInfo.getJobInfo().getUuid());
    }

    public void moveToRunning(JobTimeInfo jobTimeInfo) {
        System.out.println("任务 " + jobTimeInfo.getJobInfo().getName() + " 添加到运行队列" + "执行时间为" + jobTimeInfo.getRunTime());
        runningJob.add(jobTimeInfo);
        jobTimeInfo.setStatus(JobStatusEnum.Pending);
        jobResultMapper.setJobStatus(JobStatusEnum.Pending.getValue(), jobTimeInfo.getJobInfo().getUuid());
    }

    public void moveToWaiting(JobTimeInfo jobTimeInfo) {
        System.out.println("任务 " + jobTimeInfo.getJobInfo().getName() + " 添加到等待队列" + "执行时间为" + jobTimeInfo.getRunTime());
        waitingJob.add(jobTimeInfo);
        jobTimeInfo.setStatus(JobStatusEnum.Waiting);
        jobResultMapper.setJobStatus(JobStatusEnum.Waiting.getValue(), jobTimeInfo.getJobInfo().getUuid());
    }

    public void addJob(JobInfo jobInfo) {
        String name = jobInfo.getName();
        JobInfo job = jobMapper.getJobByName(name);
        //数据库中存在任务，则更新
        if (job != null) {
            System.out.println("任务已存在！");
            jobInfo.setUuid(job.getUuid());
            jobInfo.setLastRunTime(job.getLastRunTime());
            jobMapper.updateJobInfo(jobInfo);
            //所有队列中都不存在该任务则加入
            if (!jobTimes.containsKey(jobInfo.getUuid())) {
                JobTimeInfo jobTimeInfo = createJobTimeInfo(jobInfo);
                jobTimes.put(jobInfo.getUuid(), jobTimeInfo);
                moveToWaiting(jobTimeInfo);
            }
        } else {
            String jobId = myUUID.createUUID();
            jobInfo.setUuid(jobId);
            JobTimeInfo jobTimeInfo = createJobTimeInfo(jobInfo);
            jobInfo.setRunTime(DateUtil.CSTToDate(jobTimeInfo.getRunTime().toString()));
            System.out.println(jobInfo.getRunTime());
            boolean res = jobMapper.addJobInfo(jobInfo);
            if (!res) {
                System.out.println("任务添加失败，数据库异常！");
            }
            jobTimes.put(jobInfo.getUuid(), jobTimeInfo);
            JobResult jobResult = new JobResult();
            jobResult.setUuid(jobInfo.getUuid());
            jobResult.setName(jobInfo.getName());
            jobResultMapper.addResult(jobResult);
            moveToWaiting(jobTimeInfo);
            System.out.println("任务添加成功！");
        }
    }

    public List<JobInfo> divideJob(String resources, String maxParallel) {
        int count = 0;
        int restResources = StringAndInteger.StringToInteger(resources);
        List<JobInfo> jobInfos = new ArrayList<>();
        while (count < Integer.parseInt(maxParallel)) {
            if (preparedJob.isEmpty()) {
                return jobInfos;
            }
            JobTimeInfo jobTimeInfo = preparedJob.get(0);
            JobInfo jobInfo = jobTimeInfo.getJobInfo();
            int param = StringAndInteger.StringToInteger(jobInfo.getParam());
            if (restResources > param) {
                restResources -= param;
                jobInfos.add(jobInfo);
                moveToRunning(jobTimeInfo);
                preparedJob.remove(jobTimeInfo);
                count++;
                continue;
            }
            break;
        }
        return jobInfos;
    }
    private JobTimeInfo createJobTimeInfo(JobInfo jobInfo) {
        JobTimeInfo jobTimeInfo = new JobTimeInfo();
        jobTimeInfo.setJobInfo(jobInfo);
        if (jobInfo.getRunTime() == null || jobInfo.getRunTime().isEmpty())
            jobTimeInfo.setRunTime(scheduleTime.GetNextScheduleTime(jobInfo));
        else
            jobTimeInfo.setRunTime(DateUtil.DateToCST(jobInfo.getRunTime()));
        return jobTimeInfo;
    }
    public void addRunningJobSize(int maxParallel) {
        System.out.println("子节点增加");
        runningJobSize += maxParallel;
    }
    public void minusRunningJobSize(int maxParallel) {
        System.out.println("子节点减少");
        runningJobSize -= maxParallel;
    }
    class changeJob implements Runnable {
        @Override
        public void run() {
            //记得关闭
            while (!shutDown) {
                timeCheck();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
