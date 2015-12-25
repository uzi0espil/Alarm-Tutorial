package com.alarm.coderji.org.alarm_tutorial;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.alarm.coderji.org.alarm_tutorial.Model.DBHelper;

/**
 * Created by Osama on 12/25/2015.
 */
public class AppController extends Application {

    public static SQLiteDatabase db;
    public static DBHelper dbHelper;
    public static SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
    }
}
