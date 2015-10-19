package factor.labs.indiancalendar.CalendarUtils;

/**
 * Created by hassanhussain on 10/8/2015.
 */
public class CalendarEventDateListItem {
    public int date, mon, yr;
    public int week, offset;
    public int weekName;

    CalendarEventDateListItem(int a, int b, int c, int n)
    {
        date = a;
        mon = b;
        yr = c;
        offset = n;
        weekName = labsCalendarUtils.getWeekIndexForDate(date, mon,yr);
    }
}
