package factor.labs.indiancalendar;

import android.app.Application;

import com.github.hussainderry.securepreferences.SecurePreferences;
import com.github.hussainderry.securepreferences.model.DigestType;
import com.github.hussainderry.securepreferences.model.SecurityConfig;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import org.greenrobot.greendao.database.Database;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;
import factor.labs.indiancalendar.utils.GenericUtils;
import factor.labs.indiancalendar.utils.database.DaoMaster;
import factor.labs.indiancalendar.utils.database.DaoSession;

/**
 * Created by hassanhussain on 9/24/2016.
 */
public class DayOnApp extends Application {
    private String msPassword = "ShLab!17KctAy";
    private String mSecureFile  = "DayonDefense";
    /** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
    public static final boolean ENCRYPTED = false;
    private static DaoSession mDaoSession;
    private SecurePreferences mPreferences;
    ExecutorService mExecuteService = Executors.newSingleThreadExecutor();

    @Override
    public void onCreate() {
        super.onCreate();
        initDatabase();

        initDatabaseV2();
        initLogger();
        initPreferences();
    }

    public void onTerminate() {
        super.onTerminate();
        if(labsCalendarUtils.getCalendarDBHandler() != null)
            labsCalendarUtils.getCalendarDBHandler().close();
    }

    private void initDatabase(){ labsCalendarUtils.initDatabase(getApplicationContext()); }

    public static DaoSession getDaoSession() { return mDaoSession; }

    private void initDatabaseV2(){
        //
        //  Check if DB exists already. If not, we need to run DB initialize code.
        //
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "events-db-encrypted" : "events-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb(msPassword) : helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();

        if(CheckIfDBNeedInitialize()) {
            //  TODO: Run init code as ASYNC task.
            GenericUtils.startAsyncDbSynchronization(false, getApplicationContext());
        }
    }

    private void initLogger(){ Logger.init("debug-lab").logLevel(LogLevel.FULL); }

    private void initPreferences(){
        // Full Configurations
        SecurityConfig fullConfig = new SecurityConfig.Builder(msPassword)
                .setAesKeySize(256)
                .setPbkdf2SaltSize(32)
                .setPbkdf2Iterations(24000)
                .setDigestType(DigestType.SHA256)
                .build();

        mPreferences = SecurePreferences.getInstance(DayOnApp.this, mSecureFile, fullConfig);
    }

    private boolean CheckIfDBNeedInitialize(){
        //  Now check for shared preference, if set, no need to run init code.
        //  Now verify if rows found in DB, if found. no need to run init code.
        //  TODO: Run init code on USER actions.
        if(!mPreferences.getBoolean("DbInitSuccess", false))
            return true;

        if(!(mDaoSession.getEventsDao().count() >= 0))
            return true;
        //  By default no sync required.
        return false;
    }


}
