package factor.labs.indiancalendar.CalendarUtils;

import android.util.Log;

import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import factor.labs.indiancalendar.utils.Constants;
import factor.labs.indiancalendar.utils.database.Events;
import factor.labs.indiancalendar.utils.date.DateTime;

/**
 * Created by hassanhussain on 7/20/2015.
 */
public class CalendarDateClass {
    String stag;

    int mnDate;
    int mnMonth, mnYear;
    String msDate;


    long mlDateStartTimeStamp = 0;
    long mlDateEndTimeStamp = 0;

    int mnWeekName;
    int mnWeekNumber;
    int mnListOffset = 0;

    public boolean mbEventsFound = false;

    private boolean mbHasHolidayEvent = false;
    private boolean mbHasReligiousEvent = false;

    private boolean mbSelectedDate = false;
    List<Events> mListOfEvents = new ArrayList<Events>();
    List<Events> mListOfEventsInDisplay = new ArrayList<Events>();

    int mnIsPrevNextMonthDate = 0; // 0 -> current month; 1-> nextMonth; -1 -> PrevMonth

    public CalendarDateClass(int nDate, int nMonth, int nYear, int nPrevNextMonthDate){
        mnDate = nDate;
        mnMonth=nMonth;
        mnYear = nYear;
        msDate = ""+mnDate;
        mnIsPrevNextMonthDate = nPrevNextMonthDate;
        // get start & end time information for current month.
        if(mnIsPrevNextMonthDate == Constants.CALENDAR_CURRENT_MONTH_DATE) {
            try {
                mlDateEndTimeStamp =
                        DateTime.getTimeStampGivenDateTime(DateTime.prepareDateTimeString(23, 59, 59, nDate, nMonth, nYear));
                mlDateStartTimeStamp =
                        DateTime.getTimeStampGivenDateTime(DateTime.prepareDateTimeString(0, 0, 0, nDate, nMonth, nYear));
            } catch (ParseException e) {
                Logger.e(e, "Exception while getting time stamp information.");
            }
        }
    }

    public long getDateStartTimeStamp() {
        return mlDateStartTimeStamp;
    }

    public void setDateStartTimeStamp(long mlDateStartTimeStamp) {
        this.mlDateStartTimeStamp = mlDateStartTimeStamp;
    }

    public long getDateEndTimeStamp() {
        return mlDateEndTimeStamp;
    }

    public void setDateEndTimeStamp(long mlDateEndTimeStamp) {
        this.mlDateEndTimeStamp = mlDateEndTimeStamp;
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
        return mnWeekName;
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

    public void setEventsForDay(List<Events> oLisEvents){
        stag = "CalendarDateClass.setEventsForDay";

        try {
            //Log.d(stag, "Copying events starts !!");
            if (oLisEvents != null) {
                if (mListOfEvents.size() > 0) mListOfEvents.clear();

                if (oLisEvents.size() == 0) return;

                mbEventsFound = true;

                for (Events oClass : oLisEvents)
                    mListOfEvents.add(oClass);
                //Log.d(stag, "Copying events starts : done.");
            }
        }
        catch(Exception exec)
        {
            Log.e(stag, "Exception ["+ exec.getMessage() +"].");
        }
    }

    public void addEventsForDay(Events oEvent) {
        mbEventsFound = true;
        mListOfEvents.add(oEvent);
    }

    public List<Events> getEventsForDay() { return mListOfEvents; }
    public List<Events> getEventsForDayInDisplay() { return mListOfEventsInDisplay; }

}
