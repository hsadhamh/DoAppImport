package factor.labs.indiancalendar.utils.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import factor.labs.indiancalendar.utils.Constants;

/**
 * Created by hassanhussain on 5/26/2017.
 */

public class DateTime {

    public static long getTimeStampGivenDateTime(String sInDate) throws ParseException {
        DateFormat dfFormattedDate = new SimpleDateFormat(Constants.dfDateFormat, Locale.getDefault());
        Date dDateObject = dfFormattedDate.parse(sInDate);
        return dDateObject.getTime();
    }

    public static String getDateTimeGivenTimeStamp(long lInTimeStamp){
        DateFormat dfFormattedDate = new SimpleDateFormat(Constants.dfDateFormat, Locale.getDefault());
        return dfFormattedDate.format(new java.util.Date (lInTimeStamp*1000));
    }

    public static String prepareDateTimeString(int nHour, int nMinute, int nSecond, int nDate, int nMonth, int nYear){
        return String.format(Locale.getDefault(), Constants.sDateFormatTemplate, nDate, nMonth, nYear, nHour, nMinute, nSecond);
    }


}
