package factor.labs.indiancalendar.CalendarAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import factor.labs.indiancalendar.CalendarInterfaces.DayOnMonGridTouchInterface;

/**
 * Created by hassanhussain on 7/19/2015.
 */

//
//  Class always has 3 views.
//  Used for MonthAdapter and DateSchedule Adapter
//
public class DayonPentaMonthAdapter extends FragmentStatePagerAdapter {

    List<Fragment> moTriViews = null;

    public DayonPentaMonthAdapter(FragmentManager fm) {
        super(fm);
    }

    public DayonPentaMonthAdapter(FragmentManager fm, List<Fragment> listViews) {
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
        return moTriViews.get(position);
    }

    public void setTriViews(List<Fragment> listViews)
    {
        moTriViews = listViews;
    }

    @Override
    public int getCount() {
        return 3;   //  Class always has 5 views.
    }
}
