package com.alarm.coderji.org.alarm_tutorial;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

/**
 * Created by Osama on 12/25/2015.
 */
public class AppController extends Application {

    public static SQLiteDatabase db;
    public static SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();

        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.settings, false);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
    }
}
