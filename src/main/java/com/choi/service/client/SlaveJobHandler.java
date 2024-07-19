package com.choi.service.client;

import com.choi.Enums.JobStatusEnum;
import com.choi.mapper.JobMapper;
import com.choi.mapper.JobResultMapper;
import com.choi.pojo.JobInfo;
import com.choi.pojo.JobResult;
import com.choi.utils.DateUtil;
import com.choi.utils.ScheduleTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class SlaveJobHandler implements SlaveService{
    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private JobResultMapper jobResultMapper;
    @Qualifier("taskExecutor")
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private ScheduleTime scheduleTime;
    private String nodeId;
    private boolean shutDown = true;
    public void start(BlockingQueue<JobInfo> queue, String nodeId) {
        this.nodeId = nodeId;
        shutDown = false;
        Consumer consumer = new Consumer(queue);
        taskExecutor.submit(consumer);
    }
    public void stop() {
        shutDown = true;
    }
    @Override
    public boolean runJob(JobInfo jobInfo) {
        //用户可自主实现runJob业务
        System.out.println("任务执行");
        return true;
    }
    private void setStartTime(JobResult jobResult) {
        jobResult.setStartTime(DateUtil.CSTToDate(scheduleTime.Now().toString()));
        jobResult.setJobStatus(JobStatusEnum.Doing.getValue());
        jobResultMapper.setJobStatus(JobStatusEnum.Doing.getValue(), jobResult.getUuid());
    }
    private void setFinishTime(JobResult jobResult) {
        jobResult.setFinishTime(DateUtil.CSTToDate(scheduleTime.Now().toString()));
    }
    private void deleteJudge(JobInfo jobInfo) {
        switch (jobInfo.getScheduleType()) {
            case "Once" :
            case "Repeat" : {
                jobMapper.stopJob(jobInfo.getName());
                jobResultMapper.stopJob(jobInfo.getName());
            }
            break;
            case "Daily" : {
                //每日任务则继续启用
            }
            break;
            default:
                System.out.println("scheduleParam Error");
        }
    }

    //消费者消费任务
    class Consumer implements Runnable {
        private final BlockingQueue<JobInfo> queue;
        public Consumer(BlockingQueue<JobInfo> queue) {
            this.queue = queue;
        }
        @Override
        public void run() {
            while (!shutDown) {
                try {
                    Thread.sleep(1000);
                    //消费者模型，take()会自动唤醒生产者
                    JobInfo jobInfo = queue.take();
                    JobResult jobResult = jobResultMapper.getJobResultById(jobInfo.getUuid());
                    setStartTime(jobResult);
                    //log
                    jobResult.setNodeId(nodeId);
                    if (runJob(jobInfo))
                        jobResult.setJobStatus(JobStatusEnum.Success.getValue());
                    else
                        jobResult.setJobStatus(JobStatusEnum.Failed.getValue());
                    setFinishTime(jobResult);
                    jobResult.setResult("success");
                    jobInfo.setLastRunTime(scheduleTime.Now().toString());
                    jobMapper.setLastRunTime(DateUtil.CSTToDate(jobInfo.getLastRunTime()), jobInfo.getUuid());
                    jobResultMapper.updateJobResult(jobResult);
                    //run完之后判断任务的类型，看看是否要被删除
                    deleteJudge(jobInfo);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
