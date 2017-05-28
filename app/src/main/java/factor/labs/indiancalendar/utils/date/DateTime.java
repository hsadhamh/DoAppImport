package factor.labs.indiancalendar.utils.date;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import factor.labs.indiancalendar.utils.Constants;

/**
 * Created by hassanhussain on 5/26/2017.
 */

public class DateTime {

    static int[] arrayNumberOfdays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    static String[] arrayNamesOfMonth = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November","December"};

    static String[] arrayShortNamesOfMonth = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN",
            "JUL", "AUG", "SEP", "OCT", "NOV","DEC"};

    static String[] arrayWeeksNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
            "Saturday"};

    static String[] arrayShortWeeksNames = {"  SUN", "  MON", "  TUE", "  WED", "  THU", "  FRI", "  SAT"};

    public static long getTimeStampGivenDateTime(String sInDate) throws ParseException {
        DateTimeFormatter dfDateFormat = DateTimeFormat.forPattern(Constants.dfDateFormat).withZoneUTC();
        return dfDateFormat.parseDateTime(sInDate).getMillis()/1000;
    }

    public static String getDateTimeGivenTimeStamp(long lInTimeStamp){
        DateFormat dfFormattedDate = new SimpleDateFormat(Constants.dfDateFormat, Locale.getDefault());
        return dfFormattedDate.format(new java.util.Date (lInTimeStamp*1000));
    }

    public static String prepareDateTimeString(int nHour, int nMinute, int nSecond, int nDate, int nMonth, int nYear){
        return String.format(Locale.getDefault(), Constants.sDateFormatTemplate, nDate, nMonth, nYear, nHour, nMinute, nSecond);
    }

    public static int getNumberOfDaysForMonth(int nMonth, int nYear) {
        if(nMonth <= 0 || nMonth > 12)
            nMonth = 1;

        if(nMonth == 2 && (nYear%4) == 0)
            return arrayNumberOfdays[nMonth -1] + 1;
        else
            return arrayNumberOfdays[nMonth -1];
    }

    public static int getDaysInCurrForPrevMonth(int nMonth, int nYear){
        GregorianCalendar oGregCalendar = new GregorianCalendar(nYear, nMonth - 1, 1);
        return oGregCalendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static int getDateFromTimeStamp(long timestamp){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return cal.get(Calendar.DATE);
    }

    public static int getMonthFromTimeStamp(long timestamp){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return cal.get(Calendar.MONTH);
    }

    public static int getYearFromTimeStamp(long timestamp){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return cal.get(Calendar.YEAR);
    }

}
