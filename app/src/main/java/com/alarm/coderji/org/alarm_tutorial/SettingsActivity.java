package com.alarm.coderji.org.alarm_tutorial;

import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

import com.alarm.coderji.org.alarm_tutorial.Utilities.AlarmUtils;

/**
 * Created by Osama on 12/25/2015.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getFragmentManager().beginTransaction().replace(R.id.preference_container, new MyPreferenceFragment()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static class MyPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
            updatePreferences();
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            updatePreference(key);
        }

        private void updatePreferences(){
            updatePreference(AlarmUtils.TIME_OPTION);
            updatePreference(AlarmUtils.DATE_RANGE);
            updatePreference(AlarmUtils.DATE_FORMAT);
            updatePreference(AlarmUtils.RINGTONE_PREF);
        }

        private void updatePreference(String key){
            Preference pref = findPreference(key);

            if(pref instanceof ListPreference){
                ListPreference list = (ListPreference) pref;
                pref.setSummary(list.getEntry());
                return;
            }

            if(AlarmUtils.RINGTONE_PREF.equals(key)){
                Uri ringtoneUri = Uri.parse(AlarmUtils.getRingtone());
                Ringtone ringtone = RingtoneManager.getRingtone(getActivity().getApplicationContext(), ringtoneUri);
                if(ringtone != null) pref.setSummary(ringtone.getTitle(getActivity().getApplicationContext()));
            }
        }
    }
}
