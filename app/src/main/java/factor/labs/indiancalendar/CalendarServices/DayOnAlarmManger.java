package factor.labs.indiancalendar.CalendarServices;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by hassanhussain on 10/25/2015.
 */
public class DayOnAlarmManger {
    Context mContext = null;

    public DayOnAlarmManger(Context oCon){
        mContext = oCon;
    }

    public void setSyncSchedule(){
        //  register sync schedule here for the first time.
        Intent alarmIntent = new Intent(mContext, DayOnBroadCastReceiver.class);
        alarmIntent.putExtra("SyncData", true);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0,
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                Calendar.getInstance().getTimeInMillis(), 60 * 60 * 1000, pendingIntent);
    }
}
