package com.choi.elastic_job.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorCode {
    String code;
    String message;

    public static ErrorCode change(int num){
        ErrorCode code;
        if(num == 1){//1表示成功，2表示失败
            code = new ErrorCode("suc","success");
        }
        else{
            code = new ErrorCode("err","error");
        }
        return code;
    }
}
