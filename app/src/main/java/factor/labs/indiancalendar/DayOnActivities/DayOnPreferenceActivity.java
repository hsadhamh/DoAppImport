package factor.labs.indiancalendar.DayOnActivities;

/**
 * Created by hassanhussain on 10/25/2015.
 */
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.Preference;

import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import factor.labs.indiancalendar.CalendarInterfaces.IDayOnPrefCallBack;
import factor.labs.indiancalendar.CalendarUI.CalendarDialogUI.common.prefs.CheckBoxPreference;
import factor.labs.indiancalendar.CalendarUI.DayOnDatePicker.time.TimePickerDialog;
import factor.labs.indiancalendar.R;

@SuppressLint("NewApi")
public class DayOnPreferenceActivity extends AppCompatActivity
        implements IDayOnPrefCallBack {

    SettingsFragment settings = null;

    @Override
    public void onPreferenceSelection(int n) {
        switch(n) {
            case 1:
                TimePickerDialog oTimePicker = new TimePickerDialog();
                oTimePicker.setStartTime(6, 0);
                oTimePicker.show(getSupportFragmentManager(), "Set Time");
                break;
            default:
                break;
        }
    }

    public static class SettingsFragment extends PreferenceFragment {
        IDayOnPrefCallBack moCallback = null;

        Preference setTimeHolidayButton = null;
        CheckBoxPreference setTimeHolidayCheck = null;
        Preference setTimeReligiousButton = null;
        CheckBoxPreference setTimeReligiousCheck = null;

        public static SettingsFragment init(IDayOnPrefCallBack oCallback){
            SettingsFragment fragment = new SettingsFragment();
            fragment.setCallBack(oCallback);
            return fragment;
        }

        public void setCallBack(IDayOnPrefCallBack oCallback){ moCallback = oCallback; }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            setTimeHolidayButton = (Preference)findPreference("SetAlarmHoliday");
            setTimeReligiousButton = (Preference)findPreference("SetAlarmHoliday1");

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
                        moCallback.onPreferenceSelection(1);
                        return true;
                    }
                });
            }

            setTimeHolidayCheck = (CheckBoxPreference)findPreference("holiday_preference_enable");
            if (setTimeHolidayCheck != null) {
                setTimeHolidayCheck.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference arg0) {
                        if(((CheckBoxPreference) arg0).isChecked())
                            setTimeHolidayButton.setEnabled(true);
                        else
                            setTimeHolidayButton.setEnabled(false);
                        return true;
                    }
                });
            }

            setTimeReligiousCheck = (CheckBoxPreference)findPreference("religious_preference_enable");
            if (setTimeReligiousCheck != null) {
                setTimeReligiousCheck.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference arg0) {
                        if(((CheckBoxPreference) arg0).isChecked())
                            //moCallback.onPreferenceSelection(2);
                            setTimeReligiousButton.setEnabled(true);
                        else
                            setTimeReligiousButton.setEnabled(false);
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
}