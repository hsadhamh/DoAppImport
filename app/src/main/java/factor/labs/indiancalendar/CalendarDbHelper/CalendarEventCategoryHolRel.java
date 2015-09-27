package factor.labs.indiancalendar.CalendarDbHelper;

import java.util.List;

/**
 * Created by hassanhussain on 9/11/2015.
 */
public class CalendarEventCategoryHolRel {
    boolean bHolidayFlag;
    boolean bReligiousFlag;
    String sReligion;

    Object oInfo;
    List<Object> listReminders;
    int nAddressID;

    public CalendarEventCategoryHolRel()
    {
        bHolidayFlag = false;
        bReligiousFlag = false;
        sReligion = "";
        oInfo = null;
        listReminders = null;
        nAddressID = 0;
    }

}
