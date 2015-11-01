package factor.labs.indiancalendar.CalendarServices;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import factor.labs.indiancalendar.CalendarUtils.CalendarConstants;
import factor.labs.indiancalendar.DayOnMonthHomeActivity;
import factor.labs.indiancalendar.R;

public class DayOnAndroidService extends Service implements ICalendarService {

    IBinder mBinder = new DayOnServiceBinder();
    NotificationCompat.Builder mNotifyBuilder = null;
    NotificationManager mNotificationManager = null;
    DayOnAlarmManger mAlarmManager = null;

    private Handler mHandler = new Handler();

    // timer handling
    private Timer mTimer = null;
    int mnSyncInterval = 60*60*1000; // 1 hour interval

    public class DayOnServiceBinder extends Binder{
        public ICalendarService getService(){ return DayOnAndroidService.this; }
    }

    public DayOnAndroidService() { }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    @Override
    public void onCreate() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Toast.makeText(this, "Service is Created", Toast.LENGTH_LONG).show();
        mAlarmManager = new DayOnAlarmManger(getApplicationContext());

        if(mTimer != null)
            mTimer.cancel();
        else
            mTimer = new Timer();
        // schedule task
        mTimer.scheduleAtFixedRate(new ScheduleSyncTask(), 0, mnSyncInterval);
    }

    /*public void setScheduleForSyncOperation(){
        //  register sync schedule here for the first time.
        mAlarmManager.setSyncSchedule();
    }*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service started", Toast.LENGTH_LONG).show();
        if(intent != null) {
            int nTaskType = intent.getIntExtra("TaskType", 0);
            switch(nTaskType){
                case 1:
                    SyncLocalData(intent);
                    break;
                default:
                    // do nothing.
            }
        }
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public void SyncLocalData(Intent intent) {
        //Toast.makeText(this, "Service call", Toast.LENGTH_LONG).show();

        // Set the content for Notification
        String sShowMsg = "Sync Count :" + intent.getStringExtra("CountSync");
        ShowNotification(9001, "Sync Alert", sShowMsg);
    }

    @Override
    public void OnSharedPreferenceChange() {
        SharedPreferences prefs = getSharedPreferences(CalendarConstants.DAYON_SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        boolean enableHoliday = prefs.getBoolean(CalendarConstants.DAYON_TIME_REMINDER_HOLIDAY_ENABLE, true);
        boolean enableReligious = prefs.getBoolean(CalendarConstants.DAYON_TIME_REMINDER_RELIGIOUS_ENABLE, true);
        String timeHoliday = prefs.getString(CalendarConstants.DAYON_TIME_REMINDER_HOLIDAY, "06:00");
        String timeReligious = prefs.getString(CalendarConstants.DAYON_TIME_REMINDER_RELIGIOUS, "06:00");

        Toast.makeText(getApplicationContext(), "Shared Preference changes : " + enableHoliday + " : " + timeHoliday
                        + " : " + enableReligious + " : " + timeReligious,
                Toast.LENGTH_SHORT).show();
    }

    public void ShowNotification(int notificationID, String sTitle, String sText){
        Intent resultIntent = new Intent(this, DayOnMonthHomeActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Sets an ID for the notification, so it can be updated
        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(sTitle)
                .setContentText(sText)
                .setSmallIcon(R.drawable.ic_icon_app);
        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);
        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;
        mNotifyBuilder.setDefaults(defaults);
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(notificationID, mNotifyBuilder.build());
    }

    class ScheduleSyncTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    // display toast
                    Toast.makeText(getApplicationContext(), getDateTime(),
                            Toast.LENGTH_SHORT).show();
                }

            });
        }

        private String getDateTime() {
            // get date time in custom format
            SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]", Locale.getDefault());
            return sdf.format(new Date());
        }

    }
}
