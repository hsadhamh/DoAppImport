package factor.labs.indiancalendar.CalendarAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;
import factor.labs.indiancalendar.R;

/**
 * Created by hassanhussain on 7/19/2015.
 */
public class CalendarWeekNameDisplayAdapter extends BaseAdapter {

    String[] weekNamesInShort = labsCalendarUtils.getArrayShortWeekNames();

    LayoutInflater mLayoutInflate;
    Context mContext;

    String sTag = "";

    public CalendarWeekNameDisplayAdapter(Context oContext)
    {
        mContext = oContext;
        mLayoutInflate = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return weekNamesInShort.length;
    }

    @Override
    public Object getItem(int position) {
        return weekNamesInShort[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        sTag = "CalendarWeekNameDisplayAdapter.getView";
        try {
            String sWeekName = weekNamesInShort[position];
            CalendarWeekNameDisplayHolder holderView = null;

            if (convertView == null) {
                convertView = mLayoutInflate.inflate(R.layout.layout_calendar_week_name, parent, false);

                holderView = new CalendarWeekNameDisplayHolder();
                holderView.oTextView = (TextView) convertView.findViewById(R.id.calendar_week_name_display);

                convertView.setTag(holderView);
            }
            holderView = (CalendarWeekNameDisplayHolder) convertView.getTag();
            holderView.oTextView.setText(sWeekName);

            Log.d(sTag, "Created view for week name : " + sWeekName);

            return convertView;
        }
        catch(Exception exec)
        {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
        return null;
    }

    class CalendarWeekNameDisplayHolder{
        TextView oTextView;
    }
}
