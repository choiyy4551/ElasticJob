package com.choi.elastic_job.utils;

import com.google.protobuf.ProtocolStringList;

import java.util.ArrayList;
import java.util.Collections;

public class ConvertUtil {
    public static int toInt(String string) {
        if (string == null || "".equals(string)) {
            return 0;
        }
        try {
            return Integer.parseInt(string);
        } catch (Exception e){
            return 0;
        }
    }
    public static ArrayList<String> protocolStringListToList(ProtocolStringList protocolStringList) {
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, protocolStringList.toArray(new String[0]));
        return list;
    }
}
