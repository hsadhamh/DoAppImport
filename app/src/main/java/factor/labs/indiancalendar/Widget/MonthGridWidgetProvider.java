package factor.labs.indiancalendar.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;


import factor.labs.indiancalendar.CalendarObjects.CalendarMonthYearClass;
import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;
import factor.labs.indiancalendar.DayOnMonthHomeActivity;
import factor.labs.indiancalendar.R;

/**
 * Created by AHAMED on 9/10/2016.
 */
public class MonthGridWidgetProvider extends AppWidgetProvider {
    public static String EXTRA_WORD = "factor.labs.indiancalendar.WORD";
    public static final String TOAST_ACTION = "factor.labs.indiancalendar.TOAST_ACTION";
    public static String NAV_CLICK_NEXT = "factor.labs.indiancalendar.ACTION_NEXT_MONTH";
    public static String NAV_CLICK_PREV = "factor.labs.indiancalendar.ACTION_PREV_MONTH";
    public static String NAV_CLICK_CURR = "factor.labs.indiancalendar.ACTION_CURRENT_MONTH";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        /* Start the service */
        for(int n : appWidgetIds){
            Intent svcIntent = new Intent(context, MonthsEventsFetchService.class);
            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, n);
            svcIntent.setAction(EventsListWidgetProvider.MONTH_GRID_GET_DATES);
            context.startService(svcIntent);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    public RemoteViews updateAppWidget(Context context, int appWidgetId, int month, int year){
        Bundle bundle = new Bundle();
        bundle.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        bundle.putInt(EventsListWidgetProvider.CUR_MONTH, month);
        bundle.putInt(EventsListWidgetProvider.CUR_YEAR, year);

        Intent svcIntent = new Intent(context, MonthEventsWidgetService.class);
        svcIntent.putExtras(bundle);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        svcIntent.putExtra(EventsListWidgetProvider.WIDGET_TYPE, 1);

        RemoteViews rvWidgetMain = new RemoteViews(context.getPackageName(), R.layout.widget_main_4x3);
        rvWidgetMain.setTextViewText(R.id.monthyear, labsCalendarUtils.getMonthName(month) + " " + year);
        //setting adapter to listview of the widget
        rvWidgetMain.setRemoteAdapter(R.id.id_widget_gridview, svcIntent);
        rvWidgetMain.setEmptyView(R.id.id_widget_gridview, R.id.empty_view);

        Intent prevIntent = new Intent(context, MonthGridWidgetProvider.class);
        CalendarMonthYearClass obj = new CalendarMonthYearClass(month, year);
        CalendarMonthYearClass retObj = labsCalendarUtils.subtractMonthYear(obj, 1);
        prevIntent.putExtra(EventsListWidgetProvider.CUR_MONTH,  retObj.getMonth());
        prevIntent.putExtra(EventsListWidgetProvider.CUR_YEAR, retObj.getYear());
        prevIntent.setAction(MonthGridWidgetProvider.NAV_CLICK_PREV);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        rvWidgetMain.setOnClickPendingIntent(R.id.prevmonth, pendingIntent);

        Intent nextIntent = new Intent(context, MonthGridWidgetProvider.class);
        CalendarMonthYearClass obj1 = new CalendarMonthYearClass(month, year);
        CalendarMonthYearClass retObj1 = labsCalendarUtils.addMonthYear(obj1, 1);
        nextIntent.putExtra(EventsListWidgetProvider.CUR_MONTH,  retObj1.getMonth());
        nextIntent.putExtra(EventsListWidgetProvider.CUR_YEAR, retObj1.getYear());
        nextIntent.setAction(MonthGridWidgetProvider.NAV_CLICK_NEXT);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        rvWidgetMain.setOnClickPendingIntent(R.id.nextmonth, pendingIntent1);

        Intent toastIntent = new Intent(context, MonthGridWidgetProvider.class);
        toastIntent.putExtras(bundle);
        toastIntent.setAction(MonthGridWidgetProvider.NAV_CLICK_CURR);
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        rvWidgetMain.setPendingIntentTemplate(R.id.id_widget_gridview, toastPendingIntent);

        return rvWidgetMain;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(TOAST_ACTION)) {
            Log.d("widget", "start activity");
        }
        else if(intent.getAction().equals(EventsListWidgetProvider.DATA_UPDATED)){
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            int month = intent.getIntExtra(EventsListWidgetProvider.CUR_MONTH, labsCalendarUtils.getCurrentMonth());
            int year = intent.getIntExtra(EventsListWidgetProvider.CUR_YEAR, labsCalendarUtils.getCurrentYear());
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RemoteViews remoteViews = updateAppWidget(context, appWidgetId, month, year);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.id_widget_gridview);
        }
        else if(intent.getAction().equals(NAV_CLICK_CURR)){
            Log.d("widget", "start activity");
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int month = intent.getIntExtra(EventsListWidgetProvider.CUR_MONTH, labsCalendarUtils.getCurrentMonth());
            int year = intent.getIntExtra(EventsListWidgetProvider.CUR_YEAR, labsCalendarUtils.getCurrentYear());

            Intent svcIntent = new Intent(context, DayOnMonthHomeActivity.class);
            svcIntent.putExtra(EventsListWidgetProvider.CUR_MONTH, month);
            svcIntent.putExtra(EventsListWidgetProvider.CUR_YEAR, year);
            svcIntent.setAction(EventsListWidgetProvider.START_ACTIVITY);
            svcIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(svcIntent);
        }
        else if(intent.getAction().equals(NAV_CLICK_NEXT)){
            Log.d("widget", "refresh next month");
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int month = intent.getIntExtra(EventsListWidgetProvider.CUR_MONTH, labsCalendarUtils.getCurrentMonth());
            int year = intent.getIntExtra(EventsListWidgetProvider.CUR_YEAR, labsCalendarUtils.getCurrentYear());

            Intent svcIntent = new Intent(context, MonthsEventsFetchService.class);
            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            svcIntent.putExtra(EventsListWidgetProvider.CUR_MONTH, month);
            svcIntent.putExtra(EventsListWidgetProvider.CUR_YEAR, year);
            svcIntent.setAction(EventsListWidgetProvider.MONTH_GRID_GET_DATES);
            context.startService(svcIntent);
        }
        else if(intent.getAction().equals(NAV_CLICK_PREV)){
            Log.d("widget", "refresh prev month");
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int month = intent.getIntExtra(EventsListWidgetProvider.CUR_MONTH, labsCalendarUtils.getCurrentMonth());
            int year = intent.getIntExtra(EventsListWidgetProvider.CUR_YEAR, labsCalendarUtils.getCurrentYear());

            Intent svcIntent = new Intent(context, MonthsEventsFetchService.class);
            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            svcIntent.putExtra(EventsListWidgetProvider.CUR_MONTH, month);
            svcIntent.putExtra(EventsListWidgetProvider.CUR_YEAR, year);
            svcIntent.setAction(EventsListWidgetProvider.MONTH_GRID_GET_DATES);
            context.startService(svcIntent);
        }
        super.onReceive(context, intent);
    }
}
