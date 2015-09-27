package factor.labs.indiancalendar.CalendarUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hassanhussain on 7/20/2015.
 */
public class CalendarEventDateClass {
    int nYear;
    int nMonth;
    int nDate;
    List<CalendarEventInfoClass> Events = new ArrayList<CalendarEventInfoClass>();

    public int getDate() {return nDate;}
    public int getYear() {return nYear;}
    public int getMonth() {return nMonth;}

    public List<CalendarEventInfoClass> getEvents(){ return Events; }
}
