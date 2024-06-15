package com.choi.Enums;

import lombok.Getter;

@Getter
public enum CodeEnum {
    ADD_JOB_SUCCESS(1001, "添加任务成功"),
    ADD_JOB_ERR(1002, "添加任务失败"),
    SUCCESS(1,"成功"),
    ERR(2, "失败"),
    DELETE_JOB_SUCCESS(1003, "删除成功"),
    DELETE_JOB_ERR(1004, "删除失败"),
    GET_RESULT_SUCCESS(1005, "查询结果成功"),
    GET_RESULT_ERR(1006, "查询结果失败");
    private final int code;
    private final String message;

    CodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
