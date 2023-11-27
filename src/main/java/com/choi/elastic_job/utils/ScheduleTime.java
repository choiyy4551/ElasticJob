package com.choi.elastic_job.utils;

import com.choi.elastic_job.pojo.JobInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@Component
public class ScheduleTime {
    public Date Now() {
        Calendar nowTime = Calendar.getInstance();
        return nowTime.getTime();//获取当前时间
    }

    public Date GetNextScheduleTime(String scheduleType,String scheduleParam,Date lastScheduleTime)  {
        if (scheduleType == null) {
            return Now();
        }

        switch (scheduleType) {
            case "One": {//立即执行
                if (scheduleParam == null || scheduleParam.isEmpty()) {
                    return Now();//默认现在执行
                } else {//xx分后执行
                    String min = scheduleParam;
                    Calendar nowTime = Calendar.getInstance();
                    nowTime.add(Calendar.MINUTE, Integer.parseInt(min));
                    return nowTime.getTime();
                }
            }
            case "Repeat": {
                if (scheduleParam == null || scheduleParam.isEmpty()) {
                    return null;//
                } else {//每隔xx分后执行
                    String min = scheduleParam;
                    Calendar last = Calendar.getInstance();
                    if (lastScheduleTime == null) {
                        last.setTime(Now());
                    } else {
                        last.setTime(lastScheduleTime);
                    }
                    last.add(Calendar.MINUTE, Integer.parseInt(min));
                    return last.getTime();
                }
            }
            case "Daily": {//每天的xx点xx分执行，转化为xx分钟
                if (scheduleParam == null || scheduleParam.isEmpty()) {
                    return null;
                } else {
                    Calendar last = Calendar.getInstance();
                    if(lastScheduleTime==null){
                        String string = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
                        int hour = Integer.parseInt(scheduleParam.substring(0,2));
                        int min = Integer.parseInt(scheduleParam.substring(3,5));
                        int sec = Integer.parseInt(scheduleParam.substring(6,8));
                        int year = Integer.parseInt(string.substring(0,4));
                        int month = Integer.parseInt(string.substring(5,7));
                        int day = Integer.parseInt(string.substring(8,10));
                        last.set(year,month,day,hour,min,sec);
                    }
                    else{
                        last.setTime(lastScheduleTime);
                    }
                    last.add(Calendar.DATE, 1);
                    return last.getTime();
                }
            }
            default:
                log.info("Unknown schedule type");
                return Now();
        }
    }

}
