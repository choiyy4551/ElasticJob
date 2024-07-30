package com.choi.utils;

import com.choi.service.common.BaseOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DBChooseUtil {
    @Autowired
    private BaseOperations db1OperationsImpl;
    @Autowired
    private BaseOperations db2OperationsImpl;
    @Autowired
    private BaseOperations db3OperationsImpl;
    public BaseOperations getDB(int id) {
        switch (id % 3) {
            case 0:
                return db1OperationsImpl;
            case 1:
                return db2OperationsImpl;
            case 2:
                return db3OperationsImpl;
            default:
                return null;
        }
    }
}
