package factor.labs.indiancalendar.CalendarUtils;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import factor.labs.indiancalendar.CalendarDbHelper.CalendarEventMaster;
import factor.labs.indiancalendar.CalendarObjects.CalendarEmptyEventListItem;
import factor.labs.indiancalendar.CalendarObjects.CalendarEventDateListItem;
import factor.labs.indiancalendar.CalendarObjects.CalendarEventListItem;
import factor.labs.indiancalendar.CalendarObjects.CalendarEventMonthListItem;

/**
 * Created by hassanhussain on 7/20/2015.
 */
public class CalendarMonthClass {
    int mnMonth, mnYear;
    int mnTotalDaysInMonthGrid;
    int mnOffsetInList = 0;
    int mnSizeInList = 0;

    boolean bEventsLoaded = false;

    String sTag;

    List<CalendarDateClass> mListOfDatesInMonthGrid = new Vector<CalendarDateClass>();

    List<CalendarEventMaster> mListOfEventsForMonth = new ArrayList<>();
    List<Object> moListEventViewsToDisplay = new ArrayList<>();

    public void setmContext(Context mContext) { this.mContext = mContext; }

    Context mContext;

    public CalendarMonthClass(int nMonth, int nYear){
        sTag = "CalendarMonthClass()";
        mnMonth = nMonth;
        mnYear = nYear;
        Log.d(sTag, "Month : " + mnMonth + " Year : " +mnYear);
        bEventsLoaded = false;
        mContext = null;
    }

    public int getMonth(){ return mnMonth; }
    public int getYear(){ return mnYear; }

    public boolean hasLoadEventDone() { return bEventsLoaded; }

    public int getListOffset(){ return mnOffsetInList; }
    public void setListOffset(int n){ mnOffsetInList = n; }

    public int getListSize(){ return mnSizeInList; }
    public void setListSize(int n){ mnSizeInList = n; }

    public void prepareCalendarMonthDates(){
        sTag = "CalendarMonthClass.prepareCalendarMonthDates";
        try {
            mnTotalDaysInMonthGrid = 42;

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

            Log.d(sTag, "total month days [" + mnTotalDaysInMonthGrid + "].");
            // Previous month days.
            for (int iter = nPrevMonthDays - nPrevNoMonthDays + 1; iter <= nPrevMonthDays; iter++) {
                CalendarDateClass oDateObject = new CalendarDateClass(iter, nPrevMonth, nPrevYear, CalendarConstants.CALENDAR_PREVIOUS_MONTH_DATE);
                mListOfDatesInMonthGrid.add(oDateObject);
            }
            //  Current month days
            for (int iter = 1; iter <= nDaysOfCurrentMonth; iter++) {
                CalendarDateClass oDateObject = new CalendarDateClass(iter, mnMonth, mnYear, CalendarConstants.CALENDAR_CURRENT_MONTH_DATE);
                if(iter == labsCalendarUtils.getTodaysDate() &&
                        mnMonth == labsCalendarUtils.getCurrentMonth() &&
                        mnYear == labsCalendarUtils.getCurrentYear())
                    oDateObject.setSelected(true);
                mListOfDatesInMonthGrid.add(oDateObject);
            }
            // next month days.
            for (int iter = 1; iter <= (42 - (nPrevNoMonthDays + nDaysOfCurrentMonth)); iter++) {
                CalendarDateClass oDateObject = new CalendarDateClass(iter, nNextMonth, nNextYear, CalendarConstants.CALENDAR_NEXT_MONTH_DATE);
                mListOfDatesInMonthGrid.add(oDateObject);
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
            List<CalendarEventMaster> listEvents = null;
            if(mContext == null)
                listEvents = labsCalendarUtils.getCalendarDBHandler().getHolidayReligiousEventsForMonth(mnMonth, mnYear);
            else
                listEvents = labsCalendarUtils.getCalendarDBHandler(mContext).getHolidayReligiousEventsForMonth(mnMonth, mnYear);
            if(listEvents == null || listEvents.size() == 0) {
                Log.w(sTag, "Total number of events returned for month [" + mnMonth + "] Year [" + mnYear + "] is zero.");
            }
            else {
                mListOfEventsForMonth.clear();
                mListOfEventsForMonth.addAll(listEvents);
            }
        }catch(Exception exec) {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
        return true;
    }

    public List<CalendarEventMaster> getEventsForMonth(){
        if(!bEventsLoaded) {
            bEventsLoaded = onPrepareListOfEventsForMonth();

            for (CalendarEventMaster oEventDate : mListOfEventsForMonth) {
                CalendarDateClass oDate = getDateObject(oEventDate.getDate(), mnMonth);
                if (oDate != null) {
                    boolean found = false;
                    for(CalendarEventMaster event : oDate.getEventsForDay())
                    {
                        if(event.getEventID() == oEventDate.getEventID()) {
                            found = true;
                            break;
                        }
                    }
                    if(!found) {
                        Log.d(sTag, "Set grid dates for month [" + mnMonth + "] date [" + oEventDate.getDate() + "].");
                        oDate.addEventsForDay(oEventDate);
                        if(oEventDate.isHolidayEvent()) oDate.setHolidayFlag();
                        if(oEventDate.isReligionEvent()) oDate.setReligiousFlag();
                    }
                }
            }
        }
        return mListOfEventsForMonth;
    }

    public CalendarEventMonthListItem getHeaderItem(){
        return new CalendarEventMonthListItem(getMonth(), getYear());
    }

    public List<Object> getEventsAndViewsForMonth(){
        int nShowPref = labsCalendarUtils.getShowPreference();
        if(!bEventsLoaded) {
            bEventsLoaded = onPrepareListOfEventsForMonth();
            //  Load views for ListView.
            for (CalendarEventMaster oEventDate : mListOfEventsForMonth) {
                CalendarDateClass oDate = getDateObject(oEventDate.getDate(), mnMonth);
                if (oDate != null) {
                    boolean found = false;
                    for(CalendarEventMaster event : oDate.getEventsForDay()) {
                        if(event.getEventID() == oEventDate.getEventID()) {
                            found = true;
                            break;
                        }
                    }

                    if(!found) {
                        Log.d(sTag, "Set grid dates for month [" + mnMonth + "] date [" + oEventDate.getDate() + "].");
                        oDate.addEventsForDay(oEventDate);
                        if(oEventDate.isHolidayEvent()) oDate.setHolidayFlag();
                        if(oEventDate.isReligionEvent()) oDate.setReligiousFlag();
                    }
                }
            }
        }

        moListEventViewsToDisplay.clear();

        List<Object> tempList = new ArrayList<>();
        //int start = 3, nextAdd = 21;
        //  add views now.
        // -- first and fore most MONTH
        for(CalendarDateClass oDate : mListOfDatesInMonthGrid){

            if(oDate.isCurrentMonthDate()) {
                /*if(start == tempList.size()) {
                    tempList.add(new
                            CalendarEventAdListItem(oDate.getDate(),
                            oDate.getMonth(), oDate.getYear(), tempList.size()));
                    start = (start + nextAdd);
                }*/

                tempList.add(new CalendarEventDateListItem(oDate.getDate(), oDate.getMonth(), oDate.getYear(), tempList.size()));
                oDate.setListOffset(tempList.size());
                List<CalendarEventMaster> listEvents = oDate.getEventsForDay();

                int nEventsFound = 0;
                if (listEvents != null){
                    oDate.getEventsForDayInDisplay().clear();
                    for (CalendarEventMaster oEvent : listEvents) {

                        /*if(start == tempList.size()) {
                            tempList.add(new
                                    CalendarEventAdListItem(oDate.getDate(),
                                    oDate.getMonth(), oDate.getYear(), tempList.size()));
                            start = (start + nextAdd);
                        }*/

                        CalendarEventListItem oE = new CalendarEventListItem(oEvent, tempList.size());
                        if(nShowPref == 1 && oEvent.isReligionEvent()){
                            tempList.add(oE);
                            oDate.getEventsForDayInDisplay().add(oEvent);
                        } // religious
                        else if(nShowPref == 2 && oEvent.isHolidayEvent()){
                            tempList.add(oE);
                            oDate.getEventsForDayInDisplay().add(oEvent);
                        } // holidays
                        else if(nShowPref == 0){
                            tempList.add(oE);
                            oDate.getEventsForDayInDisplay().add(oEvent);
                        } // all events
                    }
                }

                if(oDate.getEventsForDayInDisplay().size() == 0){

                   /* if(start == tempList.size()) {
                        tempList.add(new
                                CalendarEventAdListItem(oDate.getDate(),
                                oDate.getMonth(), oDate.getYear(), tempList.size()));
                        start = (start + nextAdd);
                    }*/

                    CalendarEmptyEventListItem oEmp =
                            new CalendarEmptyEventListItem(oDate.getDate(), oDate.getMonth(),
                                    oDate.getYear(), tempList.size());
                    tempList.add(oEmp);
                }
            }
        }
        mnSizeInList = tempList.size();
        if(mnSizeInList > 0)
            moListEventViewsToDisplay.addAll(tempList);
        return moListEventViewsToDisplay;
    }

    public List<CalendarDateClass> getDateForGrid(){
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
