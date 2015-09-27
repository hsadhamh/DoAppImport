package factor.labs.indiancalendar.CalendarUtils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import factor.labs.indiancalendar.CalendarEventHandlers.CalendarEventDateClass;
import factor.labs.indiancalendar.CalendarEventHandlers.CalendarEventInfoClass;
import factor.labs.indiancalendar.CalendarEventHandlers.CalendarEventMonthClass;
import factor.labs.indiancalendar.CalendarEventHandlers.CalendarEventYearClass;
import factor.labs.indiancalendar.CalendarEventHandlers.CalendarEventsPrimaryHandler;

/**
 * Created by hassanhussain on 7/30/2015.
 */
public class labsCalendarBaseClass {

    Context moContext;
    CalendarEventsPrimaryHandler moEventHandler = null;

    public labsCalendarBaseClass(Context oCon)
    {
        moContext = oCon;
        moEventHandler = new CalendarEventsPrimaryHandler(moContext);
    }

    public List<CalendarEventDateClass> getListOfEventsForMonth(int nMonth, int nYear)
    {
        return moEventHandler.getEventsForMonth(nMonth, nYear);
    }
}
