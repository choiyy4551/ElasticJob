package com.choi.pojo;

import com.choi.Enums.CodeEnum;
import com.google.rpc.Code;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private int code;
    private String message;
    private T data;
    public static <T> Result<T> success(T data) {
        return new Result<>(CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getMessage(), data);
    }
    public static <T> Result<T> failure(T data) {
        return new Result<>(CodeEnum.ERR.getCode(), CodeEnum.ERR.getMessage(), data);
    }
}
