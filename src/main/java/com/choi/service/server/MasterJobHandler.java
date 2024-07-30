package com.choi.service.server;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.choi.Enums.JobStatusEnum;
import com.choi.Exception.MyException;
import com.choi.mapper.*;
import com.choi.pojo.*;
import com.choi.service.common.BaseOperations;
import com.choi.service.common.Operations;
import com.choi.utils.DBChooseUtil;
import com.choi.utils.DateUtil;
import com.choi.utils.ScheduleTime;
import com.choi.utils.StringIntegerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class MasterJobHandler {
    @Autowired
    private DB1Mapper db1Mapper;
    @Autowired
    private DB2Mapper db2Mapper;
    @Autowired
    private DB3Mapper db3Mapper;
    @Autowired
    private DBChooseUtil dbChooseUtil;
    @Qualifier("taskExecutor")
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private ScheduleTime scheduleTime;
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
        List<JobInfo> jobInfoList = getAllUsefulJob();
        if (jobInfoList.isEmpty()) {
            System.out.println("数据库中没有剩余任务");
            return;
        }
        List<JobResult> jobResultList = getAllUsefulResult();
        for (JobResult jobResult : jobResultList) {
            jobResultMap.put(jobResult.getUuid(), jobResult);
        }
        for (JobInfo jobinfo : jobInfoList) {
            String uuid = jobinfo.getUuid();
            if (jobTimes.containsKey(uuid))
                continue;
            JobTimeInfo jobTimeInfo = createJobTimeInfo(jobinfo);
            jobTimes.put(uuid, jobTimeInfo);
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
            if (jobResultMap.containsKey(uuid)) {
                //jobResultMap中存在任务说明该任务之前就被写入过，判断任务状态分配队列
                switch (jobResultMap.get(uuid).getJobStatus()) {
                    case 3:
                    case 4:
                        continue;
                    case 5:
                        moveToWaiting(jobTimeInfo);
                        break;
                    case 6:
                        moveToPrepared(jobTimeInfo);
                        break;
                    default:
                        if (runningJobSize > 0 && runningJob.size() < runningJobSize)
                            moveToRunning(jobTimeInfo);
                        else
                            moveToPrepared(jobTimeInfo);
                }
            } else {
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
            int id = StringIntegerUtil.StringToInteger(uuid);
            BaseOperations baseOperations = dbChooseUtil.getDB(id);
            JobResult jobResult = baseOperations.getJobResult(uuid);
            switch (jobResult.getJobStatus()) {
                //查询到之前一次到任务执行失败，尝试重做
                case 4: {
                    int failureTimes = jobResult.getFailureTimes();
                    failureTimes += 1;
                    if (failureTimes <= 3) {
                        //log 重做次数小于等于3则继续重做
                        setFailureTimes(jobResult, failureTimes);
                        moveToWaiting(jobTimeInfo);
                    } else {
                        //log 重做大于3次，最终落库状态为失败
                        jobResultMap.remove(uuid);
                        jobTimes.remove(uuid);
                        System.out.println("任务执行完成移出队列");
                        i--;
                    }
                    runningJob.remove(i);
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

    private void moveToPrepared(JobTimeInfo jobTimeInfo) {
        System.out.println("任务 " + jobTimeInfo.getJobInfo().getName() + " 添加到准备队列" + "执行时间为" + jobTimeInfo.getRunTime());
        preparedJob.add(jobTimeInfo);
        jobTimeInfo.setStatus(JobStatusEnum.Prepared);
        setJobStatus(JobStatusEnum.Prepared.getValue(), jobTimeInfo.getJobInfo().getUuid());
    }

    private void moveToRunning(JobTimeInfo jobTimeInfo) {
        System.out.println("任务 " + jobTimeInfo.getJobInfo().getName() + " 添加到运行队列" + "执行时间为" + jobTimeInfo.getRunTime());
        runningJob.add(jobTimeInfo);
        jobTimeInfo.setStatus(JobStatusEnum.Pending);
        setJobStatus(JobStatusEnum.Pending.getValue(), jobTimeInfo.getJobInfo().getUuid());
    }

    private void moveToWaiting(JobTimeInfo jobTimeInfo) {
        System.out.println("任务 " + jobTimeInfo.getJobInfo().getName() + " 添加到等待队列" + "执行时间为" + jobTimeInfo.getRunTime());
        waitingJob.add(jobTimeInfo);
        jobTimeInfo.setStatus(JobStatusEnum.Waiting);
        setJobStatus(JobStatusEnum.Waiting.getValue(), jobTimeInfo.getJobInfo().getUuid());
    }

    private void setJobStatus(int status, String uuid) {
        int id = StringIntegerUtil.StringToInteger(uuid);
        switch (id % 3) {
            case 0:
                db1Mapper.setStatus(status, uuid);
                break;
            case 1:
                db2Mapper.setStatus(status, uuid);
                break;
            case 2:
                db3Mapper.setStatus(status, uuid);
                break;
        }
    }

    public boolean addJob(JobInfo jobInfo) throws MyException {
        String uuid = jobInfo.getUuid();
        int id = StringIntegerUtil.StringToInteger(uuid);
        JobResult jobResult = new JobResult();
        jobResult.setUuid(uuid);
        jobResult.setName(jobInfo.getName());
        JobTimeInfo jobTimeInfo = createJobTimeInfo(jobInfo);
        jobTimes.put(uuid, jobTimeInfo);
        jobInfo.setRunTime(DateUtil.CSTToDate(jobTimeInfo.getRunTime().toString()));
        moveToWaiting(jobTimeInfo);
        BaseOperations baseOperations = dbChooseUtil.getDB(id);
        return baseOperations.addJob(jobInfo, jobResult);
    }

    public boolean updateJobInfo(JobInfo jobInfo) {
        String uuid = jobInfo.getUuid();
        int id = StringIntegerUtil.StringToInteger(uuid);
        //任务还未被加入任何队列，添加至waiting
        if (!jobTimes.containsKey(uuid)) {
            JobTimeInfo jobTimeInfo = createJobTimeInfo(jobInfo);
            jobTimes.put(uuid, jobTimeInfo);
            moveToWaiting(jobTimeInfo);
        }
        switch (id % 3) {
            case 0:
                return db1Mapper.updateJobInfo(jobInfo);
            case 1:
                return db2Mapper.updateJobInfo(jobInfo);
            case 2:
                return db3Mapper.updateJobInfo(jobInfo);
            default:
                return false;
        }
    }

    private List<JobInfo> getAllUsefulJob() {
        List<JobInfo> db1Jobs = db1Mapper.getAllUsefulJob();
        List<JobInfo> db2Jobs = db2Mapper.getAllUsefulJob();
        List<JobInfo> db3Jobs = db3Mapper.getAllUsefulJob();
        List<JobInfo> results = new ArrayList<>();
        results.addAll(db1Jobs);
        results.addAll(db2Jobs);
        results.addAll(db3Jobs);
        return results;
    }

    private List<JobResult> getAllUsefulResult() {
        List<JobResult> db1result = db1Mapper.getAllUsefulResult();
        List<JobResult> db2result = db2Mapper.getAllUsefulResult();
        List<JobResult> db3result = db3Mapper.getAllUsefulResult();
        List<JobResult> results = new ArrayList<>();
        results.addAll(db1result);
        results.addAll(db2result);
        results.addAll(db3result);
        return results;
    }

    private void setFailureTimes(JobResult jobResult, int failureTimes) {
        String uuid = jobResult.getUuid();
        int id = StringIntegerUtil.StringToInteger(uuid);
        switch (id % 3) {
            case 0:
                db1Mapper.setFailureTimes(failureTimes, uuid);
                break;
            case 1:
                db2Mapper.setFailureTimes(failureTimes, uuid);
                break;
            case 2:
                db3Mapper.setFailureTimes(failureTimes, uuid);
                break;
        }
    }

    public int getJobStatus(String uuid) {
        int id = StringIntegerUtil.StringToInteger(uuid);
        BaseOperations baseOperations = dbChooseUtil.getDB(id);
        JobResult jobResult = baseOperations.getJobResult(uuid);
        return jobResult == null ? -1 : jobResult.getJobStatus();
    }

    public List<JobInfo> divideJob(String resources, String maxParallel) {
        int count = 0;
        int restResources = StringIntegerUtil.StringToInteger(resources);
        List<JobInfo> jobInfos = new ArrayList<>();
        while (count < Integer.parseInt(maxParallel)) {
            if (preparedJob.isEmpty()) {
                return jobInfos;
            }
            JobTimeInfo jobTimeInfo = preparedJob.get(0);
            JobInfo jobInfo = jobTimeInfo.getJobInfo();
            int param = StringIntegerUtil.StringToInteger(jobInfo.getParam());
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
