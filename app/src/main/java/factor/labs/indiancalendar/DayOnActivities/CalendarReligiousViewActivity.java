package factor.labs.indiancalendar.DayOnActivities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import factor.labs.indiancalendar.CalendarUI.CalendarViews.CalendarReligiousScheduleEvent;
import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;
import factor.labs.indiancalendar.R;

/**
 * Created by hassanhussain on 7/23/2015.
 */
public class CalendarReligiousViewActivity extends AppCompatActivity {
    String sTag;

    LayoutInflater mInflater = null;

    PagerSlidingTabStrip mTabs;
    ViewPager mPager;
    Toolbar moActionBar;

    public void onCreate(Bundle savedInstanceState) {
        sTag = "CalendarScheduleViewFragment.onCreateView";
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.layout_calendar_schedule_pager);

            mInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            mTabs = (PagerSlidingTabStrip)findViewById(R.id.id_cal_schedule_tabs);
            mPager = (ViewPager)findViewById(R.id.id_cal_schedule_pager);

            moActionBar = (Toolbar) findViewById(R.id.id_cal_action_bar);
            setSupportActionBar(moActionBar);

            moActionBar.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            moActionBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
            mPager.setAdapter(adapter);

            mTabs.setViewPager(mPager);

            final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                    .getDisplayMetrics());
            mPager.setPageMargin(pageMargin);
            mPager.setCurrentItem(0);
        } catch (Exception exec) {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
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
            moListFragment.add(CalendarReligiousScheduleEvent.init(labsCalendarUtils.getCurrentMonth(), labsCalendarUtils.getCurrentYear(), getApplicationContext(), 1));
            moListFragment.add(CalendarReligiousScheduleEvent.init(labsCalendarUtils.getCurrentMonth(), labsCalendarUtils.getCurrentYear(), getApplicationContext(), 2));
            moListFragment.add(CalendarReligiousScheduleEvent.init(labsCalendarUtils.getCurrentMonth(), labsCalendarUtils.getCurrentYear(), getApplicationContext(), 3));
            moListFragment.add(CalendarReligiousScheduleEvent.init(labsCalendarUtils.getCurrentMonth(), labsCalendarUtils.getCurrentYear(), getApplicationContext(), 4));
            moListFragment.add(CalendarReligiousScheduleEvent.init(labsCalendarUtils.getCurrentMonth(), labsCalendarUtils.getCurrentYear(), getApplicationContext(), 5));
            moListFragment.add(CalendarReligiousScheduleEvent.init(labsCalendarUtils.getCurrentMonth(), labsCalendarUtils.getCurrentYear(), getApplicationContext(), 6));
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

