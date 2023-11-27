package com.choi.elastic_job.utils;

import org.springframework.stereotype.Component;

@Component
public class JobUtil {
    public String getJobId(String str){
        return str.substring(0,str.indexOf(":"));
    }
    public String getJobParam(String str){
        return str.substring(str.indexOf(":")+1,str.length());
    }
}
