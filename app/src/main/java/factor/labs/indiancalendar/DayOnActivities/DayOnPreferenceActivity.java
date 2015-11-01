package factor.labs.indiancalendar.DayOnActivities;

/**
 * Created by hassanhussain on 10/25/2015.
 */
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;

import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import factor.labs.indiancalendar.CalendarInterfaces.IDayOnPrefCallBack;
import factor.labs.indiancalendar.CalendarUI.CalendarDialogUI.common.prefs.CheckBoxPreference;
import factor.labs.indiancalendar.CalendarUI.DayOnDatePicker.time.RadialPickerLayout;
import factor.labs.indiancalendar.CalendarUI.DayOnDatePicker.time.TimePickerDialog;
import factor.labs.indiancalendar.CalendarUtils.CalendarConstants;
import factor.labs.indiancalendar.R;

@SuppressLint("NewApi")
public class DayOnPreferenceActivity extends AppCompatActivity
        implements IDayOnPrefCallBack{

    SettingsFragment settings = null;

    public void onPreferenceSelection(final int n) {
        SharedPreferences prefs = getSharedPreferences(CalendarConstants.DAYON_SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        String sTime = "06:00";
        if(n == 1)
            sTime = prefs.getString(CalendarConstants.DAYON_TIME_REMINDER_HOLIDAY, "06:00");
        else if(n == 2)
            sTime = prefs.getString(CalendarConstants.DAYON_TIME_REMINDER_RELIGIOUS, "06:00");

        String[] minHr = sTime.split(":");
        int hr = Integer.parseInt(minHr[0]);
        int min = Integer.parseInt(minHr[1]);

        TimePickerDialog oTimePicker = new TimePickerDialog();
        oTimePicker.setStartTime(hr, min);
        oTimePicker.show(getSupportFragmentManager(), "Set Time");
        oTimePicker.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                String sTimeFormat = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);

                SharedPreferences prefs = getSharedPreferences(CalendarConstants.DAYON_SHARED_PREFERENCE_NAME, MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                if(n == 1) {
                    edit.putString(CalendarConstants.DAYON_TIME_REMINDER_HOLIDAY, sTimeFormat);
                    settings.setTimeHolidayButton.setSummary(sTimeFormat);
                }
                else if(n == 2) {
                    edit.putString(CalendarConstants.DAYON_TIME_REMINDER_RELIGIOUS, sTimeFormat);
                    settings.setTimeReligiousButton.setSummary(sTimeFormat);
                }
                edit.apply();
            }
        });

    }

    public static class SettingsFragment extends PreferenceFragment{
        IDayOnPrefCallBack moCallback = null;

        Preference setTimeHolidayButton = null;
        CheckBoxPreference setTimeHolidayCheck = null;
        Preference setTimeReligiousButton = null;
        CheckBoxPreference setTimeReligiousCheck = null;

        SharedPreferences mPreferences = null; //
        SharedPreferences.Editor mSharedPrefEditor = null;

        boolean mEnableAlarmHoliday = true;
        boolean mEnableAlarmReligious = true;
        String mAlarmTimeHoliday = "";
        String mAlarmTimeReligious = "";

        public static SettingsFragment init(IDayOnPrefCallBack oCallback){
            SettingsFragment fragment = new SettingsFragment();
            fragment.setCallBack(oCallback);
            return fragment;
        }

        public void onStart(){
            super.onStart();
            mPreferences = getActivity().getSharedPreferences(CalendarConstants.DAYON_SHARED_PREFERENCE_NAME, MODE_PRIVATE);
            mEnableAlarmHoliday = mPreferences.getBoolean(CalendarConstants.DAYON_TIME_REMINDER_HOLIDAY_ENABLE, true);
            mEnableAlarmReligious = mPreferences.getBoolean(CalendarConstants.DAYON_TIME_REMINDER_RELIGIOUS_ENABLE, true);
            mAlarmTimeHoliday = mPreferences.getString(CalendarConstants.DAYON_TIME_REMINDER_HOLIDAY, "06:00");
            mAlarmTimeReligious = mPreferences.getString(CalendarConstants.DAYON_TIME_REMINDER_RELIGIOUS, "06:00");

            setTimeHolidayButton.setSummary(mAlarmTimeHoliday);
            setTimeReligiousButton.setSummary(mAlarmTimeReligious);
            setTimeHolidayCheck.setChecked(mEnableAlarmHoliday);
            setTimeHolidayButton.setEnabled(mEnableAlarmHoliday);
            setTimeReligiousCheck.setChecked(mEnableAlarmReligious);
            setTimeReligiousButton.setEnabled(mEnableAlarmReligious);
        }

        public void onResume(){
            super.onResume();

        }

        public void onPause(){
            SharedPreferences.Editor edit = mPreferences.edit();
            edit.putBoolean(CalendarConstants.DAYON_TIME_REMINDER_HOLIDAY_ENABLE, mEnableAlarmHoliday);
            edit.putBoolean(CalendarConstants.DAYON_TIME_REMINDER_RELIGIOUS_ENABLE, mEnableAlarmReligious);
            edit.apply();
            super.onPause();
        }

        public void onStop(){
            super.onStop();

        }

        public void setCallBack(IDayOnPrefCallBack oCallback){ moCallback = oCallback; }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            setTimeHolidayButton = findPreference("SetAlarmHoliday");
            setTimeReligiousButton = findPreference("SetAlarmHoliday1");
            setTimeHolidayCheck = (CheckBoxPreference) findPreference("holiday_preference_enable");
            setTimeReligiousCheck = (CheckBoxPreference) findPreference("religious_preference_enable");

            if (setTimeHolidayButton != null) {
                setTimeHolidayButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference arg0) {
                        moCallback.onPreferenceSelection(1);
                        return true;
                    }
                });
            }

            if (setTimeReligiousButton != null) {
                setTimeReligiousButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference arg0) {
                        moCallback.onPreferenceSelection(2);
                        return true;
                    }
                });
            }

            setTimeHolidayCheck = (CheckBoxPreference)findPreference("holiday_preference_enable");
            if (setTimeHolidayCheck != null) {
                setTimeHolidayCheck.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference arg0) {
                        boolean bChecked = ((CheckBoxPreference) arg0).isChecked();
                        setTimeHolidayButton.setEnabled(bChecked);
                        mEnableAlarmHoliday = bChecked;
                        return true;
                    }
                });
            }

            setTimeReligiousCheck = (CheckBoxPreference)findPreference("religious_preference_enable");
            if (setTimeReligiousCheck != null) {
                setTimeReligiousCheck.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference arg0) {
                        boolean bChecked = ((CheckBoxPreference) arg0).isChecked();
                        setTimeReligiousButton.setEnabled(bChecked);
                        mEnableAlarmReligious = bChecked;
                        return true;
                    }
                });
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_activity_custom);
        Toolbar moActionBar = (Toolbar) findViewById(R.id.id_cal_action_bar);

        setSupportActionBar(moActionBar);

        moActionBar.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        moActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        settings = SettingsFragment.init(this);

        if (getFragmentManager().findFragmentById(R.id.content_frame) == null) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, settings).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", 9000);
        setResult(RESULT_OK,returnIntent);
        super.onBackPressed();
    }
}