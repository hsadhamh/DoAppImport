package factor.labs.indiancalendar.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
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
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d("grid-widget-debug", "On enabled");
        AppWidgetManager appWidgetMgr = AppWidgetManager.getInstance(context);
        int[] widgets = appWidgetMgr.getAppWidgetIds(new ComponentName(context, MonthGridWidgetProvider.class));
        for(int widget : widgets ){
            startServiceToUpdate(context, widget,
                    labsCalendarUtils.getCurrentMonth(), labsCalendarUtils.getCurrentYear(), false);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("grid-widget-debug", "On Update");
        /* Start the service */
        /*for(int n : appWidgetIds){
            startServiceToUpdate(context, n, labsCalendarUtils.getCurrentMonth(), labsCalendarUtils.getCurrentYear(), false);
        }*/
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    public RemoteViews updateAppWidget(Context context, int appWidgetId, int month, int year){
        Bundle bundle = getBundle(appWidgetId, month, year);

        Intent svcIntent = new Intent(context, MonthDateWidgetService.class);
        svcIntent.putExtras(bundle);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews rvWidgetMain = new RemoteViews(context.getPackageName(), R.layout.widget_main_4x3);
        rvWidgetMain.setTextViewText(R.id.monthyear, labsCalendarUtils.getMonthName(month) + " " + year);
        //  setting adapter to list-view of the widget
        rvWidgetMain.setRemoteAdapter(R.id.id_widget_gridview, svcIntent);
        rvWidgetMain.setEmptyView(R.id.id_widget_gridview, R.id.empty_view);

        CalendarMonthYearClass obj = new CalendarMonthYearClass(month, year);

        CalendarMonthYearClass retObj = labsCalendarUtils.subtractMonthYear(obj, 1);
        PendingIntent prevPendingIntent = getPendingIntent(context, appWidgetId, retObj.getMonth(), retObj.getYear(), 1);
        rvWidgetMain.setOnClickPendingIntent(R.id.prevmonth, prevPendingIntent);

        retObj = labsCalendarUtils.addMonthYear(obj, 1);
        PendingIntent pendingIntent1 = getPendingIntent(context, appWidgetId, retObj.getMonth(), retObj.getYear(), 2);
        rvWidgetMain.setOnClickPendingIntent(R.id.nextmonth, pendingIntent1);

        PendingIntent toastPendingIntent = getPendingIntent(context, appWidgetId, month, year, 0);
        rvWidgetMain.setPendingIntentTemplate(R.id.id_widget_gridview, toastPendingIntent);

        return rvWidgetMain;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("grid-widget-debug", "On Receive : " + ((intent != null)? intent.getAction() : ""));
        if(intent.getAction().equals(EventsListWidgetProvider.DATA_UPDATED)){
            UpdateDataToWidget(context, intent);
        }
        else if(intent.getAction().equals(NAV_CLICK_CURR)){
            startActivityOnClick(context, intent);
        }
        else if(intent.getAction().equals(NAV_CLICK_NEXT)){
            startServiceToUpdateWidget(context, intent, true);
        }
        else if(intent.getAction().equals(NAV_CLICK_PREV)){
            startServiceToUpdateWidget(context, intent, true);
        }
    }

    public Bundle getBundle(int appWidgetId, int month, int year){
        Bundle bundle = new Bundle();
        bundle.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        bundle.putInt(EventsListWidgetProvider.CUR_MONTH, month);
        bundle.putInt(EventsListWidgetProvider.CUR_YEAR, year);
        return bundle;
    }

    public void startServiceToUpdateWidget(Context context, Intent intent, boolean addMonYr){
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        int month = intent.getIntExtra(EventsListWidgetProvider.CUR_MONTH, labsCalendarUtils.getCurrentMonth());
        int year = intent.getIntExtra(EventsListWidgetProvider.CUR_YEAR, labsCalendarUtils.getCurrentYear());
        startServiceToUpdate(context, appWidgetId, month, year, addMonYr);
    }

    public void startServiceToUpdate(Context context, int appWidgetId, int month, int year, boolean addMonYr){
        Intent svcIntent = new Intent(context, MonthDateEventsFetchService.class);
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        if(addMonYr) {
            svcIntent.putExtra(EventsListWidgetProvider.CUR_MONTH, month);
            svcIntent.putExtra(EventsListWidgetProvider.CUR_YEAR, year);
        }
        svcIntent.setAction(EventsListWidgetProvider.MONTH_GRID_GET_DATES);
        context.startService(svcIntent);
    }

    public void startActivityOnClick(Context context, Intent intent){
        int month = intent.getIntExtra(EventsListWidgetProvider.CUR_MONTH, labsCalendarUtils.getCurrentMonth());
        int year = intent.getIntExtra(EventsListWidgetProvider.CUR_YEAR, labsCalendarUtils.getCurrentYear());

        Intent svcIntent = new Intent(context, DayOnMonthHomeActivity.class);
        svcIntent.putExtra(EventsListWidgetProvider.CUR_MONTH, month);
        svcIntent.putExtra(EventsListWidgetProvider.CUR_YEAR, year);
        svcIntent.setAction(EventsListWidgetProvider.START_ACTIVITY);
        svcIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(svcIntent);
    }

    public void UpdateDataToWidget(Context context, Intent intent){
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        int month = intent.getIntExtra(EventsListWidgetProvider.CUR_MONTH, labsCalendarUtils.getCurrentMonth());
        int year = intent.getIntExtra(EventsListWidgetProvider.CUR_YEAR, labsCalendarUtils.getCurrentYear());

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = updateAppWidget(context, appWidgetId, month, year);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, MonthGridWidgetProvider.class));
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    public PendingIntent getPendingIntent(Context context, int appWidgetId, int month, int year, int action){
        Intent monIntent = new Intent(context, MonthGridWidgetProvider.class);
        monIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        monIntent.putExtra(EventsListWidgetProvider.CUR_MONTH,  month);
        monIntent.putExtra(EventsListWidgetProvider.CUR_YEAR, year);
        switch(action){
            case 0:
                monIntent.setAction(MonthGridWidgetProvider.NAV_CLICK_CURR);
                break;
            case 1:
                monIntent.setAction(MonthGridWidgetProvider.NAV_CLICK_PREV);
                break;
            case 2:
                monIntent.setAction(MonthGridWidgetProvider.NAV_CLICK_NEXT);
                break;
        }
        return PendingIntent.getBroadcast(context, 0, monIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
