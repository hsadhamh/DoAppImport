package factor.labs.indiancalendar;

import android.app.Application;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;

/**
 * Created by hassanhussain on 9/24/2016.
 */
public class DayOnApp extends Application {
    private String msPassword = "ShLab!17KctAy";
    private String mSecureFile  = "DayOnDefense";
    /** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
    public static final boolean ENCRYPTED = false;

    @Override
    public void onCreate() {
        super.onCreate();
        initLogger();
        initDatabase();
    }

    public void onTerminate() {
        super.onTerminate();
        if(labsCalendarUtils.getCalendarDBHandler() != null)
            labsCalendarUtils.getCalendarDBHandler().close();
    }

    private void initDatabase(){ labsCalendarUtils.initDatabase(getApplicationContext()); }

    private void initLogger(){ Logger.init("debug-lab").logLevel(LogLevel.FULL); }

}
