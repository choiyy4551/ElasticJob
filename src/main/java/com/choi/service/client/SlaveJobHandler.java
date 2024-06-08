package com.choi.service.client;

import com.choi.config.ThreadPoolConfig;
import com.choi.mapper.JobMapper;
import com.choi.pojo.JobInfo;
import com.choi.pojo.JobResult;
import com.choi.service.Node;
import com.choi.utils.ScheduleTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class SlaveJobHandler implements SlaveService{
    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private Node node;
    @Autowired
    private ThreadPoolConfig threadPoolConfig;
    @Autowired
    private ScheduleTime scheduleTime;
    @Autowired
    private SlaveNode slaveNode;
    private final String nodeId = node.getNodeId();
    private final ThreadPoolTaskExecutor taskExecutor = threadPoolConfig.taskExecutor();

    public void start(BlockingQueue<JobInfo> queue) {
        Consumer consumer = new Consumer(queue);
        taskExecutor.submit(consumer);
    }

    @Override
    public void runJob(JobInfo jobInfo) {

    }

    public void addResult(JobResult jobResult) {
        jobMapper.addResult(jobResult);
        //log
    }

    public void setStartTime(JobResult jobResult) {
        jobResult.setStartTime(scheduleTime.Now());
    }
    public void setFinishTime(JobResult jobResult) {
        jobResult.setFinishTime(scheduleTime.Now());
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
                    JobResult jobResult = new JobResult();
                    jobResult.setUuid(jobInfo.getUuid());
                    setStartTime(jobResult);
                    //log
                    jobResult.setNodeId(nodeId);
                    runJob(jobInfo);
                    setFinishTime(jobResult);
                    addResult(jobResult);
                    jobInfo.setLastRunTime(scheduleTime.Now());
                    //log
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
