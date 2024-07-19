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
        return nowTime.getTime();
    }

    public Date GetNextScheduleTime(JobInfo jobInfo)  {
        if (jobInfo.getScheduleType() == null) {
            return Now();
        }

        switch (jobInfo.getScheduleType()) {
            case "Once": {
                if (jobInfo.getScheduleParam() == null || jobInfo.getScheduleParam().isEmpty()) {
                    return Now();
                } else {
                    String min = jobInfo.getScheduleParam();
                    Calendar nowTime = Calendar.getInstance();
                    nowTime.add(Calendar.MINUTE, Integer.parseInt(min));
                    return nowTime.getTime();
                }
            }
            case "Repeat": {
                if (jobInfo.getScheduleParam() == null || jobInfo.getScheduleParam().isEmpty()) {
                    return null;//
                } else {
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
            case "Daily": {
                if (jobInfo.getScheduleParam() == null || jobInfo.getScheduleParam().isEmpty()) {
                    return null;
                } else {
                    String day;
                    if(jobInfo.getLastRunTime() == null){
                        day = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
                        day += " " + jobInfo.getScheduleParam();
                    }
                    else {
                        day = jobInfo.getLastRunTime();
                    }
                    Date date = DateUtil.DateToCST(day);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    if (date.before(Now()))
                        calendar.add(Calendar.DATE, 1);
                    return calendar.getTime();
                }
            }
            default:
                return Now();
        }
    }

}
