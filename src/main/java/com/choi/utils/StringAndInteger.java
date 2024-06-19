package com.choi.utils;

import org.springframework.stereotype.Component;


public class StringAndInteger {
    public static Integer StringToInteger(String s) {
        return Integer.parseInt(s);
    }
    public static String IntegerToString(Integer i) {
        return String.valueOf(i);
    }
}
