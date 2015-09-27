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

import java.util.ArrayList;
import java.util.List;

import factor.labs.indiancalendar.CalendarInterfaces.CalendarGetContextInterface;
import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;
import factor.labs.indiancalendar.R;

/**
 * Created by hassanhussain on 7/23/2015.
 */
public class CalendarReligiousScheduleView extends Fragment {
    String sTag;
    View moScheduleViewHolder = null;
    Context moContext = null;
    FragmentManager moFragManager = null;

    LayoutInflater mInflater = null;

    PagerSlidingTabStrip mTabs;
    ViewPager mPager;

    public static CalendarReligiousScheduleView init(FragmentManager oFm, Context oCon)
    {
        CalendarReligiousScheduleView oFragment = new CalendarReligiousScheduleView();
        oFragment.setContext(oCon);
        oFragment.setFragmentManager(oFm);
        return oFragment;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        moContext = getActivity().getApplicationContext();
        moFragManager = getActivity().getSupportFragmentManager();
    }

    public void setContext(Context oCOn){ moContext = oCOn; }

    public void setFragmentManager(FragmentManager oFrag) { moFragManager = oFrag; }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        sTag = null;
        moScheduleViewHolder = null;
    }

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

        private final String[] TITLES = {"Hindu","Islam", "Christian", "Jews", "Sikh", "Buddhist"};
        List<Fragment> moListFragment = new ArrayList();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            onPopulateFragments();
        }

        public void onPopulateFragments()
        {
            moListFragment.add(CalendarReligiousScheduleEvent.init(labsCalendarUtils.getCurrentMonth(), labsCalendarUtils.getCurrentYear(), moContext, 1));
            moListFragment.add(CalendarReligiousScheduleEvent.init(labsCalendarUtils.getCurrentMonth(), labsCalendarUtils.getCurrentYear(), moContext, 2));
            moListFragment.add(CalendarReligiousScheduleEvent.init(labsCalendarUtils.getCurrentMonth(), labsCalendarUtils.getCurrentYear(), moContext, 3));
            moListFragment.add(CalendarReligiousScheduleEvent.init(labsCalendarUtils.getCurrentMonth(), labsCalendarUtils.getCurrentYear(), moContext, 4));
            moListFragment.add(CalendarReligiousScheduleEvent.init(labsCalendarUtils.getCurrentMonth(), labsCalendarUtils.getCurrentYear(), moContext, 5));
            moListFragment.add(CalendarReligiousScheduleEvent.init(labsCalendarUtils.getCurrentMonth(), labsCalendarUtils.getCurrentYear(), moContext, 6));
            //moListFragment.add(CalendarReligiousScheduleEvent.init(labsCalendarUtils.getCurrentMonth(), labsCalendarUtils.getCurrentYear(), moContext, 7));
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
            return moListFragment.get(position);
        }
    }
}

