package factor.labs.indiancalendar.CalendarUtils;

import android.content.Context;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import factor.labs.indiancalendar.CalendarObjects.CalendarEmptyEventListItem;
import factor.labs.indiancalendar.CalendarObjects.CalendarEventDateListItem;
import factor.labs.indiancalendar.CalendarObjects.CalendarEventListItem;
import factor.labs.indiancalendar.CalendarObjects.CalendarEventMonthListItem;
import factor.labs.indiancalendar.utils.database.DatabaseHelper;
import factor.labs.indiancalendar.utils.database.Events;
import factor.labs.indiancalendar.utils.date.DateTime;
import factor.labs.indiancalendar.utils.json.EventCategory;

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

    List<Events> mListOfEventsForMonth = new ArrayList<>();
    List<Object> moListEventViewsToDisplay = new ArrayList<>();

    public long getMonthStartTimeStamp() {
        return mMonthStartTimeStamp;
    }

    public void setMonthStartTimeStamp(long mMonthStartTimeStamp) {
        this.mMonthStartTimeStamp = mMonthStartTimeStamp;
    }

    public long getMonthEndTimeStamp() {
        return mMonthEndTimeStamp;
    }

    public void setMonthEndTimeStamp(long mMonthEndTimeStamp) {
        this.mMonthEndTimeStamp = mMonthEndTimeStamp;
    }

    long mMonthStartTimeStamp = 0;
    long mMonthEndTimeStamp = 0;

    public void setContext(Context mContext) { this.mContext = mContext; }

    Context mContext;

    public CalendarMonthClass(int nMonth, int nYear){
        sTag = "CalendarMonthClass()";
        mnMonth = nMonth;
        mnYear = nYear;
        //Log.d(sTag, "Month : " + mnMonth + " Year : " +mnYear);
        bEventsLoaded = false;
        mContext = null;
    }

    public int getMonth(){ return mnMonth; }
    public int getYear(){ return mnYear; }

    public void prepareCalendarMonthDates(){
        sTag = "CalendarMonthClass.prepareCalendarMonthDates";
        try {
            mnTotalDaysInMonthGrid = 42;

            int nMonth = mnMonth, nYear = mnYear, nPrevYear = nYear;
            int nNextYear = nYear, nPrevMonth = nMonth - 1, nNextMonth = nMonth + 1;

            Logger.d("Current month [" + mnMonth + "] Current year [" +
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

            Logger.d("After : Current month [" + mnMonth + "] Current year [" +
                    mnYear + "] Next Month [" + nNextMonth + "] Next Year [" + nNextYear + "] Previous Month [" +
                    nPrevMonth + "] Previous year [" + nPrevYear + "].");

            int nPrevMonthDays = DateTime.getNumberOfDaysForMonth(nPrevMonth, nPrevYear);
            int nDaysOfCurrentMonth = DateTime.getNumberOfDaysForMonth(nMonth, nYear);
            int nPrevNoMonthDays = DateTime.getDaysInCurrForPrevMonth(nMonth, nYear);

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

                if(iter == 1) {
                    this.mMonthStartTimeStamp = oDateObject.getDateStartTimeStamp();
                }else if(iter == nDaysOfCurrentMonth){
                    this.mMonthEndTimeStamp = oDateObject.getDateEndTimeStamp();
                }
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
            List<Events> listEvents =
                DatabaseHelper.getEventsForGivenTimeFrame(this.mMonthStartTimeStamp, this.mMonthEndTimeStamp);
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

    public List<Events> getEventsForMonth(){
        if(!bEventsLoaded) {
            bEventsLoaded = onPrepareListOfEventsForMonth();

            for (Events oEventDate : mListOfEventsForMonth) {
                CalendarDateClass oDate = getDateObject(oEventDate.getStart_date());
                if (oDate != null) {
                    boolean found = false;
                    for(Events event : oDate.getEventsForDay()){
                        if(event.getGUID().equals(oEventDate.getGUID())) {
                            found = true;
                            break;
                        }
                    }
                    if(!found) {
                        //Log.d(sTag, "Set grid dates for month [" + mnMonth + "] date [" + oEventDate.getDate() + "].");
                        oDate.addEventsForDay(oEventDate);
                        if((oEventDate.getCategory()
                                & EventCategory.Category.HOLIDAY.getValue()) != 0)
                            oDate.setHolidayFlag();

                        if((oEventDate.getCategory()
                                & EventCategory.Category.RELIGIOUS.getValue()) != 0)
                            oDate.setReligiousFlag();
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
            for (Events oEventDate : mListOfEventsForMonth) {
                CalendarDateClass oDate = getDateObject(oEventDate.getStart_date());
                if (oDate != null) {
                    boolean found = false;
                    for(Events event : oDate.getEventsForDay()) {
                        if(event.getGUID().equals(oEventDate.getGUID())) {
                            found = true;
                            break;
                        }
                    }

                    if(!found) {
                        //Log.d(sTag, "Set grid dates for month [" + mnMonth + "] date [" + oEventDate.getDate() + "].");
                        oDate.addEventsForDay(oEventDate);

                        if((oEventDate.getCategory()
                                & EventCategory.Category.HOLIDAY.getValue()) != 0)
                            oDate.setHolidayFlag();

                        if((oEventDate.getCategory()
                                & EventCategory.Category.RELIGIOUS.getValue()) != 0)
                            oDate.setReligiousFlag();
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
                List<Events> listEvents = oDate.getEventsForDay();

                int nEventsFound = 0;
                if (listEvents != null){
                    oDate.getEventsForDayInDisplay().clear();
                    for (Events oEvent : listEvents) {

                        /*if(start == tempList.size()) {
                            tempList.add(new
                                    CalendarEventAdListItem(oDate.getDate(),
                                    oDate.getMonth(), oDate.getYear(), tempList.size()));
                            start = (start + nextAdd);
                        }*/

                        CalendarEventListItem oE = new CalendarEventListItem(oEvent, tempList.size());
                        if(nShowPref == 1 && (oEvent.getCategory()
                                & EventCategory.Category.RELIGIOUS.getValue()) != 0){
                            tempList.add(oE);
                            oDate.getEventsForDayInDisplay().add(oEvent);
                        } // religious
                        else if(nShowPref == 2 &&(oEvent.getCategory()
                                & EventCategory.Category.HOLIDAY.getValue()) != 0){
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

    public CalendarDateClass getDateObject(long lStartTime)
    {
        sTag = "CalendarMonthClass.getDateObject";
        try
        {
            for(CalendarDateClass oDate : mListOfDatesInMonthGrid)
                if(lStartTime >= oDate.getDateStartTimeStamp() && lStartTime <= oDate.getDateEndTimeStamp())
                {
                    Logger.d("Date ["+ oDate.getDate() +"] ["+ oDate.getMonth() +"] macthed object returned.");
                    return oDate;
                }
        }
        catch(Exception exec)
        {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
        return null;
    }
}
