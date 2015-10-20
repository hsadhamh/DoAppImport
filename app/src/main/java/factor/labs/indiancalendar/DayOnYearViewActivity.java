package factor.labs.indiancalendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import factor.labs.indiancalendar.CalendarAdapters.DayonPentaMonthAdapter;
import factor.labs.indiancalendar.CalendarUI.CalendarViews.DayOnYearGridFragment;

/**
 * Created by shussain on 20/10 020.
 */
public class DayOnYearViewActivity extends AppCompatActivity{

    ViewPager moPager = null;

    DayonPentaMonthAdapter moAdapter = null;
    List<Fragment> moListFragment = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dayon_year_home);

        moPager = (ViewPager)findViewById(R.id.id_cal_year_pager);
        setPagerProperties();
    }

    void setPagerProperties(){

        DayOnYearGridFragment oYr = DayOnYearGridFragment.init(2005);
        DayOnYearGridFragment oYr1 = DayOnYearGridFragment.init(2006);
        DayOnYearGridFragment oYr2 = DayOnYearGridFragment.init(2007);

        moListFragment.add(oYr);
        moListFragment.add(oYr1);
        moListFragment.add(oYr2);

        moAdapter = new DayonPentaMonthAdapter(getSupportFragmentManager(), moListFragment);
        moPager.setAdapter(moAdapter);
        moAdapter.notifyDataSetChanged();
    }
}
