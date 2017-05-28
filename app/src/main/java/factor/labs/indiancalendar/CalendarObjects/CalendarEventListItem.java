package factor.labs.indiancalendar.CalendarObjects;

import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;
import factor.labs.indiancalendar.utils.database.Events;
import factor.labs.indiancalendar.utils.date.DateTime;

/**
 * Created by hassanhussain on 10/8/2015.
 */
public class CalendarEventListItem {
    public int date, mon, yr;
    public int weekName, offset;
    public Events oEventInfo;

    public boolean visible = true;

    public CalendarEventListItem(Events obj, int n){
        date = DateTime.getDateFromTimeStamp(obj.getStart_date());
        mon = DateTime.getMonthFromTimeStamp(obj.getStart_date());
        yr = DateTime.getYearFromTimeStamp(obj.getStart_date());
        offset = n;
        weekName = labsCalendarUtils.getWeekIndexForDate(date, mon, yr);
        oEventInfo = obj;
    }
}
