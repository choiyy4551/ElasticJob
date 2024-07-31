package com.choi.utils;

import com.choi.service.common.BaseOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DBChooseUtil {
    @Autowired
    private BaseOperations DB1OperationsImpl;
    @Autowired
    private BaseOperations DB2OperationsImpl;
    @Autowired
    private BaseOperations DB3OperationsImpl;
    public BaseOperations getDB(int id) {
        switch (id % 3) {
            case 0:
                return DB1OperationsImpl;
            case 1:
                return DB2OperationsImpl;
            case 2:
                return DB3OperationsImpl;
            default:
                return null;
        }
    }
}
