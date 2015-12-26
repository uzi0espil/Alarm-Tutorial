package com.alarm.coderji.org.alarm_tutorial.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alarm.coderji.org.alarm_tutorial.Services.AlarmService;

/**
 * Created by Osama on 12/26/2015.
 */
public class AlarmBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, AlarmService.class);
        service.setAction(AlarmService.CREATE);
        context.startService(service);
    }
}
