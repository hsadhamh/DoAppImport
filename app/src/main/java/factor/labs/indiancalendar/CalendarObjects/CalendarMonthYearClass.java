package factor.labs.indiancalendar.CalendarObjects;

/**
 * Created by hassanhussain on 7/23/2015.
 */
public class CalendarMonthYearClass {
    public int nMonth;
    public int nYear;

    public CalendarMonthYearClass() {}

    public CalendarMonthYearClass(int Month, int nYr)
    { nMonth = Month; nYear = nYr; }

    public void setMonth(int month){ nMonth = month; }
    public void setYear(int year){ nYear = year; }

    public int getMonth(){return nMonth; }
    public int getYear(){return nYear; }
}
