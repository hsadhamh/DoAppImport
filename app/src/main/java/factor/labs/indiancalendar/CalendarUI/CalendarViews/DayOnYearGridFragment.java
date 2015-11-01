package factor.labs.indiancalendar.CalendarUI.CalendarViews;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import factor.labs.indiancalendar.CalendarUtils.CalendarMonthClass;
import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;
import factor.labs.indiancalendar.R;

/**
 * Created by shussain on 20/10 020.
 */
public class DayOnYearGridFragment extends Fragment {

    String sTag;

    View moViewHolder = null;
    GridLayout moYearGrid = null;

    public int mnPosition = 0;

    String[] mListWeekNames = new String[]{"S", "M", "T", "W", "T", "F", "S"};

    int[] mGridWeek = {R.id.id_cal_week_view1, R.id.id_cal_week_view2, R.id.id_cal_week_view3, R.id.id_cal_week_view4,
            R.id.id_cal_week_view5, R.id.id_cal_week_view6, R.id.id_cal_week_view7, R.id.id_cal_week_view8, R.id.id_cal_week_view9,
            R.id.id_cal_week_view10, R.id.id_cal_week_view11, R.id.id_cal_week_view12};

    int[] mGridIds = {R.id.id_cal_month_view1, R.id.id_cal_month_view2, R.id.id_cal_month_view3, R.id.id_cal_month_view4,
            R.id.id_cal_month_view5, R.id.id_cal_month_view6, R.id.id_cal_month_view7, R.id.id_cal_month_view8, R.id.id_cal_month_view9,
            R.id.id_cal_month_view10, R.id.id_cal_month_view11, R.id.id_cal_month_view12};

    int[] mMonthIds = {R.id.id_cal_month_name1, R.id.id_cal_month_name2, R.id.id_cal_month_name3, R.id.id_cal_month_name4,
            R.id.id_cal_month_name5, R.id.id_cal_month_name6, R.id.id_cal_month_name7, R.id.id_cal_month_name8, R.id.id_cal_month_name9,
            R.id.id_cal_month_name10, R.id.id_cal_month_name11, R.id.id_cal_month_name12};

    List<GridView> moListGridViews = new ArrayList<>();
    List<TextView> moListTextViews = new ArrayList<>();
    List<GridView> moListWeekGrid = new ArrayList<>();

    LayoutInflater mInflater = null;

    int mnYear = 2000;
    public int getYear() { return mnYear; }
    public void setYear(int mnYear) { this.mnYear = mnYear; }

    List<CalendarMonthClass> moListMonths =  new ArrayList<>();


    public static DayOnYearGridFragment init(int yr){
        DayOnYearGridFragment oYr =  new DayOnYearGridFragment();
        oYr.setYear(yr);
        oYr.onPrepareListOfMonths();
        return oYr;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sTag = "DayOnMonthGridFragment.onCreateView";
        try {
            moViewHolder = (View) inflater.inflate(R.layout.layout_dayon_year_view, container, false);
            moYearGrid = (GridLayout) moViewHolder.findViewById(R.id.id_cal_year_grid_holder);

            for(int i=0; i<12; i++){
                moListGridViews.add((GridView) moYearGrid.findViewById(mGridIds[i]));
                moListTextViews.add((TextView) moYearGrid.findViewById(mMonthIds[i]));
                moListWeekGrid.add((GridView) moYearGrid.findViewById(mGridWeek[i]));
            }
            onPrepareListMonthViews();

        } catch (Exception ex) {
        }
        return moViewHolder;
    }

    public void onPrepareListOfMonths(){
        for(int i=1; i<13; i++){
            CalendarMonthClass oMonClass = new CalendarMonthClass(i, mnYear);
            oMonClass.prepareCalendarMonthDates();
            oMonClass.getEventsForMonth();
            moListMonths.add(oMonClass);
        }
    }

    public void onPrepareListMonthViews(){
        for(int i=0; i<12; i++) {
            CalendarMonthClass oMon = moListMonths.get(i);

            GridView oGrid = moListGridViews.get(i);

            TextView oTxt = moListTextViews.get(i);

            GridView oWeekGrid = moListWeekGrid.get(i);
            BaseAdapter oAdap1 = new DayOnWeekGridAdapter(mListWeekNames, getActivity().getApplicationContext());
            oWeekGrid.setAdapter(oAdap1);
            oAdap1.notifyDataSetChanged();


            String sMon = labsCalendarUtils.getMonthName(oMon.getMonth()) + " " + oMon.getYear();
            oTxt.setText(sMon);

            DayOnMonthDateGridAdapter oAdap =
                    new DayOnMonthDateGridAdapter(oMon.getDateForGrid(), getActivity().getApplicationContext());
            oGrid.setAdapter(oAdap);
            oAdap.notifyDataSetChanged();
        }
    }
}
