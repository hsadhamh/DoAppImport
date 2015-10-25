package factor.labs.indiancalendar.CalendarObjects;

import factor.labs.indiancalendar.CalendarDbHelper.CalendarEventMaster;
import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;

/**
 * Created by hassanhussain on 10/8/2015.
 */
public class CalendarEventListItem {
    public int date, mon, yr;
    public int weekName, offset;
    public CalendarEventMaster oEventInfo;

    public boolean visible = true;

    public CalendarEventListItem(CalendarEventMaster obj, int n){
        date = obj.getDate();
        mon = obj.getMonth();
        yr = obj.getYear();
        offset = n;
        weekName = labsCalendarUtils.getWeekIndexForDate(date, mon, yr);
        oEventInfo = obj;
    }
}
