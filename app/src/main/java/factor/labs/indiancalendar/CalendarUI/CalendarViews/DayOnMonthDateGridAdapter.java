package factor.labs.indiancalendar.CalendarUI.CalendarViews;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
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
    public void setListDates(List<CalendarDateClass> moListDates) {
        this.moListDates = moListDates;
    }

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
        if(view == null){
            view = mInflater.inflate(R.layout.layout_dayon_date_view, viewGroup, false);
            CalendarDateHolder oHolder = new CalendarDateHolder();
            oHolder.otxt = (TextView) view.findViewById(R.id.id_cal_date_view);
            oHolder.v = view;
            view.setTag(oHolder);
        }
        CalendarDateHolder oHolder1 = (CalendarDateHolder) view.getTag();
        CalendarDateClass oDate = (CalendarDateClass)getItem(i);
        oHolder1.otxt.setText(""+oDate.getDate());

        if(!oDate.isCurrentMonthDate())
            oHolder1.otxt.setTextColor(ContextCompat.getColor(mContext, R.color.Grey200));

        if(oDate.getEventsForDay().size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                oHolder1.v.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_rectangle_date_grid_event));
            }
            else
                oHolder1.v.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.shape_rectangle_date_grid_event));
        }

        return view;
    }

    class CalendarDateHolder{
        TextView otxt;
        View v;
    }
}
