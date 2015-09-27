package factor.labs.indiancalendar.CalendarDbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hassanhussain on 9/11/2015.
 */
public class CalendarEventMaster {
    private int nEventID;
    public int getEventID(){ return nEventID; }
    public void setEventID(int n){ nEventID = n; }

    private String sEventName;
    public String getEventName(){ return sEventName; }
    public void setEventName(String n){ sEventName = n; }

    private String sDisplayName;
    public String getDisplayName(){ return sDisplayName; }
    public void setDisplayName(String n){ sDisplayName = n; }

    private int nDate;
    public int getDate(){ return nDate; }
    public void setDate(int n){ nDate = n; }

    private int nMonth;
    public int getMonth(){ return nMonth; }
    public void setMonth(int n){ nMonth = n; }

    private int nYear;
    public int getYear(){ return nYear; }
    public void setYear(int n){ nYear = n; }

    private String sDateString;
    public String getDateString(){ return sDateString; }
    public void setDateString(String n){ sDateString = n; }


    private String sWeekDayName;

    private int nCountry;
    public int getCountry(){ return nCountry; }
    public void setCountry(int n){ nCountry = n; }

    private int nState;
    public int getState(){ return nState; }
    public void setState(int n){ nState = n; }

    private boolean bReligiousFlag;
    public void setReligionFlag() { bReligiousFlag = true; }
    public void resetReligionFlag() { bReligiousFlag = false; }
    public boolean isReligionEvent() { return bReligiousFlag; }

    private boolean bHolidayFlag;
    public void setHolidayFlag() { bHolidayFlag = true; }
    public void resetHolidayFlag() { bHolidayFlag = false; }
    public boolean isHolidayEvent() { return bHolidayFlag; }

    private int nReligionID;
    public int getReligionID(){ return nReligionID; }
    public void setReligionID(int n){ nReligionID = n; }

}
