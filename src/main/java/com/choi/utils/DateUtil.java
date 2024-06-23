package com.choi.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
    public static String CSTToDate(String string) {
        DateFormat cst = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat gmt = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Date dateTime = null;
        try {
            dateTime = gmt.parse(string);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return cst.format(dateTime);
    }
    public static Date DateToCST(String string) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = inputFormat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        outputFormat.setTimeZone(TimeZone.getTimeZone("CDT"));
        return date;
    }
}
