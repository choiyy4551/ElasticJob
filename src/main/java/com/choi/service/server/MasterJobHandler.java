package com.choi.service.server;

import com.choi.config.ThreadPoolConfig;
import com.choi.mapper.JobMapper;
import com.choi.pojo.AtOnceJob;
import com.choi.pojo.JobInfo;
import com.choi.pojo.JobTimeInfo;
import com.choi.pojo.TimeJob;
import com.choi.service.Node;
import com.choi.utils.MyUUID;
import com.choi.utils.ScheduleTime;
import com.choi.utils.StringAndInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class MasterJobHandler{
    @Autowired
    JobMapper jobMapper;
    @Autowired
    ScheduleTime scheduleTime;
    @Autowired
    MasterNode masterNode;
    @Autowired
    MyUUID myUUID;
    @Autowired
    private Node node;
    private final List<JobTimeInfo> runningJob = new ArrayList<>();
    private final PriorityQueue<JobTimeInfo> waitingJob = new PriorityQueue<>();
    private final List<JobTimeInfo> preparedJob = new ArrayList<>();
    private final Map<String, JobTimeInfo> jobTimes = new HashMap<>();

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
        for (JobInfo jobinfo : jobInfoList) {
            JobTimeInfo jobTimeInfo = createJobTimeInfo(jobinfo);
            moveToWaiting(jobTimeInfo);
            jobTimes.put(jobTimeInfo.getUuid(), jobTimeInfo);
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
            runTime = waitingJob.peek().getRunTime();
            nowTime = scheduleTime.Now();
        }
    }
    public void moveToPrepared(JobTimeInfo jobTimeInfo) {
        preparedJob.add(jobTimeInfo);
    }

    public void moveToRunning(JobTimeInfo jobTimeInfo) {
        runningJob.add(jobTimeInfo);
    }

    public void moveToWaiting(JobTimeInfo jobTimeInfo) {waitingJob.add(jobTimeInfo);}

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
            JobTimeInfo jobTimeInfo = waitingJob.peek();
            JobInfo jobInfo = jobTimeInfo.getJobInfo();
            int param = StringAndInteger.StringToInteger(jobInfo.getParam());
            if (restResources > StringAndInteger.StringToInteger(jobInfo.getParam())) {
                restResources -= param;
                jobInfos.add(jobInfo);
                moveToRunning(jobTimeInfo);
                continue;
            }
            break;
        }
        return jobInfos;
    }
    private JobTimeInfo createJobTimeInfo(JobInfo jobInfo) {
        JobTimeInfo jobTimeInfo = new JobTimeInfo();
        jobTimeInfo.setJobInfo(jobInfo);
        jobTimeInfo.setUuid(jobInfo.getUuid());
        jobTimeInfo.setName(jobInfo.getName());
        jobTimeInfo.setScheduleType(jobInfo.getScheduleType());
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
