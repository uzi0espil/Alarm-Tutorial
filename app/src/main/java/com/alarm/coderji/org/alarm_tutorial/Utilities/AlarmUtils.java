package com.alarm.coderji.org.alarm_tutorial.Utilities;

import android.media.RingtoneManager;
import android.provider.Settings;
import android.util.Log;

import com.alarm.coderji.org.alarm_tutorial.AppController;

/**
 * Created by Osama on 12/25/2015.
 */
public class AlarmUtils {

    public static final String TIME_OPTION = "time_option";
    public static final String DATE_RANGE = "date_range";
    public static final String DATE_FORMAT = "date_format";
    public static final String TIME_FORMAT = "time_format";
    public static final String VIBRATE_PREF = "vibrate_pref";
    public static final String RINGTONE_PREF = "ringtone_pref";

    public static final String DEFAULT_DATE_FORMAT = "yyyy-M-d";

    public static boolean showRemainingTime(){
        return "1".equals(AppController.sp.getString(TIME_OPTION, "0"));
    }

    public static int getDateRange(){
        return Integer.parseInt(AppController.sp.getString(DATE_RANGE, "0"));
    }

    public static String getDateFormat(){
        return AppController.sp.getString(DATE_FORMAT, DEFAULT_DATE_FORMAT);
    }

    public static boolean is24Hours(){
        return AppController.sp.getBoolean(TIME_FORMAT, true);
    }

    public static boolean isVibrate(){
        return AppController.sp.getBoolean(VIBRATE_PREF, true);
    }

    public static String getRingtone(){
        return AppController.sp.getString(RINGTONE_PREF, Settings.System.DEFAULT_ALARM_ALERT_URI.toString());
    }

}
