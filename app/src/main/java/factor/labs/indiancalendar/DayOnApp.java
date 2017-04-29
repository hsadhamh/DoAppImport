package factor.labs.indiancalendar;

import android.app.Application;
import android.content.Context;

import org.greenrobot.greendao.database.Database;

import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;
import factor.labs.indiancalendar.database.DaoMaster;
import factor.labs.indiancalendar.database.DaoSession;

/**
 * Created by hassanhussain on 9/24/2016.
 */
public class DayOnApp extends Application {
    private static Context mContext;

    /** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
    public static final boolean ENCRYPTED = true;

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initDatabase();

        initDatabaseV2();
    }

    public void onTerminate() {
        super.onTerminate();
        if(labsCalendarUtils.getCalendarDBHandler() != null)
            labsCalendarUtils.getCalendarDBHandler().close();
    }

    private void initDatabase(){
        labsCalendarUtils.initDatabase(mContext);
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    private void initDatabaseV2(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "notes-db-encrypted" : "notes-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }
}
