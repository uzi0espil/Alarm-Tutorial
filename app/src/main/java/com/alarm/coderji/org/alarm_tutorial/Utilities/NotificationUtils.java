package com.alarm.coderji.org.alarm_tutorial.Utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.alarm.coderji.org.alarm_tutorial.MainActivity;
import com.alarm.coderji.org.alarm_tutorial.R;

/**
 * Created by Osama on 12/26/2015.
 */
public class NotificationUtils {

    private final static int NOTIFY_ID = 1244;

    public static void createNotification(Context context, String title, String msg, boolean isSound){
        PendingIntent notificationIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setTicker(context.getString(R.string.app_name))
                .setContentText(msg)
                .setContentIntent(notificationIntent)
                .setAutoCancel(true);

        if(isSound)
            mBuilder.setSound(Uri.parse(AlarmUtils.getRingtone()));

        if(AlarmUtils.isVibrate())
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //it is better to make the notification ID based on the ID of the alarm in the database
        notificationManager.notify(NOTIFY_ID, mBuilder.build());
    }

}
