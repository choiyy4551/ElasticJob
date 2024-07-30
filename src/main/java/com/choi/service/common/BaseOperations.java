package com.choi.service.common;

import com.choi.Exception.MyException;
import com.choi.mapper.BaseMapper;
import com.choi.pojo.JobInfo;
import com.choi.pojo.JobResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BaseOperations implements Operations{
    @Autowired
    private BaseMapper baseMapper;
    /**
     * 事务方法保证JobInfo和JobResult一致，注意使用同一数据源
     * @param jobInfo
     * @return
     * @throws MyException
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public boolean addJob(JobInfo jobInfo, JobResult jobResult) throws MyException {
        boolean res1 = baseMapper.addJobInfo(jobInfo);
        boolean res2 = baseMapper.addResult(jobResult);
        System.out.println(jobInfo);
        if (res1 && res2)
            return true;
        throw new MyException("addJob error");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public boolean startJob(String name) throws MyException {
        boolean res1 = baseMapper.startJob(name);
        boolean res2 = baseMapper.startResult(name);
        if (res1 && res2)
            return true;
        throw new MyException("startJob error");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public boolean stopJob(String name) throws MyException {
        boolean res1 = baseMapper.stopJob(name);
        boolean res2 = baseMapper.stopResult(name);
        if (res1 && res2)
            return true;
        throw new MyException("stopJob error");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public boolean deleteJob(String name) throws MyException {
        boolean res1 = baseMapper.deleteJob(name);
        boolean res2 = baseMapper.deleteResult(name);
        if (res1 && res2)
            return true;
        throw new MyException("deleteJob error");
    }

    @Override
    public JobResult getJobResult(String uuid) {
        return baseMapper.getResultById(uuid);
    }
}
