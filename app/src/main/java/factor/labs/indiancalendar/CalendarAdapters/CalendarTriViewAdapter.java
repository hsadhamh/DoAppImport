package factor.labs.indiancalendar.CalendarAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by hassanhussain on 7/19/2015.
 */

//
//  Class always has 3 views.
//  Used for MonthAdapter and DateSchedule Adapter
//
public class CalendarTriViewAdapter extends FragmentStatePagerAdapter {

    Fragment[] moTriViews = null;

    public CalendarTriViewAdapter(FragmentManager fm) {
        super(fm);
    }

    public CalendarTriViewAdapter(FragmentManager fm, Fragment[] listViews) {
        super(fm);
        moTriViews = listViews;
    }

    @Override
    public int getItemPosition(Object object)
    {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return moTriViews[position];
    }

    public void setTriViews(Fragment[] listViews)
    {
        moTriViews = listViews;
    }

    @Override
    public int getCount() {
        return 3;   //  Class always has 3 views.
    }
}
