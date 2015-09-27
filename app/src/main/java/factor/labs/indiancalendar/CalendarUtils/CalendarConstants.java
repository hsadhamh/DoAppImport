package factor.labs.indiancalendar.CalendarUtils;

/**
 * Created by hassanhussain on 7/20/2015.
 */
public class CalendarConstants {
    public final static int CALENDAR_PREVIOUS_MONTH_DATE = -1;
    public final static int CALENDAR_CURRENT_MONTH_DATE = 0;
    public final static int CALENDAR_NEXT_MONTH_DATE = 1;

    public final static int CALENDAR_DATE_TYPE = 0;
    public final static int CALENDAR_EVENT_TYPE = 1;
    public final static int CALENDAR_EVENT_EMPTY = 2;

    public final static int CALENDAR_SCHEDULE_MONTH_TYPE = 256;
    public final static int CALENDAR_SCHEDULE_DAY_TYPE = 512;
    public final static int CALENDAR_SCHEDULE_EMPTY_EVENT = 1024;
    public final static int CALENDAR_SCHEDULE_LOAD_UP = 2048;
    public final static int CALENDAR_SCHEDULE_LOAD_DOWN = 4096;

    public final static int CALENDAR_MONTH_SWIPE_LEFT = 1;
    public final static int CALENDAR_MONTH_SWIPE_RIGHT = -1;

    public final static int CALENDAR_SCHEDULE_MONTH_EVENTS_LOAD_THRESHOLD = 6;

    public final static String CALENDAR_EVENT_SHARED_PREF = "PREF_SHOW_EVENTS";
    public static final String CALENDAR_SHARED_PREF_COUNTRY = "COUNTRY";

    public final static int CALENDAR_SHOW_EVENT_FILTER = 0; // All events
    public final static int CALENDAR_SHOW_EVENT_US = 1; // All events
    public final static int CALENDAR_SHOW_EVENT_IN = 2; // All events
}
