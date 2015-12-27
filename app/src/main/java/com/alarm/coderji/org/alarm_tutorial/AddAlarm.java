package com.alarm.coderji.org.alarm_tutorial;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.alarm.coderji.org.alarm_tutorial.Model.Alarm;
import com.alarm.coderji.org.alarm_tutorial.Model.AlarmMsg;
import com.alarm.coderji.org.alarm_tutorial.Model.AlarmTime;
import com.alarm.coderji.org.alarm_tutorial.Model.DBHelper;
import com.alarm.coderji.org.alarm_tutorial.Services.AlarmService;
import com.alarm.coderji.org.alarm_tutorial.Utilities.AlarmUtils;
import com.alarm.coderji.org.alarm_tutorial.Utilities.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddAlarm extends AppCompatActivity {

    private EditText msgEdit;
    private CheckBox soundCb;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private TextView fromdateText;
    private TextView todateText;
    private TextView attimeText;

    private ViewSwitcher vs;
    private RadioGroup rg;
    private RelativeLayout rl3, rl4;
    private Spinner s1, s2, s3;
    private EditText minsEdit, hoursEdit, daysEdit, monthsEdit, yearsEdit;

    private SQLiteDatabase db;

    private static final int DIALOG_FROMDATE = 1;
    private static final int DIALOG_TODATE = 2;
    private static final int DIALOG_ATTIME = 3;

    private final static SimpleDateFormat sdf = new SimpleDateFormat();

    private AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(s1.getSelectedItemPosition() > 0 && s2.getSelectedItemPosition() > 0)
                s1.setSelection(0);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViews();
        db = AppController.db;

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio0:
                        rl3.setVisibility(View.VISIBLE);
                        rl4.setVisibility(View.GONE);
                        break;
                    case R.id.radio1:
                        rl3.setVisibility(View.GONE);
                        rl4.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        s1.setOnItemSelectedListener(spinnerListener);
        s2.setOnItemSelectedListener(spinnerListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("vs", vs.getDisplayedChild());
        outState.putInt("hour", timePicker.getHour());
        outState.putCharSequence("fromdate", fromdateText.getText());
        outState.putCharSequence("todate", todateText.getText());
        outState.putCharSequence("attime", attimeText.getText());
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        vs.setDisplayedChild(state.getInt("vs"));
        timePicker.setHour(state.getInt("hour"));
        fromdateText.setText(state.getCharSequence("fromdate"));
        todateText.setText(state.getCharSequence("todate"));
        attimeText.setText(state.getCharSequence("attime"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        sdf.applyPattern(AlarmUtils.getDateFormat());
    }

    private void findViews() {
        msgEdit = (EditText) findViewById(R.id.msg_et);
        soundCb = (CheckBox) findViewById(R.id.sound_cb);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        fromdateText = (TextView) findViewById(R.id.fromdate_tv);
        todateText = (TextView) findViewById(R.id.todate_tv);
        attimeText = (TextView) findViewById(R.id.attime_tv);
        vs = (ViewSwitcher) findViewById(R.id.view_switcher);
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        rl3 = (RelativeLayout) findViewById(R.id.relativeLayout3);
        rl4 = (RelativeLayout) findViewById(R.id.relativeLayout4);
        s1 = (Spinner) findViewById(R.id.spinner1);
        s2 = (Spinner) findViewById(R.id.spinner2);
        s3 = (Spinner) findViewById(R.id.spinner3);

        minsEdit = (EditText) findViewById(R.id.mins_et);
        hoursEdit = (EditText) findViewById(R.id.hours_et);
        daysEdit = (EditText) findViewById(R.id.days_et);
        monthsEdit = (EditText) findViewById(R.id.months_et);
        yearsEdit = (EditText) findViewById(R.id.years_et);
    }

    private boolean validate() {
        if (TextUtils.isEmpty(msgEdit.getText())) {
            msgEdit.requestFocus();
            Toast.makeText(this, "Enter a message", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (vs.getDisplayedChild() == 1) {
            if (TextUtils.isEmpty(fromdateText.getText())) {
                Toast.makeText(this, "Specify from date", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(todateText.getText())) {
                Toast.makeText(this, "Specify to date", Toast.LENGTH_SHORT).show();
                return false;
            }
            try {
                if (sdf.parse(fromdateText.getText().toString()).after(sdf.parse(todateText.getText().toString()))) {
                    Toast.makeText(this, "From date is after To date!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (ParseException e) {}
            if (TextUtils.isEmpty(attimeText.getText())) {
                Toast.makeText(this, "Specify time", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toggleButton1:
                vs.showNext();
                break;

            case R.id.fromdate_tv:
            case R.id.fromdate_lb:
                showDialog(DIALOG_FROMDATE);
                break;

            case R.id.todate_tv:
            case R.id.todate_lb:
                showDialog(DIALOG_TODATE);
                break;

            case R.id.attime_tv:
            case R.id.attime_lb:
                showDialog(DIALOG_ATTIME);
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(final int id) {
        Calendar cal = Calendar.getInstance();
        switch(id) {
            case DIALOG_ATTIME:
                TimePickerDialog.OnTimeSetListener mTimeSetListener =
                        new TimePickerDialog.OnTimeSetListener() {
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                attimeText.setText(Util.getActualTime(hourOfDay, minute));
                            }
                        };
                return new TimePickerDialog(this, mTimeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), AlarmUtils.is24Hours());

            case DIALOG_FROMDATE:
            case DIALOG_TODATE:
                DatePickerDialog.OnDateSetListener dateListener =
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String txt = DBHelper.getDateStr(year, monthOfYear+1, dayOfMonth);
                                try {
                                    txt = sdf.format(DBHelper.sdf.parse(txt));
                                } catch (ParseException e) {}

                                if (id == DIALOG_FROMDATE) {
                                    fromdateText.setText(txt);
                                } else if (id == DIALOG_TODATE) {
                                    todateText.setText(txt);
                                }
                            }
                        };
                return new DatePickerDialog(this, dateListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        }

        return super.onCreateDialog(id);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);

        switch(id) {
            case DIALOG_ATTIME:
                if (!TextUtils.isEmpty(attimeText.getText())) {
                    String[] arr = Util.toPersistentTime(attimeText.getText().toString()).split(":");
                    ((TimePickerDialog)dialog).updateTime(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
                }
                break;

            case DIALOG_FROMDATE:
                if (!TextUtils.isEmpty(fromdateText.getText())) {
                    String[] arr = Util.toPersistentDate(fromdateText.getText().toString(), sdf).split("-");
                    ((DatePickerDialog)dialog).updateDate(Integer.parseInt(arr[0]), Integer.parseInt(arr[1])-1, Integer.parseInt(arr[2]));
                }
                break;

            case DIALOG_TODATE:
                if (!TextUtils.isEmpty(todateText.getText())) {
                    String[] arr = Util.toPersistentDate(todateText.getText().toString(), sdf).split("-");
                    ((DatePickerDialog)dialog).updateDate(Integer.parseInt(arr[0]), Integer.parseInt(arr[1])-1, Integer.parseInt(arr[2]));
                }
                break;
        }
    }

    public void create(View v){
        if(!validate()) return;

        Alarm alarm = new Alarm();
        alarm.setName(msgEdit.getText().toString());
        alarm.setSound(soundCb.isChecked());
        AlarmTime alarmTime = new AlarmTime();
        long alarmId = 0;

        switch(vs.getDisplayedChild()){
            case 0:
                alarm.setFromDate(DBHelper.getDateStr(datePicker.getYear(), datePicker.getMonth()+1, datePicker.getDayOfMonth()));
                alarmId = alarm.persist(db);

                if (Build.VERSION.SDK_INT >= 23 )
                    alarmTime.setAt(DBHelper.getTimeStr(timePicker.getHour(), timePicker.getMinute()));
                else
                    alarmTime.setAt(DBHelper.getTimeStr(timePicker.getCurrentHour(), timePicker.getCurrentMinute()));

                alarmTime.setAlarmId(alarmId);
                alarmTime.persist(db);
                break;
            case 1:
                alarm.setFromDate(Util.toPersistentDate(fromdateText.getText().toString(), sdf));
                alarm.setToDate(Util.toPersistentDate(todateText.getText().toString(), sdf));
                switch (rg.getCheckedRadioButtonId()){
                    case R.id.radio0:
                        alarm.setRule(Util.concat(s1.getSelectedItemPosition(), " ",
                                s2.getSelectedItemPosition(), " ",
                                s3.getSelectedItemPosition()));
                        break;
                    case R.id.radio1:
                        alarm.setInterval(Util.concat(minsEdit.getText(), " ",
                                hoursEdit.getText(), " ",
                                daysEdit.getText(), " ",
                                monthsEdit.getText(), " ",
                                yearsEdit.getText()));
                        break;
                }
                alarmId = alarm.persist(db);

                alarmTime.setAt(Util.toPersistentTime(attimeText.getText().toString()));
                alarmTime.setAlarmId(alarmId);
                alarmTime.persist(db);
                break;
        }


        Intent service = new Intent(this, AlarmService.class);
        service.putExtra(AlarmMsg.COL_ALARMID, String.valueOf(alarmId));
        service.setAction(AlarmService.POPULATE);
        startService(service);

        finish();
    }
}
