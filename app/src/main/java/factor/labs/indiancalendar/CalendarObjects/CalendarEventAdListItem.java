package factor.labs.indiancalendar.CalendarObjects;

import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;

/**
 * Created by hassanhussain on 8/27/2016.
 */
public class CalendarEventAdListItem {
    public int date, mon, yr;
    public int week, offset;
    public int weekName;

    public boolean visible = false;

    public CalendarEventAdListItem(int a, int b, int c, int n)
    {
        date = a;
        mon = b;
        yr = c;
        offset = n;
        weekName = labsCalendarUtils.getWeekIndexForDate(date, mon, yr);
    }
}
