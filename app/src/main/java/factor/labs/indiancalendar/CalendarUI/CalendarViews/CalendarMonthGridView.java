package factor.labs.indiancalendar.CalendarUI.CalendarViews;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import factor.labs.indiancalendar.CalendarAdapters.CalendarEventsForDayAdapter;
import factor.labs.indiancalendar.CalendarAdapters.CalendarEventsForMonthAdapter;
import factor.labs.indiancalendar.CalendarAdapters.CalendarMonthGridDateAdapter;
import factor.labs.indiancalendar.CalendarAdapters.CalendarWeekNameDisplayAdapter;
import factor.labs.indiancalendar.CalendarDbHelper.CalendarEventMaster;
import factor.labs.indiancalendar.CalendarInterfaces.CalendarDateClickListenerInterface;
import factor.labs.indiancalendar.CalendarInterfaces.CalendarMonthChangeListener;
import factor.labs.indiancalendar.CalendarUtils.CalendarConstants;
import factor.labs.indiancalendar.CalendarUtils.CalendarDateClass;
import factor.labs.indiancalendar.CalendarEventHandlers.CalendarEventDateClass;
import factor.labs.indiancalendar.CalendarEventHandlers.CalendarEventInfoClass;
import factor.labs.indiancalendar.CalendarUtils.CalendarMonthClass;
import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;
import factor.labs.indiancalendar.R;

/**
 * Created by hassanhussain on 7/19/2015.
 */
public class CalendarMonthGridView extends Fragment implements CalendarDateClickListenerInterface{

    int mnMonth, mnYear;
    int mnSelectedDate = 1;

    String sTag;

    FragmentManager moFragmentManager;
    Context moContext;

    View moMonthGridViewHolder;

    LinearLayout moWeekNameHolder;

    LayoutInflater mInflater;
    TextView moEventTitle, moEmptyEvents;
    ListView moEventList;
    ImageButton moExpandCollapseButton;
    ImageButton moBackMonthListButton;
    GridView moMonthGrid;

    TextView moMonthNameView;
    ImageButton moImageButtonNextMon;
    ImageButton moImageButtonPrevMon;
    GridView moWeekNameGridHolder;


    CalendarMonthClass moMonthDates;
    CalendarMonthChangeListener moCallback;

    List<CalendarEventMaster> mListEventsForMonth = null;
    CalendarMonthGridDateAdapter moGridDateAdapter = null;

    boolean mbCollapsed = true;

    boolean mbEventsLoaded = false;
    boolean mbUIRefreshNeeded = false;

    public static CalendarMonthGridView init(int nLastSelectedDate, int nMonth, int nYear,
                                             FragmentManager oFrgamentManager, Context oContext,
                                             CalendarMonthViewFragment oCallback){

        CalendarMonthGridView oFragment = new CalendarMonthGridView();
        oFragment.setFragmentManager(oFrgamentManager);
        oFragment.setContext(oContext);
        oFragment.setMonth(nMonth);
        oFragment.setYear(nYear);
        oFragment.preapreGridDatesForMonth();
        oFragment.setCallBack(oCallback);

        Bundle oArguments = new Bundle();
        oArguments.putInt("key", 1);

        oFragment.setArguments(oArguments);

        return oFragment;
    }

    public void setCallBack(CalendarMonthViewFragment oCallBack){ moCallback = oCallBack; }

    public void preapreGridDatesForMonth(){
        moMonthDates = new CalendarMonthClass(mnMonth, mnYear);
        moMonthDates.prepareCalendarMonthDates();
    }

    public void setWeekNamesDisplay()
    {
        sTag = "CalendarMonthViewFragment.setWeekNamesDisplay";
        try {
            CalendarWeekNameDisplayAdapter oAdapter = new CalendarWeekNameDisplayAdapter(moContext);
            moWeekNameGridHolder.setAdapter(oAdapter);
            Log.d(sTag, "week names set.");
        }
        catch(Exception exec)
        { Log.e(sTag, "Exception caught : " + exec.getMessage()); }
    }

    public void setMonthName()
    {
        Log.d("setMonthName", "Set month name for [" + mnMonth+ "] year ["+mnYear+"].");
        String sMonthName = labsCalendarUtils.getMonthName(mnMonth);
        moMonthNameView.setText(sMonthName + " " + mnYear);
    }

    public void setMonth(int nMonth){
        mnMonth = nMonth;
    }

    public void setYear(int nYear){
        mnYear = nYear;
    }

    public void setFragmentManager(FragmentManager fm)
    {
        moFragmentManager = fm;
    }

    public void setContext(Context con)
    {
        moContext=con;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sTag = "CalendarMonthGridView.onCreateView";
        try {
            super.onCreate(savedInstanceState);

            mInflater = (LayoutInflater) moContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int key = (getArguments() != null ? getArguments().getInt("key") : 1);

           Log.d(sTag, "Load contents.");

            moMonthGridViewHolder = inflater.inflate(R.layout.layout_calendar_month_grid_view, container, false);

            moWeekNameHolder= (LinearLayout) moMonthGridViewHolder.findViewById(R.id.calendar_month_name_holder);
            moMonthNameView = (TextView) moMonthGridViewHolder.findViewById(R.id.calendar_month_name);

            Log.d(sTag, "Set set week names.");
            moWeekNameGridHolder = (GridView) moMonthGridViewHolder.findViewById(R.id.calendar_week_names);
            setWeekNamesDisplay();

            moImageButtonPrevMon = (ImageButton) moMonthGridViewHolder.findViewById(R.id.calendar_month_previous);
            moImageButtonPrevMon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moCallback.onProcessMonthChage(CalendarConstants.CALENDAR_MONTH_SWIPE_RIGHT);
                }
            });
            moImageButtonNextMon = (ImageButton) moMonthGridViewHolder.findViewById(R.id.calendar_month_next);
            moImageButtonNextMon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moCallback.onProcessMonthChage(CalendarConstants.CALENDAR_MONTH_SWIPE_LEFT);
                }
            });

            Log.d(sTag, "Set grid dates for month ["+ mnMonth +"] ["+ mnYear +"].");
            //  Create Views For each Date.
            moMonthGrid = (GridView) moMonthGridViewHolder.findViewById(R.id.calendar_month_date_grid);
            moGridDateAdapter = new CalendarMonthGridDateAdapter(moMonthDates.getDateForGrid(), moContext, this);
            moGridDateAdapter.notifyDataSetChanged();
            moMonthGrid.setAdapter(moGridDateAdapter);

            moEventTitle = (TextView) moMonthGridViewHolder.findViewById(R.id.calendar_month_event_title);
            moEventList = (ListView) moMonthGridViewHolder.findViewById(R.id.calendar_month_day_events_list);
            moEmptyEvents = (TextView) moMonthGridViewHolder.findViewById(R.id.calendar_month_day_events_empty);
            moExpandCollapseButton = (ImageButton) moMonthGridViewHolder.findViewById(R.id.calendar_expand_collapse_btn);

            moBackMonthListButton = (ImageButton) moMonthGridViewHolder.findViewById(R.id.calendar_back_month_list);

            setMonthName();

            moExpandCollapseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mbCollapsed)
                    {
                        moExpandCollapseButton.setImageDrawable(moContext.getResources().getDrawable(R.drawable.calendar_arrow_collapse));
                        moMonthGrid.setVisibility(View.GONE);
                        moWeekNameHolder.setVisibility(LinearLayout.GONE);
                        onMonthViewGone(!mbCollapsed);
                        mbCollapsed = false;
                    }
                    else
                    {
                        moExpandCollapseButton.setImageDrawable(moContext.getResources().getDrawable(R.drawable.calendar_arrow_expand));
                        onMonthViewGone(!mbCollapsed);
                        moMonthGrid.setVisibility(View.VISIBLE);
                        moWeekNameHolder.setVisibility(LinearLayout.VISIBLE);
                        mbCollapsed = true;
                    }
                }
            });

            moBackMonthListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moBackMonthListButton.setVisibility(View.GONE);
                    moExpandCollapseButton.setVisibility(View.VISIBLE);
                    doSetMonthEvents();
                }
            });

            Log.d(sTag, "Set current date events ["+ mnSelectedDate +"] for month [" + mnMonth + "] [" + mnYear + "].");
            //  set initial date events to list.
            new LoadMonthEvents().execute();

            return moMonthGridViewHolder;
        }
        catch(Exception exec)
        {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
        return null;
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    public int getMonth()
    {
        return mnMonth;
    }

    public int getYear()
    {
        return mnYear;
    }

    void onProcessListOfEventsForView()
    {
        sTag = "CalendarMonthGridView.onProcessListOfEventsForView";
        try {
            Log.d(sTag, "Set grid dates for month [" + mnMonth + "] [" + mnYear + "].");
            if (mListEventsForMonth != null) {
                //  Now we need to copy events to date class.
                for (CalendarEventMaster oEventDate : mListEventsForMonth) {
                    CalendarDateClass oDate = moMonthDates.getDateObject(oEventDate.getDate(), mnMonth);
                    if (oDate != null) {
                        Log.d(sTag, "Set grid dates for month [" + mnMonth + "] date [" + oEventDate.getDate() + "].");
                        oDate.addEventsForDay(oEventDate);

                        if(oEventDate.isHolidayEvent() && !oDate.hasHolidayFlagSet())
                            oDate.setHolidayFlag();
                        if(oEventDate.isReligionEvent() && !oDate.hasReligiousFlagSet())
                            oDate.setReligiousFlag();
                    }
                }
                //  no need to be in memory
                mListEventsForMonth.clear();
            }
            else
                Log.d(sTag, "Null events for month ["+ mnMonth +"]");
            Log.d(sTag, "Events set complete.   ");
        }
        catch(Exception exec)
        {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
    }

    public void doSetMonthEvents()
    {
        sTag = "CalendarMonthGridView.doSetMonthEvents";
        try {
            List<CalendarEventMaster> oListEvents = new ArrayList<CalendarEventMaster>();
            for (CalendarDateClass oDate : moMonthDates.getDateForGrid()) {
                List<CalendarEventMaster> listEve = oDate.getEventsForDay();
                if (listEve == null || listEve.size() == 0)
                    continue;

                for (CalendarEventMaster oEvent : listEve) {
                    oListEvents.add(oEvent);
                }
            }
            Log.d(sTag, "List of events size [" + oListEvents.size() + "].");
            moEventTitle.setText(labsCalendarUtils.getMonthShortName(mnMonth) + " Events");
            if (oListEvents.isEmpty()) {
                moEventList.setVisibility(View.GONE);
                moEmptyEvents.setVisibility(View.VISIBLE);
                return;
            }

            Log.d(sTag, "Set list of events!!");
            moEventList.setVisibility(View.VISIBLE);
            moEmptyEvents.setVisibility(View.GONE);

            CalendarEventsForMonthAdapter oAdapter = new CalendarEventsForMonthAdapter(oListEvents, moContext);
            oAdapter.notifyDataSetChanged();
            moEventList.setAdapter(oAdapter);
        }
        catch(Exception exec)
        {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
    }

    public void updateUIwithEvents()
    {
        moGridDateAdapter.setGridDateValues(moMonthDates.getDateForGrid());
        moGridDateAdapter.notifyDataSetChanged();
        doSetMonthEvents();
    }

    @Override
    public void doProcessClickEventForDate(CalendarDateClass oDate)
    {
        sTag = "CalendarMonthGridView.doProcessClickEventForDate";
        try {
            mnSelectedDate = oDate.getDate();

            if(oDate.isNextMonthDate())
            {
                Log.d(sTag, "Selected event date : " + mnSelectedDate + ". Next Month date!");
                moCallback.onProcessMonthChage(CalendarConstants.CALENDAR_MONTH_SWIPE_LEFT);
                return;
            }
            else if(oDate.isPreviousMonthDate())
            {
                Log.d(sTag, "Selected event date : " + mnSelectedDate + ". Previous Month date!");
                moCallback.onProcessMonthChage(CalendarConstants.CALENDAR_MONTH_SWIPE_RIGHT);
                return;
            }

            List<CalendarEventMaster> oList = oDate.getEventsForDay();

            Log.d(sTag, "Selected event date : " + mnSelectedDate);

            moExpandCollapseButton.setVisibility(View.GONE);
            moBackMonthListButton.setVisibility(View.VISIBLE);

            if (oList != null && oList.size() != 0) {
                moEventTitle.setText(oDate.getFullDateString() + " events");
                moEventList.setVisibility(View.VISIBLE);
                moEmptyEvents.setVisibility(View.GONE);

                Log.d(sTag, "Set events for selected date : " + mnSelectedDate);
                CalendarEventsForDayAdapter oAdapterListView = new CalendarEventsForDayAdapter(oDate.getEventsForDay(), moContext);
                oAdapterListView.notifyDataSetChanged();
                moEventList.setAdapter(oAdapterListView);
            } else {
                moEventTitle.setText(oDate.getFullDateString() + " events");
                moEventList.setVisibility(View.GONE);
                moEmptyEvents.setVisibility(View.VISIBLE);
            }
        }
        catch(Exception exec)
        {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
    }

    public int getLastSelectedDate() { return mnSelectedDate; }

    public void setLastSelectedDate(int nLastSelectedDate){ mnSelectedDate = nLastSelectedDate; }

    public void onMonthViewGone(boolean visibility) {
        if(visibility)
            moWeekNameGridHolder.setVisibility(View.VISIBLE);
        else
            moWeekNameGridHolder.setVisibility(View.GONE);
    }

    private class LoadMonthEvents extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            mListEventsForMonth = moMonthDates.getEventsForMonth();
            Log.d("doInBackground", "Set Events for each day (holidays_year.json).");
            if(!mbEventsLoaded) {
                onProcessListOfEventsForView();
                mbEventsLoaded = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(!mbUIRefreshNeeded) {
                updateUIwithEvents();
                mbUIRefreshNeeded = true;
            }
            else
                doSetMonthEvents();
        }
    }
}
