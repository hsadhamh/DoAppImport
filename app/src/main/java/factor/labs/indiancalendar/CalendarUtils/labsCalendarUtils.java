package factor.labs.indiancalendar.CalendarUtils;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import factor.labs.indiancalendar.CalendarDbHelper.CalendarSQLiteCRUD;
import factor.labs.indiancalendar.CalendarEventHandlers.CalendarEventsPrimaryHandler;
import factor.labs.indiancalendar.R;

/**
 * Created by hassanhussain on 7/19/2015.
 */
public class labsCalendarUtils {

    private static CalendarEventsPrimaryHandler eventHandler = null;

    private static CalendarSQLiteCRUD sqliteCRUD = null;

    private static int nCountryPref = 1;

    private static int nCurrentShowingMonth = 0;
    private static int nCurrentShowingYear = 0;

    public static int getCountryPref(){ return nCountryPref; }
    public static void setCountryPref(int n){ nCountryPref = n; }

    public static void initializeBase() {
        nCurrentShowingMonth = getCurrentMonth();
        nCurrentShowingYear = getCurrentYear();
    }

    public static void initDatabase(Context oCon){
        if(sqliteCRUD == null)
            sqliteCRUD = new CalendarSQLiteCRUD(oCon);
    }

    public static CalendarSQLiteCRUD getCalendarDBHandler() { return sqliteCRUD; }

    static int[] arrayNumberOfdays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    static String[] arrayNamesOfMonth = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November","December"};

    static String[] arrayShortNamesOfMonth = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN",
            "JUL", "AUG", "SEP", "OCT", "NOV","DEC"};

    static String[] arrayWeeksNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
            "Saturday"};

    static String[] arrayShortWeeksNames = {"  SUN", "  MON", "  TUE", "  WED", "  THU", "  FRI", "  SAT"};

    static String[] arraySliderMenuItems = {"header", "Home", "Holidays", "Religious Events", "About Us", "Rate Us", "Share"};

    static int[] arraySliderMenuItemsIcons = {0, R.drawable.ic_home_nav_bar,
            R.drawable.ic_event_note_menu, R.drawable.ic_event_note_menu, R.drawable.ic_dev_group,
            R.drawable.ic_start_rate_nav, R.drawable.ic_share_btn};

    static int[] arrReligion = { 0, R.drawable.hindu, R.drawable.islam, R.drawable.christian,
            R.drawable.jew, R.drawable.sikh, R.drawable.buddist, R.drawable.orthodox};

    public static int getNumberOfDatesForMonth(int nMonth, int nYear) {
        if(nMonth <= 0 || nMonth > 12)
            nMonth = 1;

        if(nMonth == 2 && (nYear%4) == 0)
            return arrayNumberOfdays[nMonth -1]+1;
        else
            return arrayNumberOfdays[nMonth -1];
    }

    public static int getIconFormMenuSlider(int position)
    {
        return arraySliderMenuItemsIcons[position];
    }

    public static int getIconFormReligion(int position)
    {
        return arrReligion[position];
    }

    public static int getIndexForShortName(String sName)
    {
        int n = -1;
        Log.d("HolidayReligiousEvents", "month : " + sName);
        for(String s : arrayShortNamesOfMonth)
        {
            ++n;
            if(s.equalsIgnoreCase(sName))
            {
                Log.d("HolidayReligiousEvents", "month : o " + n);
                return n;
            }
        }
        Log.d("HolidayReligiousEvents", "month : " + n);
        return -1;
    }

    public static int getWeekIndexForDate(int nDate, int nMonth, int nYear)
    {
        Date oDate = new Date(nYear, nMonth, nDate);
        Calendar oCalendar = GregorianCalendar.getInstance(Locale.getDefault());
        oCalendar.setTime(oDate);
        int nWeekIndex = oCalendar.get(oCalendar.DAY_OF_WEEK);
        return nWeekIndex;
    }

    public static String getWeekNameForDate(int nDate, int nMonth, int nYear)
    {
        int nWeek = getWeekIndexForDate(nDate, nMonth, nYear);
        String sWeekName = arrayShortWeeksNames[nWeek -1];
        return sWeekName.trim();
    }

    public static String getMonthName(int month) {
        if(month <=0 || month>12) month =1;
        return arrayNamesOfMonth[month-1];
    }

    public static String getMonthShortName(int month) {
        if(month <=0 || month>12) month =1;
        return arrayShortNamesOfMonth[month-1];
    }

    public static String[] getArrayWeek()
    {
        return arrayWeeksNames;
    }

    public static String[] getArrayShortWeekNames()
    {
        return arrayShortWeeksNames;
    }

    public static String getWeekName(int nOffset)
    {
        return arrayWeeksNames[nOffset - 1];
    }

    public static String getShortWeekName(int nOffset)
    {
        return arrayShortWeeksNames[nOffset-1];
    }

    public static int getTodaysDate() {
        Calendar oCalendar = Calendar.getInstance(Locale.getDefault());
        int nDate = oCalendar.get(oCalendar.DATE);
        return nDate;
    }

    public static int getCurrentMonth() {
        Calendar oCalendar = Calendar.getInstance(Locale.getDefault());
        int nMonth = oCalendar.get(oCalendar.MONTH);
        return nMonth+1;
    }

    public static int getCurrentYear() {
        Calendar oCalendar = Calendar.getInstance(Locale.getDefault());
        int nYear = oCalendar.get(oCalendar.YEAR);
        return nYear;
    }

    public static String[] getSliderMenuItems()
    {
        return arraySliderMenuItems;
    }

    public static CalendarMonthYearClass subtractMonthYear(CalendarMonthYearClass oObject, int offset) {
        int nMonth = oObject.nMonth - offset;
        int nYear = oObject.nYear;
        while (nMonth <= 0) {
            nMonth = nMonth + 12;
            nYear--;
        }
        CalendarMonthYearClass oReturn = new CalendarMonthYearClass();
        oReturn.nMonth = nMonth;
        oReturn.nYear = nYear;
        return oReturn;
    }

    public static CalendarMonthYearClass addMonthYear(CalendarMonthYearClass oObject, int offset) {
        int nMonth = oObject.nMonth + offset;
        int nYear = oObject.nYear;
        while (nMonth > 12) {
            nMonth = nMonth - 12;
            nYear++;
        }
        CalendarMonthYearClass oReturn = new CalendarMonthYearClass();
        oReturn.nMonth = nMonth;
        oReturn.nYear = nYear;
        return oReturn;
    }
}
