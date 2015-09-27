package factor.labs.indiancalendar.CalendarUI.CalendarViews;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import factor.labs.indiancalendar.CalendarAdapters.CalendarScheduleEventsAdapter;
import factor.labs.indiancalendar.CalendarAdapters.CalendarScheduleReligiousAdapter;
import factor.labs.indiancalendar.CalendarDbHelper.CalendarEventMaster;
import factor.labs.indiancalendar.CalendarEventHandlers.CalendarEventDateClass;
import factor.labs.indiancalendar.CalendarInterfaces.CalendarGetContextInterface;
import factor.labs.indiancalendar.CalendarInterfaces.CalendarLoadMoreListener;
import factor.labs.indiancalendar.CalendarUI.CalendarCircleLoading.CircleProgressBar;
import factor.labs.indiancalendar.CalendarUtils.CalendarConstants;
import factor.labs.indiancalendar.CalendarUtils.CalendarListObjectClass;
import factor.labs.indiancalendar.CalendarUtils.CalendarMonthYearClass;
import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;
import factor.labs.indiancalendar.R;

/**
 * Created by hassanhussain on 9/20/2015.
 */
public class CalendarReligiousScheduleEvent extends Fragment implements CalendarLoadMoreListener {
    String sTag;
    View moScheduleViewHolder = null;
    ListView moListSchedule;
    CircleProgressBar moProgressView = null;

    int mnMonth;
    int mnYear;

    Context moContext;
    LayoutInflater mInflater;

    List<CalendarListObjectClass> mListOfSchedule = new ArrayList<CalendarListObjectClass>();
    CalendarScheduleReligiousAdapter moAdapter;

    CalendarGetContextInterface oActivityContact;

    int mnCurrentMonthIndex = 0;
    int mnFilter = 0;

    public static CalendarReligiousScheduleEvent init(int nMonth, int nYear, Context oCon, int nFilter)
    {
        CalendarReligiousScheduleEvent oFragment = new CalendarReligiousScheduleEvent();
        oFragment.setMonth(nMonth);
        oFragment.setYear(nYear);
        oFragment.setContext(oCon);
        oFragment.setFilter(nFilter);

        return oFragment;
    }

    @Override
    public void onAttach(Context activity){
        super.onAttach(activity);
        moContext = getActivity().getApplicationContext();
    }

    public void setFilter(int n) { mnFilter = n; }

    public void setContext(Context c)
    {
        moContext = c;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sTag= "CalendarScheduleViewFragment.onCreateView";
        try {
            super.onCreate(savedInstanceState);
            int value = (getArguments() != null ? getArguments().getInt("key") : 1);

            moScheduleViewHolder = inflater.inflate(R.layout.layout_calendar_schedule_religious, container, false);
            mInflater = (LayoutInflater) moContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            moListSchedule = (ListView) moScheduleViewHolder.findViewById(R.id.calendar_schedule_religious);
            moProgressView = (CircleProgressBar) moScheduleViewHolder.findViewById(R.id.calendar_schedule_progress);

            new LoadMonthEvents().execute();
            return moScheduleViewHolder;
        }
        catch(Exception exec)
        {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
        return null;
    }

    public void setMonth(int nMonth){ mnMonth = nMonth;}
    public void setYear(int nYear){ mnYear = nYear;}

    public void onPrepareEventsForView() {
        sTag = "CalendarScheduleViewFragment.onPrepareEventsForView";
        try {
            mListOfSchedule.clear();

            CalendarMonthYearClass oMonthYear = new CalendarMonthYearClass();
            oMonthYear.setMonth(mnMonth);
            oMonthYear.setYear(mnYear);

            Log.d(sTag, "month : " + oMonthYear.getMonth() + " year : " + oMonthYear.getYear());

            onAddLoadMoreEvents(CalendarConstants.CALENDAR_SCHEDULE_LOAD_UP);
            //  get all events for 6 months before current
            onGetBeforeMonthEvents(oMonthYear, CalendarConstants.CALENDAR_SCHEDULE_MONTH_EVENTS_LOAD_THRESHOLD);
            mnCurrentMonthIndex = mListOfSchedule.size();
            //  get all events for current month
            //  get events for current month.
            onProcessMonthEvents(oMonthYear);

            //  get all events for 6 months after current
            onGetAfterMonthEvents(oMonthYear, CalendarConstants.CALENDAR_SCHEDULE_MONTH_EVENTS_LOAD_THRESHOLD);

            //  set footer for listview
            onAddLoadMoreEvents(CalendarConstants.CALENDAR_SCHEDULE_LOAD_DOWN);
        }
        catch(Exception exec)
        {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
    }

    public void onGetBeforeMonthEvents(CalendarMonthYearClass oMonthYear, int nOffset)
    {
        sTag = "CalendarScheduleViewFragment.onGetBeforeMonthEvents";
        try
        {
            for (int nIter = nOffset; nIter > 0; nIter--) {
                CalendarMonthYearClass oSubtractMonth = labsCalendarUtils.subtractMonthYear(oMonthYear, nIter);
                Log.d(sTag, "month : " + oSubtractMonth.getMonth() + " year : " + oSubtractMonth.getYear());

                //  get events for month.
                onProcessMonthEvents(oSubtractMonth);
            }
        }
        catch(Exception exec)
        {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
    }

    public void onGetAfterMonthEvents(CalendarMonthYearClass oMonthYear, int nOffset)
    {
        sTag = "CalendarScheduleViewFragment.onGetAfterMonthEvents";
        try
        {
            for (int nIter = 1; nIter <= nOffset; nIter++) {
                CalendarMonthYearClass oAddMonth = labsCalendarUtils.addMonthYear(oMonthYear, nIter);
                Log.d(sTag, "month : " + oAddMonth.getMonth() + " year : " + oAddMonth.getYear());

                //  get events for month.
                onProcessMonthEvents(oAddMonth);
            }
        }
        catch(Exception exec)
        {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
    }

    public void onAddLoadMoreEvents(int nDirection)
    {
        sTag = "CalendarScheduleViewFragment.onAddLoadMoreEvents";
        try
        {
            CalendarListObjectClass oLoadEvent = new CalendarListObjectClass();
            oLoadEvent.setType(nDirection);
            oLoadEvent.setGenericObject(null);
            Log.d(sTag, "Type : " + nDirection);
            mListOfSchedule.add(oLoadEvent);
        }
        catch(Exception exec)
        {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
    }

    public void onProcessMonthEvents(CalendarMonthYearClass oMonthYear)
    {
        sTag = "CalendarScheduleViewFragment.onProcessMonthEvents";

        try {
            List<CalendarEventMaster> oListOfEvents = labsCalendarUtils.getCalendarDBHandler().getReligiousEventsForMonth(
                    oMonthYear.getMonth(), oMonthYear.getYear(), mnFilter);
            if(oListOfEvents != null)
                Log.d(sTag, "List of events : " + oListOfEvents.size());

            CalendarListObjectClass oBeforeObject = new CalendarListObjectClass();
            oBeforeObject.setType(CalendarConstants.CALENDAR_SCHEDULE_MONTH_TYPE);
            oBeforeObject.setGenericObject(oMonthYear);
            Log.d(sTag, "Type : " + 256);

            mListOfSchedule.add(oBeforeObject);


            //  Add date list item
            if (oListOfEvents == null || oListOfEvents.isEmpty()) {
                //  Create month list item
                CalendarListObjectClass oEmptyObject = new CalendarListObjectClass();
                oEmptyObject.setType(CalendarConstants.CALENDAR_SCHEDULE_EMPTY_EVENT);
                oEmptyObject.setGenericObject(null);
                Log.d(sTag, "Type : " + 1024);

                mListOfSchedule.add(oEmptyObject);
            } else {
                List<CalendarEventDateClass> oListDateEvents = new ArrayList<>();

                for(CalendarEventMaster oEvent : oListOfEvents)
                {
                    CalendarEventDateClass oDate = null;
                    for(CalendarEventDateClass oDateItem : oListDateEvents)
                    {
                        if(oDateItem.getDate() == oEvent.getDate())
                        {
                            oDate = oDateItem;
                            break;
                        }
                    }
                    if(oDate == null)
                    {
                        oDate = new CalendarEventDateClass();
                        oDate.setDate(oEvent.getDate());
                        oDate.setMonth(oEvent.getMonth());
                        oDate.setYear(oEvent.getYear());
                        oListDateEvents.add(oDate);
                    }
                    oDate.addEvent(oEvent);
                }

                Collections.sort(oListDateEvents);

                for (CalendarEventDateClass oDate : oListDateEvents) {
                    CalendarListObjectClass oDateObject = new CalendarListObjectClass();
                    oDateObject.setType(CalendarConstants.CALENDAR_SCHEDULE_DAY_TYPE);
                    Log.d(sTag, "Type : " + 512);
                    //Log.d(sTag, "Events sum : " + oDate.getEvents().size());
                    oDateObject.setGenericObject(oDate);
                    mListOfSchedule.add(oDateObject);
                }
            }
        }
        catch(Exception exec)
        {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
    }

    @Override
    public void onLoadMoreEvents(int nDirection) {
        sTag = "CalendarScheduleViewFragment.onLoadMoreEvents";
        try{
            List<CalendarListObjectClass> oldValues = new ArrayList<CalendarListObjectClass>();
            oldValues.addAll(mListOfSchedule);

            int nLastIndex = 1;
            if(nDirection == CalendarConstants.CALENDAR_SCHEDULE_LOAD_UP) {
                Log.d(sTag, "Load Events at top.");
                oldValues.remove(0);
                CalendarListObjectClass obj = oldValues.get(0);
                CalendarMonthYearClass oLastObject = (CalendarMonthYearClass) obj.getGenericObject();

                mListOfSchedule.clear();
                onAddLoadMoreEvents(CalendarConstants.CALENDAR_SCHEDULE_LOAD_UP);
                onGetBeforeMonthEvents(oLastObject, CalendarConstants.CALENDAR_SCHEDULE_MONTH_EVENTS_LOAD_THRESHOLD);

                nLastIndex = mListOfSchedule.size() - 1;
                Log.d(sTag, "Index ["+ nLastIndex +"].");
                mListOfSchedule.addAll(oldValues);
            }
            else if(nDirection == CalendarConstants.CALENDAR_SCHEDULE_LOAD_DOWN)
            {
                Log.d(sTag, "Load Events at bottom.");
                int nLastindex = oldValues.size()-1;
                oldValues.remove(nLastindex);
                CalendarListObjectClass obj = oldValues.get(nLastindex - 1);
                int nMonth, nYear;

                Log.d(sTag, "Index ["+ nLastIndex +"].");

                if(obj.getGenericObject() == null)
                {
                    obj = oldValues.get(nLastindex -2);
                    nMonth = ((CalendarMonthYearClass) obj.getGenericObject()).getMonth();
                    nYear = ((CalendarMonthYearClass) obj.getGenericObject()).getYear();
                }
                else
                {
                    nMonth = ((CalendarEventDateClass) obj.getGenericObject()).getMonth();
                    nYear = ((CalendarEventDateClass) obj.getGenericObject()).getYear();
                }

                CalendarMonthYearClass oLastObject= new CalendarMonthYearClass(nMonth, nYear);

                mListOfSchedule.clear();
                mListOfSchedule.addAll(oldValues);
                nLastIndex = mListOfSchedule.size();

                onGetAfterMonthEvents(oLastObject, CalendarConstants.CALENDAR_SCHEDULE_MONTH_EVENTS_LOAD_THRESHOLD);
                onAddLoadMoreEvents(CalendarConstants.CALENDAR_SCHEDULE_LOAD_DOWN);
            }

            moAdapter.setObjectListItems(mListOfSchedule);
            moAdapter.notifyDataSetChanged();
            moListSchedule.setSelection(nLastIndex);
            oldValues.clear();
            Log.d(sTag, "Events Loaded ...!!");
        }
        catch(Exception exec)
        {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
    }

    public void SetContentsToUI()
    {
        moAdapter = new CalendarScheduleReligiousAdapter(mListOfSchedule, moContext, CalendarReligiousScheduleEvent.this);
        moAdapter.notifyDataSetChanged();
        moListSchedule.setAdapter(moAdapter);
    }

    private class LoadMonthEvents extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            moProgressView.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(sTag, "prepare views for list.");
            onPrepareEventsForView();
            Log.d(sTag, "Set Events for list");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            SetContentsToUI();

            moProgressView.setVisibility(View.GONE);
            moListSchedule.setVisibility(View.VISIBLE);

            Log.d(sTag, "Set to current month index [" + mnCurrentMonthIndex + "].");
            moListSchedule.setSelection(mnCurrentMonthIndex);
            moListSchedule.setSmoothScrollbarEnabled(true);
        }
    }
}