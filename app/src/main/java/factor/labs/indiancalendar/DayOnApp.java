package factor.labs.indiancalendar;

import android.app.Application;
import android.content.Context;

import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;

/**
 * Created by hassanhussain on 9/24/2016.
 */
public class DayOnApp extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initDatabase();
    }

    public void onTerminate() {
        super.onTerminate();
        if(labsCalendarUtils.getCalendarDBHandler() != null)
            labsCalendarUtils.getCalendarDBHandler().close();
    }

    private void initDatabase(){
        labsCalendarUtils.initDatabase(mContext);
    }
}
