package factor.labs.indiancalendar.CalendarUI.CalendarViews;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import factor.labs.indiancalendar.CalendarUtils.CalendarMonthClass;
import factor.labs.indiancalendar.R;

/**
 * Created by shussain on 20/10 020.
 */
public class DayOnYearGridFragment extends Fragment {

    String sTag;

    View moViewHolder = null;
    GridView moYearGrid = null;

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
            moYearGrid = (GridView) moViewHolder.findViewById(R.id.id_cal_grid_year);

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
        DayOnYearGridAdapter oAdapter = new DayOnYearGridAdapter(moListMonths, getActivity().getApplicationContext());
        moYearGrid.setAdapter(oAdapter);
        oAdapter.notifyDataSetChanged();
    }

}
