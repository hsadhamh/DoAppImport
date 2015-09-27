package factor.labs.indiancalendar.CalendarUI.CalendarViews;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import factor.labs.indiancalendar.CalendarInterfaces.CalendarGetContextInterface;
import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;
import factor.labs.indiancalendar.R;

/**
 * Created by hassanhussain on 7/23/2015.
 */
public class CalendarMdScheduleView extends Fragment {
    String sTag;
    View moScheduleViewHolder = null;
    Context moContext = null;
    FragmentManager moFragManager = null;

    LayoutInflater mInflater = null;

    PagerSlidingTabStrip mTabs;
    ViewPager mPager;

    @Override
    public void onAttach(Context activity){
        super.onAttach(activity);
        moContext = getActivity().getApplicationContext();
        moFragManager = getActivity().getSupportFragmentManager();
    }

    public static CalendarMdScheduleView init(FragmentManager oFm, Context oCon)
    {
        CalendarMdScheduleView oFragment = new CalendarMdScheduleView();
        oFragment.setContext(oCon);
        oFragment.setFragmentManager(oFm);
        return oFragment;
    }

    public void setContext(Context oCOn){ moContext = oCOn; }

    public void setFragmentManager(FragmentManager oFrag) { moFragManager = oFrag; }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sTag = "CalendarScheduleViewFragment.onCreateView";
        try {
            super.onCreate(savedInstanceState);
            int value = (getArguments() != null ? getArguments().getInt("key") : 1);

            moScheduleViewHolder = inflater.inflate(R.layout.layout_calendar_schedule_pager, container, false);
            mInflater = (LayoutInflater) moContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            mTabs = (PagerSlidingTabStrip)moScheduleViewHolder.findViewById(R.id.id_cal_schedule_tabs);
            mPager = (ViewPager) moScheduleViewHolder.findViewById(R.id.id_cal_schedule_pager);

            MyPagerAdapter adapter = new MyPagerAdapter(moFragManager);
            mPager.setAdapter(adapter);

            mTabs.setViewPager(mPager);

            final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                    .getDisplayMetrics());
            mPager.setPageMargin(pageMargin);
            mPager.setCurrentItem(0);
            return moScheduleViewHolder;
        } catch (Exception exec) {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
        return null;
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        private final String[] TITLES = {"Holidays","Religious Events"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object)
        {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0)
                return CalendarScheduleViewFragment.init(
                        labsCalendarUtils.getCurrentMonth(), labsCalendarUtils.getCurrentYear(), moContext);
            else if(position == 1)
                return CalendarReligiousScheduleEvent.init(
                        labsCalendarUtils.getCurrentMonth(), labsCalendarUtils.getCurrentYear(), moContext, 0);
            return null;
        }
    }
}
