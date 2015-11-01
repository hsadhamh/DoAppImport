package factor.labs.indiancalendar.CalendarAdapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import factor.labs.indiancalendar.CalendarDbHelper.CalendarEventMaster;
import factor.labs.indiancalendar.CalendarInterfaces.CalendarDateClickListenerInterface;
import factor.labs.indiancalendar.CalendarUtils.CalendarDateClass;
import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;
import factor.labs.indiancalendar.R;

/**
 * Created by hassanhussain on 7/20/2015.
 */
public class DayonMonthDateAdapter extends BaseAdapter {
    int mnSelDate, mnSelMon, mnSelYr;
    List<CalendarDateClass> mListDatesForGrid;

    Context mContext;
    LayoutInflater mInflater;

    CalendarDateClickListenerInterface moClickCallback = null;

    String sTag;

    class CalendarDateHolder{
        TextView oTextView;
        View oVw;
    }

    public DayonMonthDateAdapter(List<CalendarDateClass> oDateGrid, Context oContext,
                                 CalendarDateClickListenerInterface oObj){
        mListDatesForGrid = oDateGrid;
        mContext = oContext;
        moClickCallback = oObj;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setGridDateValues(List<CalendarDateClass> oDateGrid){ mListDatesForGrid = oDateGrid; }

    public void setGridSelectionDate(int date, int mon, int yr){
        mnSelDate = date;
        mnSelMon = mon;
        mnSelYr = yr;
    }

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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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
                    convertView = mInflater.inflate(R.layout.layout_calendar_current_month_date1, parent, false);
                    oHolder = new CalendarDateHolder();
                    oHolder.oTextView = (TextView) convertView.findViewById(R.id.calendar_current_month_date);
                    oHolder.oVw = (View)convertView.findViewById(R.id.calendar_current_date_events_notify);

                } else {
                    Log.d(sTag, "Create non-current month view for position ["+ position +"].");
                    convertView = mInflater.inflate(R.layout.layout_calendar_previous_month_date1, parent, false);
                    oHolder = new CalendarDateHolder();
                    oHolder.oTextView = (TextView) convertView.findViewById(R.id.calendar_prevnext_month_date);
                    oHolder.oVw = (View)convertView.findViewById(R.id.calendar_current_date_events_notify);
                }
                convertView.setTag(oHolder);
            }
            oHolder = (CalendarDateHolder) convertView.getTag();
            oHolder.oTextView.setText(oDate.getDateString());

            if(oDate.isCurrentMonthDate())
                oHolder.oTextView.setTextColor(ContextCompat.getColor(mContext, R.color.icons));

            convertView.setOnClickListener(new onDateClickListener());
            Log.d(sTag, "Created date for position [" + position + "].");
            oHolder.oTextView.setTag(oDate);

            if(oDate.getDate() == labsCalendarUtils.getTodaysDate() &&
                    oDate.getMonth() == labsCalendarUtils.getCurrentMonth() &&
                    oDate.getYear() == labsCalendarUtils.getCurrentYear() && oDate.isCurrentMonthDate())
            {
                convertView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_calendar_ring_current_date));
            }

            if(oDate.getDate() == mnSelDate &&
                    oDate.getMonth() == mnSelMon &&
                    oDate.getYear() == mnSelYr && oDate.isCurrentMonthDate())
            {
                convertView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_calendar_ring_selected_date));
                oHolder.oTextView.setTextColor(ContextCompat.getColor(mContext, R.color.primary));
            }
            else if(oDate.getDate() == labsCalendarUtils.getTodaysDate() &&
                    oDate.getMonth() == labsCalendarUtils.getCurrentMonth() &&
                    oDate.getYear() == labsCalendarUtils.getCurrentYear() && oDate.isCurrentMonthDate())
            {
                convertView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_calendar_ring_current_date));
            }
            else convertView.setBackgroundResource(0);


            if (oDate.getEventsForDayInDisplay().size() > 0 && oDate.isCurrentMonthDate()) {
                ImageView iv = new ImageView(mContext);
                iv.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.shape_ring_event_notify));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.CENTER | Gravity.TOP;
                iv.setLayoutParams(lp);
                Log.d(sTag, "Load event notify");
                ((LinearLayout) oHolder.oVw).removeAllViews();
                ((LinearLayout) oHolder.oVw).addView(iv);
            }
            return convertView;
        }
        catch(Exception exec)
        {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
        return null;
    }

    class onDateClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            sTag = "onDateClickListener.onClick";
            LinearLayout b = (LinearLayout) v;
            CalendarDateHolder oHolder = (CalendarDateHolder)b.getTag();
            CalendarDateClass oDate = (CalendarDateClass) oHolder.oTextView.getTag();
            Log.d(sTag, "Clicked date : " + oDate.getFullDateString());
            moClickCallback.doProcessClickEventForDate(oDate);
        }
    }

}
