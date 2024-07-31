package com.choi.service.client;

import com.choi.Enums.JobStatusEnum;
import com.choi.Exception.MyException;
import com.choi.mapper.DB1Mapper;
import com.choi.mapper.DB2Mapper;
import com.choi.mapper.DB3Mapper;
import com.choi.pojo.JobInfo;
import com.choi.pojo.JobResult;
import com.choi.service.common.BaseOperations;
import com.choi.utils.DBChooseUtil;
import com.choi.utils.DateUtil;
import com.choi.utils.ScheduleTime;
import com.choi.utils.StringIntegerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class SlaveJobHandler implements SlaveService {
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
        jobResult.setStartTime(DateUtil.CSTToDate(scheduleTime.now().toString()));
        jobResult.setJobStatus(JobStatusEnum.Doing.getValue());
        String uuid = jobResult.getUuid();
        int id = StringIntegerUtil.StringToInteger(uuid);
        switch (id % 3) {
            case 0:
                db1Mapper.setStatus(JobStatusEnum.Doing.getValue(), jobResult.getUuid());
                break;
            case 1:
                db2Mapper.setStatus(JobStatusEnum.Doing.getValue(), jobResult.getUuid());
                break;
            case 2:
                db3Mapper.setStatus(JobStatusEnum.Doing.getValue(), jobResult.getUuid());
                break;
            default:
                break;
        }
    }

    private void setFinishTime(JobResult jobResult) {
        jobResult.setFinishTime(DateUtil.CSTToDate(scheduleTime.now().toString()));
    }

    private void deleteJudge(JobInfo jobInfo) {
        switch (jobInfo.getScheduleType()) {
            case "Once" :
            case "Repeat" :
                try {
                    String uuid = jobInfo.getUuid();
                    int id = StringIntegerUtil.StringToInteger(uuid);
                    BaseOperations baseOperations = dbChooseUtil.getDB(id);
                    baseOperations.stopJob(jobInfo.getName());
                } catch (MyException e) {
                    e.printStackTrace();
                }
                break;
            case "Daily" :
                //每日任务则继续启用
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
                    String uuid = jobInfo.getUuid();
                    int id = StringIntegerUtil.StringToInteger(uuid);
                    BaseOperations baseOperations = dbChooseUtil.getDB(id);
                    JobResult jobResult = baseOperations.getJobResult(jobInfo.getUuid());
                    setStartTime(jobResult);
                    //log
                    jobResult.setNodeId(nodeId);
                    if (runJob(jobInfo)) {
                        jobResult.setJobStatus(JobStatusEnum.Success.getValue());
                    } else {
                        jobResult.setJobStatus(JobStatusEnum.Failed.getValue());
                    }
                    setFinishTime(jobResult);
                    jobResult.setResult("success");
                    jobInfo.setLastRunTime(scheduleTime.now().toString());
                    String date = DateUtil.CSTToDate(jobInfo.getLastRunTime());
                    switch (id % 3) {
                        case 0:
                            db1Mapper.setLastRunTime(date, uuid);
                            db1Mapper.updateResult(jobResult);
                            break;
                        case 1:
                            db2Mapper.setLastRunTime(date, uuid);
                            db2Mapper.updateResult(jobResult);
                            break;
                        case 2:
                            db3Mapper.setLastRunTime(date, uuid);
                            db3Mapper.updateResult(jobResult);
                            break;
                        default:
                            break;
                    }
                    //run完之后判断任务的类型，看看是否要被删除
                    deleteJudge(jobInfo);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
