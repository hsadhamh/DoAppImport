package factor.labs.indiancalendar.Widget;

/**
 * Created by hassanhussain on 9/8/2016.
 */

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import java.util.ArrayList;
import java.util.List;

import factor.labs.indiancalendar.CalendarDbHelper.CalendarEventMaster;
import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;
import factor.labs.indiancalendar.R;

/**
 * If you are familiar with Adapter of ListView,this is the same as adapter
 * with few changes
 * here it now takes RemoteFetchService ArrayList<ListItem> for data
 * which is a static ArrayList
 * and this example won't work if there are multiple widgets and
 * they update at same time i.e they modify RemoteFetchService ArrayList at same
 * time.
 * For that use Database or other techniquest
 */
public class MonthEventsListFactory implements RemoteViewsFactory {
    private static final int mCount = 10;
    private List<CalendarEventMaster> mWidgetItems = new ArrayList<>();
    private Context mContext;
    private int mAppWidgetId, mMonth, mYear;

    public MonthEventsListFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        mMonth = intent.getIntExtra(EventsListWidgetProvider.CUR_MONTH, labsCalendarUtils.getCurrentMonth());
        mYear = intent.getIntExtra(EventsListWidgetProvider.CUR_YEAR, labsCalendarUtils.getCurrentYear());
    }

    public void onCreate() {
        // We sleep for 3 seconds here to show how the empty view appears in the interim.
        // The empty view is set in the StackWidgetProvider and should be a sibling of the
        // collection view.
        try {
            if(!MonthsEventsFetchService.listItemList.isEmpty()) {
                mWidgetItems.clear();
                mWidgetItems.addAll(MonthsEventsFetchService.listItemList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        // In onDestroy() you should tear down anything that was setup for your data source,
        // eg. cursors, connections, etc.
        mWidgetItems.clear();
    }

    public int getCount() {
        return mWidgetItems.size();
    }

    public RemoteViews getViewAt(int position) {
        // position will always range from 0 to getCount() - 1.

        // We construct a remote views item based on our widget item xml file, and set the
        // text based on the position.
        CalendarEventMaster master = mWidgetItems.get(position);
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_event);
        rv.setTextViewText(R.id.calendar_schedule_event_name, master.getEventName());
        rv.setTextViewText(R.id.calendar_schedule_event_desc, master.getDisplayName());
        rv.setTextViewText(R.id.calendar_schedule_date_set, "" + master.getDate());
        rv.setTextViewText(R.id.calendar_schedule_week_set,
                labsCalendarUtils.getWeekNameForDate(master.getDate(), master.getMonth(), master.getYear()));

        // Next, we set a fill-intent which will be used to fill-in the pending intent template
        // which is set on the collection view in StackWidgetProvider.
        Bundle extras = new Bundle();
        extras.putInt(EventsListWidgetProvider.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.calendar_schedule_event_info_holder, fillInIntent);
        return rv;
    }

    public RemoteViews getLoadingView() {
        // You can create a custom loading view (for instance when getViewAt() is slow.) If you
        // return null here, you will get the default loading view.
        return null;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public long getItemId(int position) {
        return position;
    }

    public boolean hasStableIds() {
        return true;
    }

    public void onDataSetChanged() {
        // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
        // on the collection view corresponding to this factory. You can do heaving lifting in
        // here, synchronously. For example, if you need to process an image, fetch something
        // from the network, etc., it is ok to do it here, synchronously. The widget will remain
        // in its current state while work is being done here, so you don't need to worry about
        // locking up the widget.
    }
}
