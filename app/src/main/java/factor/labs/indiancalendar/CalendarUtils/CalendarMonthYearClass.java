package factor.labs.indiancalendar.CalendarUtils;

/**
 * Created by hassanhussain on 7/23/2015.
 */
public class CalendarMonthYearClass {
    int nMonth, nYear;

    public CalendarMonthYearClass() {}

    public CalendarMonthYearClass(int Month, int nYr)
    { nMonth = Month; nYear = nYr; }

    public void setMonth(int month){ nMonth = month; }
    public void setYear(int year){ nYear = year; }

    public int getMonth(){return nMonth; }
    public int getYear(){return nYear; }
}
