package factor.labs.indiancalendar.CalendarObjects;

/**
 * Created by hassanhussain on 10/8/2015.
 */
public class CalendarEventMonthListItem {
    int mon, yr;
    int offset = 0;

    public CalendarEventMonthListItem(int a, int b){ mon = a; yr = b; }

    public int getMonth(){ return mon; }
    public int getYear(){ return yr; }
    public int getOffset(){ return offset; }
}
