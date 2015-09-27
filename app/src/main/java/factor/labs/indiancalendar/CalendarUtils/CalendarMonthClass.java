package factor.labs.indiancalendar.CalendarUtils;

import android.content.res.AssetManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import factor.labs.indiancalendar.CalendarDbHelper.CalendarEventMaster;
import factor.labs.indiancalendar.CalendarEventHandlers.CalendarEventDateClass;
import factor.labs.indiancalendar.CalendarJSONLib.CalendarJsonParser;

/**
 * Created by hassanhussain on 7/20/2015.
 */
public class CalendarMonthClass {
    int mnMonth, mnYear;
    int mnTotalDaysInMonthGrid;

    boolean bEventsLoaded = false;

    String sTag;

    List<CalendarDateClass> mListOfDatesInMonthGrid = new Vector<CalendarDateClass>();

    List<CalendarEventMaster> mListOfEventsForMonth = null;

    public CalendarMonthClass(int nMonth, int nYear){
        sTag = "CalendarMonthClass()";
        mnMonth = nMonth;
        mnYear = nYear;
        Log.d(sTag, "Month : " + mnMonth + " Year : " +mnYear);
        bEventsLoaded = false;
    }

    public int getMonth(){ return mnMonth; }
    public int getYear(){ return mnYear; }

    public void prepareCalendarMonthDates(){
        sTag = "CalendarMonthClass.prepareCalendarMonthDates";
        try {
            int nMonth = mnMonth, nYear = mnYear, nPrevYear = nYear;
            int nNextYear = nYear, nPrevMonth = nMonth - 1, nNextMonth = nMonth + 1;

            Log.d(sTag, "Current month [" + mnMonth + "] Current year [" +
                    mnYear + "] Next Month [" + nNextMonth + "] Next Year [" + nNextYear + "] Previous Month [" +
                    nPrevMonth + "] Previous year [" + nPrevYear + "].");

            if (nMonth == 1) {
                nPrevMonth = 12;
                nPrevYear--;
            }
            if (nMonth == 12) {
                nNextMonth = 1;
                nNextYear++;
            }

            Log.d(sTag, "After : Current month [" + mnMonth + "] Current year [" +
                    mnYear + "] Next Month [" + nNextMonth + "] Next Year [" + nNextYear + "] Previous Month [" +
                    nPrevMonth + "] Previous year [" + nPrevYear + "].");

            int nPrevMonthDays = labsCalendarUtils.getNumberOfDatesForMonth(nPrevMonth, nPrevYear);
            int nDaysOfCurrentMonth = labsCalendarUtils.getNumberOfDatesForMonth(nMonth, nYear);
            int nNextMonthDays = labsCalendarUtils.getNumberOfDatesForMonth(nNextMonth, nNextYear);

            Log.d(sTag, "Number of days for previous month [" + nPrevMonthDays + "].");
            Log.d(sTag, "Number of days for current month [" + nDaysOfCurrentMonth + "].");
            Log.d(sTag, "Number of days for next month [" + nNextMonthDays + "].");

            GregorianCalendar oGregCalendar = new GregorianCalendar(nYear, nMonth - 1, 1);

            int nPrevNoMonthDays = oGregCalendar.get(Calendar.DAY_OF_WEEK) - 1;
            Log.d(sTag, "Previous month days [" + nPrevNoMonthDays + "].");
            nNextMonthDays = ((nPrevNoMonthDays + nDaysOfCurrentMonth) % 7);
            Log.d(sTag, "Next month days [" + nNextMonthDays + "].");
            if (nNextMonthDays > 0)
                mnTotalDaysInMonthGrid = nPrevNoMonthDays + nDaysOfCurrentMonth + (7 - nNextMonthDays);
            else
                mnTotalDaysInMonthGrid = nPrevNoMonthDays + nDaysOfCurrentMonth;

            Log.d(sTag, "total month days [" + mnTotalDaysInMonthGrid + "].");
            // Previous month days.
            for (int iter = nPrevMonthDays - nPrevNoMonthDays + 1; iter <= nPrevMonthDays; iter++) {
                CalendarDateClass oDateObject = new CalendarDateClass(iter, nPrevMonth, nPrevYear, CalendarConstants.CALENDAR_PREVIOUS_MONTH_DATE);
                mListOfDatesInMonthGrid.add(oDateObject);
            }
            //  Current month days
            for (int iter = 1; iter <= nDaysOfCurrentMonth; iter++) {
                CalendarDateClass oDateObject = new CalendarDateClass(iter, mnMonth, mnYear, CalendarConstants.CALENDAR_CURRENT_MONTH_DATE);
                mListOfDatesInMonthGrid.add(oDateObject);
            }
            // next month days.
            if (nNextMonthDays > 0) {
                for (int iter = 1; iter <= (7 - nNextMonthDays); iter++) {
                    CalendarDateClass oDateObject = new CalendarDateClass(iter, nNextMonth, nNextYear, CalendarConstants.CALENDAR_NEXT_MONTH_DATE);
                    mListOfDatesInMonthGrid.add(oDateObject);
                }
            }
        }
        catch(Exception exec)
        {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
    }

    public boolean onPrepareListOfEventsForMonth()
    {
        sTag = "CalendarMonthClass.onPrepareListOfEventsForMonth";
        try {
            mListOfEventsForMonth = labsCalendarUtils.getCalendarDBHandler().getHolidayReligiousEventsForMonth(mnMonth, mnYear);
            if(mListOfEventsForMonth == null || mListOfEventsForMonth.size() == 0)
                Log.w(sTag, "Total number of events returned for month ["+ mnMonth +"] Year [" + mnYear + "] is zero.");
        }catch(Exception exec) {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
        return true;
    }

    public List<CalendarEventMaster> getEventsForMonth(){
        if(!bEventsLoaded)
            bEventsLoaded = onPrepareListOfEventsForMonth();
        return mListOfEventsForMonth;
    }

    public List<CalendarDateClass> getDateForGrid()
    {
        return mListOfDatesInMonthGrid;
    }

    public CalendarDateClass getDateObject(int date, int nMonth)
    {
        sTag = "CalendarMonthClass.getDateObject";
        try
        {
            for(CalendarDateClass oDate : mListOfDatesInMonthGrid)
            {
                if(oDate.mnDate == date && oDate.getMonth() == nMonth)
                {
                    Log.d(sTag, "Date ["+date+"] ["+ nMonth +"] macthed object returned.");
                    return oDate;
                }
            }
        }
        catch(Exception exec)
        {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
        return null;
    }
}
