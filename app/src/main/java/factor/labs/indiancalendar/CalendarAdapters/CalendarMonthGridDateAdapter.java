package factor.labs.indiancalendar.CalendarAdapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;

import factor.labs.indiancalendar.CalendarDbHelper.CalendarEventMaster;
import factor.labs.indiancalendar.CalendarInterfaces.CalendarDateClickListenerInterface;
import factor.labs.indiancalendar.CalendarUtils.CalendarDateClass;
import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;
import factor.labs.indiancalendar.R;

/**
 * Created by hassanhussain on 7/20/2015.
 */
public class CalendarMonthGridDateAdapter extends BaseAdapter {

    List<CalendarDateClass> mListDatesForGrid;
    Context mContext;
    LayoutInflater mInflater;

    String sTag;

    CalendarDateClickListenerInterface moClickCallback;

    class CalendarDateHolder{
        Button oButton;
        View oVw;
    }

    class onDateClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            sTag = "onDateClickListener.onClick";
            Button b = (Button) v;
            CalendarDateClass oDate = (CalendarDateClass) b.getTag();
            Log.d(sTag, "Clicked date : " + oDate.getFullDateString());
            moClickCallback.doProcessClickEventForDate(oDate);
        }
    }

    public CalendarMonthGridDateAdapter(List<CalendarDateClass> oDateGrid, Context oContext,
                                        CalendarDateClickListenerInterface callback){
        mListDatesForGrid = oDateGrid;
        mContext = oContext;
        moClickCallback = callback;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setGridDateValues(List<CalendarDateClass> oDateGrid){ mListDatesForGrid = oDateGrid; }

    @Override
    public int getCount() {
        return mListDatesForGrid.size();
    }

    @Override
    public Object getItem(int position) {
        return mListDatesForGrid.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        sTag = "CalendarMonthGridDateAdapter.getView";
        try {
            CalendarDateClass oDate = mListDatesForGrid.get(position);
            CalendarDateHolder oHolder;
            if (convertView == null) {
                Log.d(sTag, "Create view for position ["+ position +"].");
                if (oDate.isCurrentMonthDate()) {
                    Log.d(sTag, "Create current month view for position ["+ position +"].");
                    convertView = mInflater.inflate(R.layout.layout_calendar_current_month_date, parent, false);
                    oHolder = new CalendarDateHolder();
                    oHolder.oButton = (Button) convertView.findViewById(R.id.calendar_current_month_date);
                    oHolder.oVw = (View)convertView.findViewById(R.id.calendar_current_date_events_notify);

                } else {
                    Log.d(sTag, "Create non-current month view for position ["+ position +"].");
                    convertView = mInflater.inflate(R.layout.layout_calendar_previous_month_date, parent, false);
                    oHolder = new CalendarDateHolder();
                    oHolder.oButton = (Button) convertView.findViewById(R.id.calendar_prevnext_month_date);
                    oHolder.oVw = (View)convertView.findViewById(R.id.calendar_current_date_events_notify);
                }
                convertView.setTag(oHolder);
            }
            oHolder = (CalendarDateHolder) convertView.getTag();
            oHolder.oButton.setText(oDate.getDateString());
            oHolder.oButton.setOnClickListener(new onDateClickListener());
            Log.d(sTag, "Created date for position [" + position + "].");
            oHolder.oButton.setTag(oDate);

            if(oDate.getDate() == labsCalendarUtils.getTodaysDate() &&
                    oDate.getMonth() == labsCalendarUtils.getCurrentMonth() &&
                    oDate.getYear() == labsCalendarUtils.getCurrentYear() && oDate.isCurrentMonthDate())
            {
                convertView.setBackground(mContext.getResources().getDrawable(R.drawable.shape_calendar_ring_current_date));
            }

            if (oDate.hasReligiousFlagSet() && oDate.isCurrentMonthDate()) {
                ImageView iv = new ImageView(mContext);
                iv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shape_ring_event_notify));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.CENTER | Gravity.TOP;
                iv.setLayoutParams(lp);
                Log.d(sTag,"Load event notify");
                ((LinearLayout) oHolder.oVw).removeAllViews();
                ((LinearLayout) oHolder.oVw).addView(iv);
            }

            if(oDate.hasHolidayFlagSet()&& oDate.isCurrentMonthDate())
                oHolder.oButton.setTextColor(Color.RED);

            return convertView;
        }
        catch(Exception exec)
        {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
        return null;
    }

}
