package com.alarm.coderji.org.alarm_tutorial.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.alarm.coderji.org.alarm_tutorial.AppController;
import com.alarm.coderji.org.alarm_tutorial.Model.Alarm;
import com.alarm.coderji.org.alarm_tutorial.Model.AlarmMsg;
import com.alarm.coderji.org.alarm_tutorial.R;
import com.alarm.coderji.org.alarm_tutorial.Utilities.NotificationUtils;

/**
 * Created by Osama on 12/26/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long alarmMsgId = intent.getLongExtra(AlarmMsg.COL_ID, -1);
        long alarmId = intent.getLongExtra(AlarmMsg.COL_ALARMID, -1);

        AlarmMsg alarmMsg = new AlarmMsg(alarmMsgId);
        alarmMsg.setStatus(AlarmMsg.EXPIRED);
        alarmMsg.persist(AppController.db);

        Alarm alarm = new Alarm(alarmId);
        alarm.load(AppController.db);

        NotificationUtils.createNotification(context, context.getString(R.string.app_name), alarm.getName(), alarm.getSound());
    }
}
