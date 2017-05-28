package factor.labs.indiancalendar.utils.file;

import android.content.Context;

import com.orhanobut.logger.Logger;

import java.io.InputStream;

/**
 * Created by hassanhussain on 4/28/2017.
 */

public class FileReader {

    public static String FILE_DATA = "data_old.bin";
    public static String FILE_DATA_ENCRYPTED = "data.bin";

    public static String readData(Context context, String file) {
        try {
            InputStream is = context.getAssets().open(file);
            int size = is.available();
            if(size > 0) {
                byte buffer[] = new byte[size];
                is.read(buffer);
                is.close();
                return new String(buffer, "UTF-8");
            }
        } catch (Exception e) {
            Logger.e(e, "Failed to read data from assets [%s].", file);
        }
        return "" ;
    }

}
