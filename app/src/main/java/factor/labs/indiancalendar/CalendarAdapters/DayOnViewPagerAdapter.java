package factor.labs.indiancalendar.CalendarAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by hassanhussain on 7/19/2015.
 */

//
//  Class always has 3 views.
//  Used for MonthAdapter and DateSchedule Adapter
//
public class DayOnViewPagerAdapter extends FragmentStatePagerAdapter {
    List<Fragment> moTriViews;

    public DayOnViewPagerAdapter(FragmentManager fm, List<Fragment> listViews) {
        super(fm);
        moTriViews = listViews;
    }

    @Override
    public Fragment getItem(int position) {
        return moTriViews.get(position);
    }

    @Override
    public int getCount() {
        return moTriViews.size();
    }

}
