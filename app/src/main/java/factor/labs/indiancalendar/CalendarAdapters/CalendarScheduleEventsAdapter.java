package factor.labs.indiancalendar.CalendarAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import factor.labs.indiancalendar.CalendarDbHelper.CalendarEventMaster;
import factor.labs.indiancalendar.CalendarEventHandlers.CalendarEventDateClass;
import factor.labs.indiancalendar.CalendarInterfaces.CalendarLoadMoreListener;
import factor.labs.indiancalendar.CalendarObjects.CalendarMonthYearClass;
import factor.labs.indiancalendar.CalendarUtils.CalendarConstants;
import factor.labs.indiancalendar.CalendarUtils.CalendarListObjectClass;
import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;
import factor.labs.indiancalendar.R;

/**
 * Created by hassanhussain on 7/23/2015.
 */
public class CalendarScheduleEventsAdapter extends BaseAdapter {

    List<CalendarListObjectClass> mListItemobject;
    Context mContext;
    LayoutInflater mInflater;

    int HEADER_VIEW = 0;
    int EVENT_VIEW = 1;
    int NO_EVENT = 2;
    int LOAD_MORE = 3;

    CalendarLoadMoreListener moCallback;

    String sTag;

    public class CalendarScheduleMonthHolder{
        TextView otxt;
    }

    public class CalendarScheduleDayHolder{
        TextView otxt;
        TextView otxt1;
        View v;
    }

    public class CalendarScheduleEventHolder{
        TextView otxt;
        TextView otxt1;
        ImageView oVw;
    }

    public class CalendarScheduleLoadHolder{
        Button otxt;
    }

    public CalendarScheduleEventsAdapter(List<CalendarListObjectClass> oDateGrid,
                                         Context oContext, CalendarLoadMoreListener oCallback) {
        mListItemobject = oDateGrid;
        mContext = oContext;
        moCallback = oCallback;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        CalendarListObjectClass oTemp = mListItemobject.get(position);
        if(oTemp != null)
        {
            switch(oTemp.getType())
            {
                case CalendarConstants.CALENDAR_SCHEDULE_MONTH_TYPE:
                    return HEADER_VIEW;
                case CalendarConstants.CALENDAR_SCHEDULE_DAY_TYPE:
                    return EVENT_VIEW;
                case CalendarConstants.CALENDAR_SCHEDULE_EMPTY_EVENT:
                    return NO_EVENT;
                case CalendarConstants.CALENDAR_SCHEDULE_LOAD_UP:
                case CalendarConstants.CALENDAR_SCHEDULE_LOAD_DOWN:
                    return LOAD_MORE;

                default:
                    return -1;
            }
        }
        return 0;
    }

    public void setObjectListItems(List<CalendarListObjectClass> oListOfEvents)
    { mListItemobject = oListOfEvents; }

    @Override
    public int getViewTypeCount()
    {
        return 4;
    }

    @Override
    public int getCount() {
        return mListItemobject.size();
    }

    @Override
    public Object getItem(int position) {
        return mListItemobject.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        sTag = "CalendarScheduleEventsAdapter.getView";
        try {
            CalendarListObjectClass oInfo = (CalendarListObjectClass)getItem(position);
            int nType = getItemViewType(position);

            Log.d(sTag, "Type of view : " + nType + " position : " + position);

            if (nType == HEADER_VIEW) {
                CalendarScheduleMonthHolder oHolder;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.layout_calendar_schedule_header_month_view, parent, false);
                    oHolder = new CalendarScheduleMonthHolder();
                    oHolder.otxt = (TextView) convertView.findViewById(R.id.calendar_schedule_month_name);
                    convertView.setTag(oHolder);
                }
                oHolder = (CalendarScheduleMonthHolder) convertView.getTag();
                CalendarMonthYearClass oMonYr = (CalendarMonthYearClass) oInfo.getGenericObject();

                String sMonthName = labsCalendarUtils.getMonthName(oMonYr.getMonth()) + "  " + oMonYr.getYear();
                Log.d(sTag, "Month Name : " + sMonthName + " position : " + position);
                oHolder.otxt.setText(sMonthName);
                return convertView;

            } else if (nType == EVENT_VIEW) {
                CalendarEventDateClass oDate = (CalendarEventDateClass) oInfo.getGenericObject();
                CalendarScheduleDayHolder oHolder;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.layout_calendar_schedule_event_day, parent, false);
                    oHolder = new CalendarScheduleDayHolder();
                    oHolder.otxt = (TextView) convertView.findViewById(R.id.calendar_schedule_week_set);
                    oHolder.otxt1 = (TextView) convertView.findViewById(R.id.calendar_schedule_date_set);
                    oHolder.v = (View) convertView.findViewById(R.id.calendar_schedule_events_holder);

                    convertView.setTag(oHolder);
                }
                oHolder = (CalendarScheduleDayHolder) convertView.getTag();
                ((ViewGroup) oHolder.v).removeAllViewsInLayout();
                oHolder.otxt1.setText("" + oDate.getDate());
                oHolder.otxt.setText(labsCalendarUtils.getWeekNameForDate(oDate.getDate(), oDate.getMonth(), oDate.getYear()));

                for (CalendarEventMaster oEvent : oDate.getoEvents()) {
                    ((LinearLayout) oHolder.v).addView(getEventView(parent, oEvent));
                }
                Log.d(sTag, "Created view for events.");
                return convertView;
            }
            else if(nType == NO_EVENT)
            {
                Log.d(sTag, "Created view for X: NO (I meant zero) :D events.");
                if(convertView == null)
                    convertView = mInflater.inflate(R.layout.layout_calendar_schedule_event_empty, parent, false);
                return convertView;
            }
            else if(nType == LOAD_MORE)
            {
                Log.d(sTag, "Created view for load more events.");
                CalendarScheduleLoadHolder oHolder;
                if(convertView == null) {
                    convertView = mInflater.inflate(R.layout.layout_calendar_schedule_load_more, parent, false);
                    oHolder = new CalendarScheduleLoadHolder();
                    oHolder.otxt = (Button)convertView.findViewById(R.id.calendar_schedule_load_more_btn);
                    convertView.setTag(oHolder);
                }
                oHolder = (CalendarScheduleLoadHolder)convertView.getTag();
                oHolder.otxt.setTag(oInfo);

                oHolder.otxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CalendarListObjectClass oObj = (CalendarListObjectClass)v.getTag();
                        moCallback.onLoadMoreEvents(oObj.getType());
                    }
                });

                return convertView;
            }
        }
        catch(Exception exec)
        {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
        return null;
    }

    public View getEventView(ViewGroup parent, CalendarEventMaster oEvent) {
        sTag = "CalendarScheduleEventsAdapter.getEventView";
        try {
            Log.d(sTag, "Create view for events for a day.");
            View v = mInflater.inflate(R.layout.layout_calendar_schedule_event_info, parent, false);
            CalendarScheduleEventHolder oHolder = new CalendarScheduleEventHolder();
            oHolder.otxt = (TextView) v.findViewById(R.id.calendar_schedule_event_name);
            String sEvName = oEvent.getEventName();
            oHolder.otxt.setText(sEvName);
            oHolder.otxt.setFocusable(false);
            oHolder.oVw = (ImageView) v.findViewById(R.id.calendar_schedule_event_info);

            if(oEvent.getCountry() == 4)
                oHolder.oVw.setImageDrawable(mContext.getResources().getDrawable(R.drawable.unitedstates));
            else if(oEvent.getCountry() == 7)
                oHolder.oVw.setImageDrawable(mContext.getResources().getDrawable(R.drawable.india));

            oHolder.otxt1 = (TextView) v.findViewById(R.id.calendar_schedule_event_desc);
            oHolder.otxt1.setText(oEvent.getDisplayName());
            oHolder.otxt1.setFocusable(false);
            v.setTag(oHolder);
            Log.d(sTag, "Created view for events [" + sEvName + "] for a day.");
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return v;
        }
        catch(Exception exec)
        {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
        return null;
    }


}
