package com.alarm.coderji.org.alarm_tutorial.Utilities;

import java.util.List;

/**
 * Created by Osama on 12/25/2015.
 */
public class Util {

    public static final StringBuilder sb = new StringBuilder();

    public final static double MIN = 60 * 1000.0;
    public final static double HOUR = 60 * MIN;
    public final static double DAY = 24 * HOUR;
    public final static double MONTH = 30 * DAY;
    public final static double YEAR = 365 * DAY;

    public static String concat(Object... objects){
        sb.setLength(0);
        for (Object obj : objects){
            sb.append(obj);
        }
        return sb.toString();
    }

    public static String listToCsv(List list){
        if(list == null || list.isEmpty())
            return "";

        sb.setLength(0);
        for(Object obj : list){
            sb.append(",").append(obj);
        }
        return sb.toString();
    }
}
