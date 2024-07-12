package com.choi.utils;

import com.choi.pojo.JobInfo;
import org.springframework.stereotype.Component;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class ScheduleTime {
    public Date Now() {
        Calendar nowTime = Calendar.getInstance();
        return nowTime.getTime();//获取当前时间
    }

    public Date GetNextScheduleTime(JobInfo jobInfo)  {
        if (jobInfo.getScheduleType() == null) {
            return Now();
        }

        switch (jobInfo.getScheduleType()) {
            case "Once": {//立即执行
                if (jobInfo.getScheduleParam() == null || jobInfo.getScheduleParam().isEmpty()) {
                    return Now();//默认现在执行
                } else {//xx分后执行
                    String min = jobInfo.getScheduleParam();
                    Calendar nowTime = Calendar.getInstance();
                    nowTime.add(Calendar.MINUTE, Integer.parseInt(min));
                    return nowTime.getTime();
                }
            }
            case "Repeat": {
                if (jobInfo.getScheduleParam() == null || jobInfo.getScheduleParam().isEmpty()) {
                    return null;//
                } else {//每隔xx分后执行
                    String min = jobInfo.getScheduleParam();
                    Calendar last = Calendar.getInstance();
                    if (jobInfo.getLastRunTime() == null) {
                        last.setTime(Now());
                    } else {
                        last.setTime(new Date(jobInfo.getLastRunTime()));
                    }
                    last.add(Calendar.MINUTE, Integer.parseInt(min));
                    return last.getTime();
                }
            }
            case "Daily": {//每天的xx点xx分执行，转化为xx分钟
                if (jobInfo.getScheduleParam() == null || jobInfo.getScheduleParam().isEmpty()) {
                    return null;
                } else {
                    Calendar last = Calendar.getInstance();
                    if(jobInfo.getLastRunTime()==null){
                        String string = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
                        int hour = Integer.parseInt(jobInfo.getScheduleParam().substring(0,2));
                        int min = Integer.parseInt(jobInfo.getScheduleParam().substring(3,5));
                        int sec = Integer.parseInt(jobInfo.getScheduleParam().substring(6,8));
                        int year = Integer.parseInt(string.substring(0,4));
                        int month = Integer.parseInt(string.substring(5,7));
                        int day = Integer.parseInt(string.substring(8,10));
                        last.set(year,month,day,hour,min,sec);
                    }
                    else{
                        last.setTime(new Date(jobInfo.getLastRunTime()));
                    }
                    last.add(Calendar.DATE, 1);
                    return last.getTime();
                }
            }
            default:
                return Now();
        }
    }

}
