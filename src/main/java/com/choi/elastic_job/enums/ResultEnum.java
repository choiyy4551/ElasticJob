package com.choi.elastic_job.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultEnum {


    SUCCESS(0,"success"),
    //与锁相关的
    getLock_Error(1,"get Lock failed"),
    unLock_Error(2,"release lock failed"),
    unLock2_Error(3,"dont have lock"),
    //
    getJob_Error(4,"get Job error"),
    addJob_Error(5,"add Job error"),

    delJob_Error(6,"delete job error"),
    editJob_Error(7,"edit a job error"),
    getResult_Error(8,"get results error"),
    startJob_Error(9,"start job error"),
    stoptJob_Error(9,"stop job error");

    private Integer code;
    private String message;

}
