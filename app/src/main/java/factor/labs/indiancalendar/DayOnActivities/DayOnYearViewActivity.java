package factor.labs.indiancalendar.DayOnActivities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import factor.labs.indiancalendar.CalendarAdapters.DayOnViewPagerAdapter;
import factor.labs.indiancalendar.CalendarUI.CalendarDialogUI.core.MaterialDialog;
import factor.labs.indiancalendar.CalendarUI.CalendarViews.DayOnYearGridFragment;
import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;
import factor.labs.indiancalendar.R;

/**
 * Created by shussain on 20/10 020.
 */
public class DayOnYearViewActivity extends AppCompatActivity{

    ViewPager moPager = null;
    Toolbar moActionBar = null;

    DayOnViewPagerAdapter moAdapter = null;
    List<Fragment> moListFragment = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dayon_year_home);

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
        moPager = (ViewPager)findViewById(R.id.id_cal_year_pager);
        new LoadFirstMonths().execute();
    }

    public void onBackPressed(){
        finish();
        super.onBackPressed();
    }

    public void onLoadMonthData(){
        DayOnYearGridFragment oYr = DayOnYearGridFragment.init(labsCalendarUtils.getCurrentYear() -1);
        DayOnYearGridFragment oYr1 = DayOnYearGridFragment.init(labsCalendarUtils.getCurrentYear());
        DayOnYearGridFragment oYr2 = DayOnYearGridFragment.init(labsCalendarUtils.getCurrentYear() +1);

        moListFragment.add(oYr);
        moListFragment.add(oYr1);
        moListFragment.add(oYr2);
    }

    void setPagerProperties(){
        moAdapter = new DayOnViewPagerAdapter(getSupportFragmentManager(), moListFragment);
        moPager.setAdapter(moAdapter);
        moAdapter.notifyDataSetChanged();

        moPager.setOffscreenPageLimit(2);
        moPager.setCurrentItem(1);
    }

    private class LoadFirstMonths extends AsyncTask<Void, Void, Void> {
        MaterialDialog mDialog = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new MaterialDialog.Builder(DayOnYearViewActivity.this)
                    .content(R.string.please_wait)
                    .progress(true, 0)
                    .progressIndeterminateStyle(false).show();
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            onLoadMonthData();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mDialog.dismiss();
            setPagerProperties();
        }
    }

}
