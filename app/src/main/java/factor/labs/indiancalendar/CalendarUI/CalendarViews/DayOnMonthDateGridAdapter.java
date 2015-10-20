package factor.labs.indiancalendar.CalendarUI.CalendarViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import factor.labs.indiancalendar.CalendarUtils.CalendarDateClass;
import factor.labs.indiancalendar.CalendarUtils.CalendarMonthClass;
import factor.labs.indiancalendar.R;

/**
 * Created by shussain on 20/10 020.
 */
public class DayOnMonthDateGridAdapter extends BaseAdapter{
    LayoutInflater mInflater = null;
    Context mContext;

    List<CalendarDateClass> moListDates = new ArrayList<>();
    public List<CalendarDateClass> getListDates() { return moListDates; }
    public void setListDates(List<CalendarDateClass> moListDates) { this.moListDates = moListDates; }

    public DayOnMonthDateGridAdapter(List<CalendarDateClass> listDates, Context con){
        moListDates.addAll(listDates);
        mContext = con;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return moListDates.size();
    }

    @Override
    public Object getItem(int i) {
        return moListDates.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView oTxt = null;
        if(view == null){
            view = mInflater.inflate(R.layout.layout_dayon_date_view, viewGroup, false);
        }
        oTxt = (TextView) view.findViewById(R.id.id_cal_date_view);
        CalendarDateClass oDate = (CalendarDateClass)getItem(i);
        oTxt.setText(""+oDate.getDate());
        return view;
    }
}
