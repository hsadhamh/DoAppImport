package factor.labs.indiancalendar.CalendarObjects;

import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;

/**
 * Created by hassanhussain on 9/26/2015.
 */
public class CalendarEmptyEventListItem {
    public int date, mon, yr;
    public int week, offset;
    public int weekName;

    public boolean visible = false;

    public CalendarEmptyEventListItem(int a, int b, int c, int n)
    {
        date = a;
        mon = b;
        yr = c;
        offset = n;
        weekName = labsCalendarUtils.getWeekIndexForDate(date, mon, yr);
    }
}
