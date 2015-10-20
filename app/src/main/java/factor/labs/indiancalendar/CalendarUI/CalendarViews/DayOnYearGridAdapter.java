package factor.labs.indiancalendar.CalendarUI.CalendarViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import factor.labs.indiancalendar.CalendarUtils.CalendarMonthClass;
import factor.labs.indiancalendar.R;

/**
 * Created by shussain on 20/10 020.
 */
public class DayOnYearGridAdapter extends BaseAdapter {
    LayoutInflater mInflater = null;
    Context mContext;

    List<CalendarMonthClass> moListMonthClass = new ArrayList<>();
    public List<CalendarMonthClass> getListMonthClass() { return moListMonthClass; }
    public void setMoListMonthClass(List<CalendarMonthClass> moListMonthClass) { this.moListMonthClass = moListMonthClass; }

    public DayOnYearGridAdapter(List<CalendarMonthClass> listMonths, Context con){
        moListMonthClass.addAll(listMonths);
        mContext = con;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return moListMonthClass.size();
    }

    @Override
    public Object getItem(int i) {
        return moListMonthClass.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        GridView oDateGrid;
        CalendarMonthClass mon = (CalendarMonthClass)getItem(i);
        if(view == null){
            view = mInflater.inflate(R.layout.layout_dayon_year_month_view, viewGroup, false);
        }
        oDateGrid = (GridView) view.findViewById(R.id.id_cal_month_view);
        DayOnMonthDateGridAdapter oAdapter = new DayOnMonthDateGridAdapter(mon.getDateForGrid(), mContext);
        oDateGrid.setAdapter(oAdapter);
        oAdapter.notifyDataSetChanged();
        return view;
    }
}
