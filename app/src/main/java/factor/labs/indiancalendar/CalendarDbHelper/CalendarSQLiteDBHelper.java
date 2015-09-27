package factor.labs.indiancalendar.CalendarDbHelper;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by hassanhussain on 9/9/2015.
 */
public class CalendarSQLiteDBHelper extends SQLiteOpenHelper{

    private String DB_PATH;
    private String DB_NAME; //= "CalendarEvents.eve";

    public SQLiteDatabase database;
    public Context context = null;

    public CalendarSQLiteDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, 2);
        this.context = context;

        String packageName = context.getPackageName();
        DB_PATH = String.format("//data//data//%s//databases//", packageName);
        DB_NAME = name;

        openDataBase();
    }

    //  create
    public void createDataBase() {
        boolean dbExist = checkIfDataBase();
        if (!dbExist) {
            this.getReadableDatabase();

        } else {
            Log.i(this.getClass().toString(), "Database already exists");
        }
        try {
            copyDataBase();
        } catch (IOException e) {
            Log.e(this.getClass().toString(), "Copying error");
            throw new Error("Error copying database!");
        }
    }

    //  copy DB file
    private void copyDataBase() throws IOException {
        Log.d("copyDataBase", "Copying db");
        InputStream externalDbStream = context.getAssets().open("calendar.db");
        String outFileName = DB_PATH + DB_NAME;
        OutputStream localDbStream = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = externalDbStream.read(buffer)) > 0) {
            localDbStream.write(buffer, 0, bytesRead);
        }
        localDbStream.close();
        externalDbStream.close();

    }

    //  Check DB files
    private boolean checkIfDataBase() {
        SQLiteDatabase checkDb = null;
        try {
            String path = DB_PATH + DB_NAME;
            checkDb = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLException e) {
            Log.e(this.getClass().toString(), "Error while checking db");
        }

        if (checkDb != null) {
            checkDb.close();
        }
        return checkDb != null;
    }

    //  Open DB files
    public SQLiteDatabase openDataBase() throws SQLException {
        String path = DB_PATH + DB_NAME;
        if (database == null) {
            createDataBase();
            database = SQLiteDatabase.openDatabase(path, null,
                    SQLiteDatabase.OPEN_READWRITE);
        }
        return database;
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //  tables create
        //  values populate
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //  Table format changes
        //  delete (Drop) tables
    }

    public SQLiteDatabase getDb() {
        return database;
    }
}
