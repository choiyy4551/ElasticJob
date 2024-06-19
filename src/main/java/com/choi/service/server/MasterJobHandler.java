package com.choi.service.server;

import com.choi.Enums.JobStatusEnum;
import com.choi.Enums.ScheduleType;
import com.choi.config.ThreadPoolConfig;
import com.choi.mapper.JobMapper;
import com.choi.mapper.JobResultMapper;
import com.choi.pojo.*;
import com.choi.service.Node;
import com.choi.utils.MyUUID;
import com.choi.utils.ScheduleTime;
import com.choi.utils.StringAndInteger;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
public class MasterJobHandler{
    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private JobResultMapper jobResultMapper;
    @Autowired
    private ScheduleTime scheduleTime;
    @Autowired
    private MyUUID myUUID;
    @Autowired
    private Node node;
    private final List<JobTimeInfo> runningJob = new ArrayList<>();
    private final PriorityQueue<JobTimeInfo> waitingJob = new PriorityQueue<>();
    private final List<JobTimeInfo> preparedJob = new ArrayList<>();
    private final Map<String, JobTimeInfo> jobTimes = new HashMap<>();

    private final Map<String, JobResult> jobResultMap = new HashMap<>();

    public void start() {
        setJobTime();
    }

    public void stop() {
        //设置shutdown回收线程
       node.setShutDown(true);
    }
    public void setJobTime() {
        //只有启动时从数据库中重新读取一遍任务
        List<JobInfo> jobInfoList = jobMapper.getAllJob();
        List<JobResult> jobResultList = jobResultMapper.getAllJobResult();
        for (JobResult jobResult : jobResultList) {
            jobResultMap.put(jobResult.getUuid(), jobResult);
        }
        for (JobInfo jobinfo : jobInfoList) {
            JobTimeInfo jobTimeInfo = createJobTimeInfo(jobinfo);
            jobTimes.put(jobTimeInfo.getJobInfo().getUuid(), jobTimeInfo);
            if (jobinfo.getScheduleType().equals("Daily")) {
                Date lastRunTime = jobinfo.getLastRunTime();
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
                    default:
                        moveToRunning(jobTimeInfo);
                }
            }
            JobResult jobResult = new JobResult();
            jobResult.setUuid(jobinfo.getUuid());
            jobResult.setName(jobinfo.getName());
            jobResultMapper.addResult(jobResult);
            moveToWaiting(jobTimeInfo);
        }
    }

    //将waiting队列中RunTime到达的任务添加到prepared中
    public void timeCheck() {
        if (waitingJob.isEmpty())
            return;
        Date runTime = waitingJob.peek().getRunTime();
        Date nowTime = scheduleTime.Now();
        while (runTime.after(nowTime) || runTime.equals(nowTime)) {
            moveToPrepared(waitingJob.poll());
            if (waitingJob.isEmpty()) {
                //log 没有任务了
                return ;
            }
            runTime = waitingJob.peek().getRunTime();
            nowTime = scheduleTime.Now();
        }
        //检查一下runningJob队列中的任务是否已经执行完成
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
                        i--;
                    }
                }
                break;
                case 3: {
                    //任务执行成功
                    jobResultMap.remove(uuid);
                    jobTimes.remove(uuid);
                    runningJob.remove(i);
                    i--;
                }
                break;
                default:
                    break;
            }
        }
    }
    public void moveToPrepared(JobTimeInfo jobTimeInfo) {
        preparedJob.add(jobTimeInfo);
        jobTimeInfo.setStatus(JobStatusEnum.Prepared);
        jobResultMapper.setJobStatus(JobStatusEnum.Prepared.getValue(), jobTimeInfo.getJobInfo().getUuid());
    }

    public void moveToRunning(JobTimeInfo jobTimeInfo) {
        runningJob.add(jobTimeInfo);
        jobTimeInfo.setStatus(JobStatusEnum.Pending);
        jobResultMapper.setJobStatus(JobStatusEnum.Pending.getValue(), jobTimeInfo.getJobInfo().getUuid());
    }

    public void moveToWaiting(JobTimeInfo jobTimeInfo) {
        waitingJob.add(jobTimeInfo);
        jobTimeInfo.setStatus(JobStatusEnum.Waiting);
        jobResultMapper.setJobStatus(JobStatusEnum.Waiting.getValue(), jobTimeInfo.getJobInfo().getUuid());
    }

    public void addJob(JobInfo jobInfo) {
        String name = jobInfo.getName();
        JobInfo job = jobMapper.getJobByName(name);
        //数据库中存在任务，则更新
        if (job != null) {
            jobInfo.setUuid(job.getUuid());
            jobInfo.setLastRunTime(job.getLastRunTime());
            boolean res = jobMapper.updateJobInfo(jobInfo);
            if (!res) {
                //log
            }
            //所有队列中都不存在该任务则加入
            if (!jobTimes.containsKey(jobInfo.getUuid())) {
                JobTimeInfo jobTimeInfo = createJobTimeInfo(jobInfo);
                jobTimes.put(jobInfo.getUuid(), jobTimeInfo);
                moveToWaiting(jobTimeInfo);
            }
        } else {
            String jobId = myUUID.createUUID();
            jobInfo.setUuid(jobId);
            boolean res = jobMapper.addJobInfo(jobInfo);
            if (!res) {
                //log
            }
            JobTimeInfo jobTimeInfo = createJobTimeInfo(jobInfo);
            jobTimes.put(jobInfo.getUuid(), jobTimeInfo);
            moveToWaiting(jobTimeInfo);
        }
    }

    public List<JobInfo> divideJob(String resources, String maxParallel) {
        int count = 0;
        int restResources = StringAndInteger.StringToInteger(resources);
        List<JobInfo> jobInfos = new ArrayList<>();
        while (count < Integer.parseInt(maxParallel)) {
            if (preparedJob.isEmpty())
                return jobInfos;
            JobTimeInfo jobTimeInfo = preparedJob.get(0);
            JobInfo jobInfo = jobTimeInfo.getJobInfo();
            int param = StringAndInteger.StringToInteger(jobInfo.getParam());
            if (restResources > StringAndInteger.StringToInteger(jobInfo.getParam())) {
                restResources -= param;
                jobInfos.add(jobInfo);
                moveToRunning(jobTimeInfo);
                preparedJob.remove(jobTimeInfo);
                continue;
            }
            break;
        }
        return jobInfos;
    }
    private JobTimeInfo createJobTimeInfo(JobInfo jobInfo) {
        JobTimeInfo jobTimeInfo = new JobTimeInfo();
        jobTimeInfo.setJobInfo(jobInfo);
        jobTimeInfo.setRunTime(scheduleTime.GetNextScheduleTime(jobInfo));
        return jobTimeInfo;
    }
    class changeJob implements Runnable {
        @Override
        public void run() {
            //记得关闭
            while (!node.isShutDown()) {
                timeCheck();
            }
        }
    }
}
