package factor.labs.indiancalendar.CalendarAdapters;

import android.content.Context;
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
import factor.labs.indiancalendar.R;

/**
 * Created by hassanhussain on 7/22/2015.
 */
public class CalendarEventsForMonthAdapter extends BaseAdapter {
    List<CalendarEventMaster> mListDateClass = null;

    Context mContext;
    LayoutInflater mInflater;

    String sTag;

    public class CalendarEventForMonthHolder
    {
        TextView oTxt;
        TextView oTxt1;
        TextView oTxt2;
        ImageView oImg;
    }

    public CalendarEventsForMonthAdapter(List<CalendarEventMaster> oDateGrid, Context oContext){
        mListDateClass = oDateGrid;
        mContext = oContext;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(mListDateClass == null) return 0;
        return mListDateClass.size();
    }

    @Override
    public Object getItem(int position) {
        if(mListDateClass == null) return null;
        return mListDateClass.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        sTag = "CalendarEventsForMonthAdapter.getView";
        try
        {
            CalendarEventMaster oEvent = mListDateClass.get(position);

            Log.d(sTag, "getView called for postion ["+ position +"].");
            String sDate = oEvent.getDateString();
            sDate = sDate.substring(0, sDate.indexOf('-')).trim();
            Log.d(sTag, "set date ["+ sDate +"] for postion ["+ position +"].");
            CalendarEventForMonthHolder oHolder;
            if(convertView == null)
            {
                Log.d(sTag, "create view for postion : " + position);
                convertView = mInflater.inflate(R.layout.layout_calendar_event_full_month, parent, false);
                oHolder = new CalendarEventForMonthHolder();

                oHolder.oTxt = (TextView)convertView.findViewById(R.id.calendar_event_full_date);
                oHolder.oTxt1 = (TextView)convertView.findViewById(R.id.calendar_event_full_desc);
                oHolder.oTxt2 = (TextView)convertView.findViewById(R.id.calendar_event_full_desc2);
                oHolder.oImg = (ImageView)convertView.findViewById(R.id.calendar_event_full_info);

                convertView.setTag(oHolder);
            }
            Log.d(sTag, "Set properties for the view position : " + position);

            oHolder = (CalendarEventForMonthHolder)convertView.getTag();
            oHolder.oTxt.setText(sDate);
            oHolder.oTxt1.setText(oEvent.getEventName());
            oHolder.oTxt2.setText(oEvent.getDisplayName());

            if(oEvent.getCountry() == 4)
                oHolder.oImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.unitedstates));
            else if(oEvent.getCountry() == 7)
                oHolder.oImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.india));

            return convertView;
        }
        catch (Exception exec)
        {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
        return null;
    }


}
