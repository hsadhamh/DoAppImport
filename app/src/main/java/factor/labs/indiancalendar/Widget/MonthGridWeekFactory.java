package factor.labs.indiancalendar.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import factor.labs.indiancalendar.CalendarUtils.CalendarDateClass;
import factor.labs.indiancalendar.CalendarUtils.CalendarMonthClass;
import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;
import factor.labs.indiancalendar.R;


/**
 * Created by AHAMED on 9/10/2016.
 */
public class MonthGridWeekFactory implements RemoteViewsService.RemoteViewsFactory {

    String[] weekNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    private Context mContext = null;
    private int mAppWidgetId, mMonth, mYear;
    private CalendarMonthClass mMonthClass;

    @Override
    public void onCreate() {
        mMonthClass = MonthDateEventsFetchService.monthClass;
    }

    public MonthGridWeekFactory(Context context, Intent intent) {
        this.mContext = context;
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        mMonth = intent.getIntExtra(EventsListWidgetProvider.CUR_MONTH, labsCalendarUtils.getCurrentMonth());
        mYear = intent.getIntExtra(EventsListWidgetProvider.CUR_YEAR, labsCalendarUtils.getCurrentYear());
    }

    @Override
    public void onDataSetChanged() { }

    @Override
    public void onDestroy() { }

    @Override
    public int getCount() {
        return (weekNames.length +mMonthClass.getDateForGrid().size() );
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_layout_week_list);
        if(position >= 0 && position <= 6){
            rv.setTextViewText(R.id.calendar_week_name_display1, weekNames[position]);
            rv.setFloat(R.id.calendar_week_name_display1, "setTextSize", 11.0f);
        }
        else
        {
            CalendarDateClass date = mMonthClass.getDateForGrid().get(position - 7);
            /*
            if((date.getEventsForDay().size() > 0) && date.isCurrentMonthDate())
                rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_layout_day_events);
            */

            if((date.getEventsForDay().size() > 0) && date.isCurrentMonthDate())
                // RGB(250, 128, 114)
                rv.setTextColor(R.id.calendar_week_name_display1, Color.rgb(255, 64, 133));


            rv.setTextViewText(R.id.calendar_week_name_display1, "" + date.getDate());
            if (date.isNextMonthDate() || date.isPreviousMonthDate())
                rv.setTextColor(R.id.calendar_week_name_display1, Color.GRAY);

            // Next, set a fill-intent, which will be used to fill in the pending intent template
            // that is set on the collection view in StackWidgetProvider.

            Bundle extras = new Bundle();
            extras.putInt(MonthGridWidgetProvider.EXTRA_WORD, position);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            // Make it possible to distinguish the individual on-click
            // action of a given item
            rv.setOnClickFillInIntent(R.id.calendar_week_name_display1, fillInIntent);
        }
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() { return null; }

    @Override
    public int getViewTypeCount() { return 1; }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
