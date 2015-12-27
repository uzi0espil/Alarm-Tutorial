package com.alarm.coderji.org.alarm_tutorial.Services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.util.Log;

import com.alarm.coderji.org.alarm_tutorial.AppController;
import com.alarm.coderji.org.alarm_tutorial.Model.Alarm;
import com.alarm.coderji.org.alarm_tutorial.Model.AlarmMsg;
import com.alarm.coderji.org.alarm_tutorial.Receiver.AlarmReceiver;
import com.alarm.coderji.org.alarm_tutorial.Utilities.Util;

/**
 * Created by Osama on 12/26/2015.
 */
public class AlarmService extends IntentService {

    private final static String TAG = Alarm.class.getSimpleName();

    public static final String POPULATE = "POPULATE";
    public static final String CREATE = "CREATE";
    public static final String CANCEL = "CANCEL";

    private IntentFilter matcher;

    public AlarmService() {
        super(TAG);
        matcher = new IntentFilter();
        matcher.addAction(POPULATE);
        matcher.addAction(CREATE);
        matcher.addAction(CANCEL);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("SERVICE","Service has been started");
        String action = intent.getAction();
        String alarmId = intent.getStringExtra(AlarmMsg.COL_ALARMID);
        String alarmMsgId = intent.getStringExtra(AlarmMsg.COL_ID);
        String startTime = intent.getStringExtra(Alarm.COL_FROMDATE);
        String endTime = intent.getStringExtra(Alarm.COL_TODATE);

        if(matcher.matchAction(action)){
            Log.d("MATCHER","Service has been started");
            if(POPULATE.equals(action)){
                AppController.dbHelper.populate(Long.parseLong(alarmId), AppController.db);
                execute(CREATE, alarmId);
            }

            if (CREATE.equals(action)) {
                Log.d("CREATE","Service has been started");
                execute(CREATE, alarmId, alarmMsgId, startTime, endTime);
            }

            if (CANCEL.equals(action)) {
                execute(CANCEL, alarmId, alarmMsgId, startTime, endTime);
                AppController.dbHelper.shred(AppController.db);
            }
        }
    }

    private void execute(String action, String... args) {
        Intent i;
        PendingIntent pi;
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Cursor c;

        String alarmId = (args != null && args.length > 0) ? args[0] : null;
        String alarmMsgId = (args != null && args.length > 1) ? args[1] : null;
        String startTime = (args != null && args.length > 2) ? args[2] : null;
        String endTime = (args != null && args.length > 3) ? args[3] : null;

        String status = CANCEL.equals(action) ? AlarmMsg.CANCELLED : AlarmMsg.ACTIVE;

        if (alarmMsgId != null)
            c = AppController.db.query(AlarmMsg.TABLE_NAME, null, AlarmMsg.COL_ID+" = ?", new String[]{alarmMsgId}, null, null, null); // should be moved to class AlarmMsg;
        else
            c = AlarmMsg.list(AppController.db, alarmId, startTime, endTime, status);


        Log.d("Service cound C","" + c.getCount());
        if(c.moveToFirst()){
            Log.d("within C","lets see");
            long now = System.currentTimeMillis();
            long time, diff;

            do {
                i = new Intent(this, AlarmReceiver.class);
                i.putExtra(AlarmMsg.COL_ID, c.getLong(c.getColumnIndex(AlarmMsg.COL_ID)));
                i.putExtra(AlarmMsg.COL_ALARMID, c.getLong(c.getColumnIndex(AlarmMsg.COL_ALARMID)));

                pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

                time = c.getLong(c.getColumnIndex(AlarmMsg.COL_DATETIME));
                diff = time - now + (long) Util.MIN;
                Log.d("diff", "" + diff);
                if(CREATE.equals(action)){
                    if (diff > 0 && diff < Util.YEAR) {
                        Log.d("I am here", "Here I am");
                        am.set(AlarmManager.RTC_WAKEUP, time, pi);
                    }
                } else if(CANCEL.equals(action)){
                    am.cancel(pi);
                }
            } while(c.moveToNext());
        }
        c.close();
    }
}
