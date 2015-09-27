package factor.labs.indiancalendar.CalendarUI.CalendarViews;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import factor.labs.indiancalendar.CalendarAdapters.CalendarTriViewAdapter;
import factor.labs.indiancalendar.CalendarAdapters.CalendarWeekNameDisplayAdapter;
import factor.labs.indiancalendar.CalendarInterfaces.CalendarGetContextInterface;
import factor.labs.indiancalendar.CalendarInterfaces.CalendarMonthChangeListener;
import factor.labs.indiancalendar.CalendarUtils.CalendarConstants;
import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;
import factor.labs.indiancalendar.R;

/**
 * Created by hassanhussain on 7/19/2015.
 */
public class CalendarMonthViewFragment extends Fragment implements CalendarMonthChangeListener {

    String sTag;

    FragmentManager moFragmentManager;
    Context moContext;

    ViewPager moPagerMonthList;
    View moMonthViewsHolder;

    int mnSelectedPage = 1;
    CalendarTriViewAdapter moAdapterViews;

    CalendarGetContextInterface oActivityContact;

    Fragment[] moTriMonthList=null;

    int mnMonthInit = 1;
    int mnYearInit = 2015;

    public static CalendarMonthViewFragment init(int nMon, int nYr) {
        CalendarMonthViewFragment oFragment = new CalendarMonthViewFragment();

        Bundle args = new Bundle();
        args.putInt("month", nMon);
        args.putInt("year", nYr);
        oFragment.setArguments(args);

        return oFragment;
    }

    public void setActivityContact(CalendarGetContextInterface oAct)
    {
        oActivityContact = oAct;
    }

    @Override
    public void onAttach(Context activity){
        super.onAttach(activity);
        moContext = getActivity().getApplicationContext();
        moFragmentManager = getActivity().getSupportFragmentManager();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sTag = "CalendarMonthViewFragment.onCreateView";
        try {
            super.onCreate(savedInstanceState);
            mnMonthInit = (getArguments() != null ? getArguments().getInt("month") : 1);
            mnYearInit = (getArguments() != null ? getArguments().getInt("year") : 2015);

            moMonthViewsHolder = inflater.inflate(R.layout.layout_calendar_month_list, container, false);
            moPagerMonthList = (ViewPager) moMonthViewsHolder.findViewById(R.id.calendar_month_view_list);
            Log.d(sTag, "Set pager features .");
            setPagerFeatures();
            return moMonthViewsHolder;
        }
        catch(Exception exec)
        { Log.e(sTag, "Exception caught : " + exec.getMessage()); }
        return null;
    }



    public void setPagerFeatures()
    {
        sTag = "CalendarMonthViewFragment.setPagerFeatures";
        try {
            Log.d(sTag, "month view fragments start..!!");
            moTriMonthList = new Fragment[3];

            //  create new views for month
            prepareMonthViewsList(moTriMonthList);
            Log.d(sTag, "month view list created..!");
            moPagerMonthList.setOffscreenPageLimit(1);

            moAdapterViews = new CalendarTriViewAdapter(moFragmentManager, moTriMonthList);
            Log.d(sTag, "Month view adapter set..!");
            moPagerMonthList.setAdapter(moAdapterViews);
            updateListForMonth();

            moPagerMonthList.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    mnSelectedPage = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        if (mnSelectedPage == 0) {
                            Log.d(sTag, "Month view adapter : swiped right!!");
                            //swiped right  //  remove 2 //  move 0 -> 1 //  move 1 -> 2 //  add 0 //  set 1
                            addPreviousMonthOnRightSwipe(moTriMonthList);

                        } else if (mnSelectedPage == 2) {
                            Log.d(sTag, "Month view adapter : swiped left!!");
                            // swiped left //  Remove 0 //  move 1 -> 0 //  move 2 -> 1 //  add 2 //  set 1
                            addNextMonthOnLeftSwipe(moTriMonthList);
                        }

                        //  set changes to adapter and notify the changes
                        updateListForMonth();
                    }
                }
            });
        }
        catch (Exception exec)
        { Log.e(sTag, "Exception caught : " + exec.getMessage()); }
    }

    void updateListForMonth()
    {
        sTag = "CalendarMonthViewFragment.updateListForMonth";
        try {
            //  set changes to adapter and notify the changes
            moAdapterViews.setTriViews(moTriMonthList);
            moAdapterViews.notifyDataSetChanged();
            Log.d(sTag, "Month view update..!!");
            //  set center screen for view.
            mnSelectedPage = 1;
            moPagerMonthList.setCurrentItem(mnSelectedPage, false);
            Log.d(sTag, "Month view updated..!!");
        }
        catch (Exception exec){ Log.e(sTag, "Exception caught : " + exec.getMessage()); }
    }

    public void prepareMonthViewsList(Fragment[] moTriMonthList)
    {
        sTag = "CalendarMonthViewFragment.prepareMonthViewsList";
        try{
            int nMonth = mnMonthInit;
            int nYear = mnYearInit;

            int nLastSelectedDate = labsCalendarUtils.getTodaysDate();

            Log.d(sTag, "Date ["+ nLastSelectedDate +"] Month ["+ nMonth +"] Year ["+ nYear +"].");

            int nPrevMonth = nMonth -1;
            int nNextMonth = nMonth +1;
            int nPrevYear = nYear;
            int nNextYear = nYear;

            if(nMonth == 1)
            {
                nPrevMonth = 12;
                nPrevYear--;
            }
            if(nMonth == 12)
            {
                nNextMonth = 1;
                nNextYear++;
            }

            Log.d(sTag, "Creating Month views ..!!");

            moTriMonthList[0] = CalendarMonthGridView.init(nLastSelectedDate, (int)nPrevMonth,
                    (int)nPrevYear, moFragmentManager, moContext, this);
            moTriMonthList[1] = CalendarMonthGridView.init(nLastSelectedDate, (int)nMonth,
                    (int)nYear, moFragmentManager, moContext, this);
            moTriMonthList[2] = CalendarMonthGridView.init(nLastSelectedDate, (int)nNextMonth,
                    (int)nNextYear, moFragmentManager, moContext, this);

            Log.d(sTag, "Creating Month views completed!!");
        }
        catch (Exception exec)
        { Log.e(sTag, "Exception caught : " + exec.getMessage()); }
    }

    public void addPreviousMonthOnRightSwipe(Fragment[] moTriMonthList)
    {
        sTag = "CalendarMonthViewFragment.addPreviousMonthOnRightSwipe";
        try {
            int nSelectedDate = ((CalendarMonthGridView) moTriMonthList[1]).getLastSelectedDate();

            moTriMonthList[2] = moTriMonthList[1];
            moTriMonthList[1] = moTriMonthList[0];

            int mon = ((CalendarMonthGridView) moTriMonthList[1]).getMonth();
            int yr = ((CalendarMonthGridView) moTriMonthList[1]).getYear();

            Log.d(sTag, "Selected Date [" + nSelectedDate + "] month [" + mon + "] year [" + yr + "].");

            ((CalendarMonthGridView) moTriMonthList[1]).setLastSelectedDate(nSelectedDate);
            ((CalendarMonthGridView) moTriMonthList[2]).setLastSelectedDate(nSelectedDate);

            if (mon == 1) {
                mon = 12;
                yr--;
            } else {
                mon--;
            }
            Log.d(sTag, "Creating new view for Date [" + nSelectedDate + "] month [" + mon + "] year [" + yr + "].");

            CalendarMonthGridView newMonthView = CalendarMonthGridView.init(nSelectedDate, (int) mon, (int) yr,
                    moFragmentManager, moContext, this);
            moTriMonthList[0] = newMonthView;
        }
        catch (Exception exec)
        { Log.e(sTag, "Exception caught : " + exec.getMessage()); }
    }

    public void addNextMonthOnLeftSwipe(Fragment[] moTriMonthList)
    {
        sTag = "CalendarMonthViewFragment.addNextMonthOnLeftSwipe";
        try {
            int nSelectedDate = ((CalendarMonthGridView) moTriMonthList[1]).getLastSelectedDate();

            moTriMonthList[0] = moTriMonthList[1];
            moTriMonthList[1] = moTriMonthList[2];

            int mon = ((CalendarMonthGridView) moTriMonthList[1]).getMonth();
            int yr = ((CalendarMonthGridView) moTriMonthList[1]).getYear();

            Log.d(sTag, "Selected Date ["+ nSelectedDate +"] month [" + mon+ "] year ["+yr+"].");

            ((CalendarMonthGridView) moTriMonthList[1]).setLastSelectedDate(nSelectedDate);
            ((CalendarMonthGridView) moTriMonthList[0]).setLastSelectedDate(nSelectedDate);

            if (mon == 12) {
                mon = 1;
                yr++;
            } else
                mon++;

            Log.d(sTag, "Creating new view for Date ["+ nSelectedDate +"] month [" + mon+ "] year ["+yr+"].");

            CalendarMonthGridView newMonthView = CalendarMonthGridView.init(nSelectedDate, (int) mon, (int) yr,
                    moFragmentManager, moContext, this);
            moTriMonthList[2] = newMonthView;
        }
        catch (Exception exec)
        { Log.e(sTag, "Exception caught : " + exec.getMessage()); }
    }

    @Override
    public void onProcessMonthChage(int nTypeSwiped) {
        sTag = "CalendarMonthViewFragment.onProcessMonthChage";
        if(nTypeSwiped == CalendarConstants.CALENDAR_MONTH_SWIPE_LEFT)
        {
            Log.d(sTag, "Swipe left and change views.");
            addNextMonthOnLeftSwipe(moTriMonthList);
            updateListForMonth();
        }
        else if(nTypeSwiped == CalendarConstants.CALENDAR_MONTH_SWIPE_RIGHT)
        {
            Log.d(sTag, "Swipe right and change views.");
            addPreviousMonthOnRightSwipe(moTriMonthList);
            updateListForMonth();
        }
    }

    public int getCurrentMonthInView()
    {
        return ((CalendarMonthGridView)moTriMonthList[1]).getMonth();
    }

    public int getCurrentYearInView()
    {
        return ((CalendarMonthGridView)moTriMonthList[1]).getYear();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        sTag = null;
        moFragmentManager = null;
        moContext = null;
        moPagerMonthList = null;
        moMonthViewsHolder = null;
        moAdapterViews = null;
        oActivityContact = null;
        moTriMonthList=null;
    }
}
