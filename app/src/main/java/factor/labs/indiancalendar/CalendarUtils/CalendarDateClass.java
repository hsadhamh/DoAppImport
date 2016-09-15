package factor.labs.indiancalendar.CalendarUtils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import factor.labs.indiancalendar.CalendarDbHelper.CalendarEventMaster;

/**
 * Created by hassanhussain on 7/20/2015.
 */
public class CalendarDateClass {
    String stag;

    int mnDate;
    int mnMonth, mnYear;
    String msDate;

    int mnWeekName;
    int mnWeekNumber;
    int mnListOffset = 0;

    public boolean mbEventsFound = false;

    private boolean mbHasHolidayEvent = false;
    private boolean mbHasReligiousEvent = false;

    private boolean mbSelectedDate = false;
    List<CalendarEventMaster> mListOfEvents = new ArrayList<CalendarEventMaster>();
    List<CalendarEventMaster> mListOfEventsInDisplay = new ArrayList<CalendarEventMaster>();

    int mnIsPrevNextMonthDate = 0; // 0 -> current month; 1-> nextMonth; -1 -> PrevMonth

    public CalendarDateClass(int nDate, int nMonth, int nYear, int nPrevNextMonthDate){
        mnDate = nDate;
        mnMonth = nMonth;
        mnYear = nYear;
        msDate = ""+mnDate;
        mnWeekNumber = labsCalendarUtils.getWeekIndexForDate(mnDate, mnMonth, mnYear);
        mnIsPrevNextMonthDate = nPrevNextMonthDate;
    }

    void setSelected(boolean mb) { mbSelectedDate = mb; }

    public int getDate()
    {
        return mnDate;
    }
    public int getMonth()
    {
        return mnMonth;
    }
    public int getYear()
    {
        return mnYear;
    }

    public void setListOffset(int n) { mnListOffset = n; }
    public int getListOffset() { return mnListOffset; }

    public void setHolidayFlag() { mbHasHolidayEvent = true; }
    public void setReligiousFlag() { mbHasReligiousEvent = true; }

    public boolean hasHolidayFlagSet() { return mbHasHolidayEvent; }
    public boolean hasReligiousFlagSet() { return mbHasReligiousEvent ; }

    public String getDateString()
    {
        return msDate;
    }

    public String getFullDateString()
    {
        return (mnDate + "-" + mnMonth + "-" + mnYear);
    }

    public int getWeekNameNumber()
    {
        return mnWeekNumber;
    }
    public boolean isCurrentMonthDate() {
        return mnIsPrevNextMonthDate == CalendarConstants.CALENDAR_CURRENT_MONTH_DATE;
    }

    public boolean isDateSelected(){ return mbSelectedDate; }

    public boolean isPreviousMonthDate()
    {
        return mnIsPrevNextMonthDate == CalendarConstants.CALENDAR_PREVIOUS_MONTH_DATE;
    }

    public boolean isNextMonthDate()
    {
        return mnIsPrevNextMonthDate == CalendarConstants.CALENDAR_NEXT_MONTH_DATE;
    }

    public void setEventsForDay(List<CalendarEventMaster> oLisEvents)
    {
        stag = "CalendarDateClass.setEventsForDay";

        try {
            Log.d(stag, "Copying events starts !!");
            if (oLisEvents != null) {
                if (mListOfEvents.size() > 0) mListOfEvents.clear();

                if (oLisEvents.size() == 0) return;

                mbEventsFound = true;

                for (CalendarEventMaster oClass : oLisEvents)
                    mListOfEvents.add(oClass);
                Log.d(stag, "Copying events starts : done.");
            }
        }
        catch(Exception exec)
        {
            Log.e(stag, "Exception ["+ exec.getMessage() +"].");
        }
    }

    public void addEventsForDay(CalendarEventMaster oEvent)
    {
        mbEventsFound = true;
        mListOfEvents.add(oEvent);
    }

    public List<CalendarEventMaster> getEventsForDay()
    {
        return mListOfEvents;
    }

    public List<CalendarEventMaster> getEventsForDayInDisplay()
    {
        return mListOfEventsInDisplay;
    }

}
