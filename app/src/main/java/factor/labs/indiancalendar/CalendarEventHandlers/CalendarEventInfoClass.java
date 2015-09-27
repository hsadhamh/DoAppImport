package factor.labs.indiancalendar.CalendarEventHandlers;

/**
 * Created by hassanhussain on 7/20/2015.
 */
public class CalendarEventInfoClass {
    String sEventName;
    String sDisplayName;
    int nRegional;
    int nReligion;
    int sEventInfo;
    int nIsholiday;
    int nIsNationWideHoliday;
    int nIsWorldWideHoliday;
    String sDate;

    public String getEventName()
    { return sEventName; }

    public String getEventDisplayName()
    { return sDisplayName; }

    public String getDate()
    { return sDate; }
}
