package com.choi.service.client;

import com.choi.Enums.JobStatusEnum;
import com.choi.config.ThreadPoolConfig;
import com.choi.mapper.JobMapper;
import com.choi.mapper.JobResultMapper;
import com.choi.pojo.JobInfo;
import com.choi.pojo.JobResult;
import com.choi.service.Node;
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
    @Autowired
    private Node node;
    @Qualifier("taskExecutor")
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private ScheduleTime scheduleTime;
    @Autowired
    private SlaveNode slaveNode;
    private final String nodeId = node.getNodeId();

    public void start(BlockingQueue<JobInfo> queue) {
        Consumer consumer = new Consumer(queue);
        taskExecutor.submit(consumer);
    }

    @Override
    public boolean runJob(JobInfo jobInfo) {
        //
        return true;
    }
    private void setStartTime(JobResult jobResult) {
        jobResult.setStartTime(scheduleTime.Now());
        jobResult.setJobStatus(JobStatusEnum.Doing.getValue());
        jobResultMapper.setJobStatus(JobStatusEnum.Doing.getValue(), jobResult.getUuid());
    }
    private void setFinishTime(JobResult jobResult) {
        jobResult.setFinishTime(scheduleTime.Now());
    }
    private void deleteJudge(JobInfo jobInfo) {
        switch (jobInfo.getScheduleType()) {
            case "One" :
            case "Repeat" : {
                jobMapper.stopJob(jobInfo.getUuid());
            }
            break;
            case "Daily" : {
                //每日任务则继续启用
            }
            break;
            default:
                //log
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
            try {
                //消费者模型，take()会自动唤醒生产者
                while (!slaveNode.isSlaveShutdown()) {
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
                    jobInfo.setLastRunTime(scheduleTime.Now());
                    //log

                    //run完之后判断任务的类型，看看是否要被删除
                    deleteJudge(jobInfo);
                    jobResultMapper.updateJobResult(jobResult);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
