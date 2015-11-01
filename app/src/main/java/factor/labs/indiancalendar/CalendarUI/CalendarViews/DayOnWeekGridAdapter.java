package factor.labs.indiancalendar.CalendarUI.CalendarViews;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import factor.labs.indiancalendar.CalendarUtils.CalendarDateClass;
import factor.labs.indiancalendar.R;

/**
 * Created by shussain on 20/10 020.
 */
public class DayOnWeekGridAdapter extends BaseAdapter{
    LayoutInflater mInflater = null;
    Context mContext;

    String[] mListWeeks = null;


    public DayOnWeekGridAdapter(String[] oListWeeks, Context con){
        mListWeeks = oListWeeks;
        mContext = con;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return mListWeeks.length;
    }

    @Override
    public Object getItem(int i) {
        return mListWeeks[i];
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
            view.setTag(oHolder);
        }
        CalendarDateHolder oHolder1 = (CalendarDateHolder) view.getTag();
        oHolder1.otxt.setText("" + getItem(i));
        oHolder1.otxt.setTextColor(ContextCompat.getColor(mContext, R.color.Grey600));
        return view;
    }

    class CalendarDateHolder{
        TextView otxt;
    }
}
