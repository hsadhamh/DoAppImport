package factor.labs.indiancalendar.CalendarAdapters;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import factor.labs.indiancalendar.CalendarDbHelper.CalendarEventLocationCityInfo;
import factor.labs.indiancalendar.CalendarDbHelper.CalendarEventLocationCountryInfo;
import factor.labs.indiancalendar.CalendarDbHelper.CalendarEventLocationStateInfo;
import factor.labs.indiancalendar.CalendarDbHelper.CalendarEventMaster;
import factor.labs.indiancalendar.CalendarEventHandlers.CalendarEventInfoClass;
import factor.labs.indiancalendar.R;

/**
 * Created by hassanhussain on 7/20/2015.
 */
public class CalendarEventsForDayAdapter extends BaseAdapter {
    String sTag;
    Context mContext;
    LayoutInflater mInflater;

    List<CalendarEventMaster> mListEventsForMonth;

    public class EventDateViewHolder{
        TextView v1;
        TextView v2;
        ImageView oImg;
    }

    public CalendarEventsForDayAdapter(List<CalendarEventMaster> oDateGrid, Context oContext){
        mListEventsForMonth = oDateGrid;
        mContext = oContext;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mListEventsForMonth.size();
    }

    @Override
    public Object getItem(int position) {
        return mListEventsForMonth.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        sTag = "CalendarEventsForDayAdapter.getView";
        try {
            CalendarEventMaster oEventObj = mListEventsForMonth.get(position);
            Log.d(sTag, "on create view for position ["+position+"]");

            EventDateViewHolder oHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.layout_calendar_event_data, parent, false);
                oHolder = new EventDateViewHolder();
                oHolder.v1 = (TextView) convertView.findViewById(R.id.calendar_event_data_desc);
                oHolder.v2 = (TextView) convertView.findViewById(R.id.calendar_event_data_desc2);
                oHolder.oImg = (ImageView) convertView.findViewById(R.id.calendar_event_data_info);
                convertView.setTag(oHolder);
                Log.d(sTag, "Created new view for event.");
            }
            oHolder = (EventDateViewHolder) convertView.getTag();
            String sEventName = oEventObj.getEventName();
            oHolder.v1.setText(sEventName);
            oHolder.v2.setText(oEventObj.getDisplayName());
            Log.d(sTag, "Event display ["+ sEventName + "].");

            if(oEventObj.getCountry() == 4)
                oHolder.oImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.unitedstates));
            else if(oEventObj.getCountry() == 7)
                oHolder.oImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.india));

            return convertView;
        }
        catch(Exception exec)
        {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
        return null;
    }


}
