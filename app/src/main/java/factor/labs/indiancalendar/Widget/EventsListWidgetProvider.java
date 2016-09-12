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
 * Created by hassanhussain on 9/8/2016.
 */
public class EventsListWidgetProvider extends AppWidgetProvider {

    public static final String TOAST_ACTION = "com.factor.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.factor.EXTRA_ITEM";
    public static final String DATA_UPDATED = "com.factor.DATA_UPDATED";
    public static final String NEXT_CLICK = "com.factor.NEXT_CLICK";
    public static final String PREV_CLICK = "com.factor.PREV_CLICK";
    public static final String CURRENT_CLICK = "com.factor.CURRENT_CLICK";
    public static final String CUR_MONTH = "com.factor.CUR_MONTH";
    public static final String CUR_YEAR = "com.factor.CUR_YEAR";
    public static final String GET_EVENTS = "com.factor.GET_EVENTS";
    public static final String START_ACTIVITY = "com.factor.START_ACTIVITY";
    public static final String WIDGET_TYPE = "com.factor.WIDGET_TYPE";
    public static final String MONTH_GRID_GET_DATES = "com.factor.MONTH_GRID_GET_DATES";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) { super.onDeleted(context, appWidgetIds); }

    @Override
    public void onDisabled(Context context) { super.onDisabled(context); }

    @Override
    public void onEnabled(Context context) { super.onEnabled(context); }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("eve-widget-debug", "On Update");
        /* Start the service */
        for(int n : appWidgetIds){
            startServiceToUpdate(context, n, labsCalendarUtils.getCurrentMonth(), labsCalendarUtils.getCurrentYear(), false);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    public Bundle getBundle(int appWidgetId, int month, int year){
        Bundle bundle = new Bundle();
        bundle.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        bundle.putInt(CUR_MONTH, month);
        bundle.putInt(CUR_YEAR, year);
        return bundle;
    }

    public RemoteViews updateAppWidget(Context context, int appWidgetId, int month, int year){
        Bundle bundle = getBundle(appWidgetId, month, year);

        // Here we setup the intent which points to the StackViewService which will
        // provide the views for this collection.
        Intent intent = new Intent(context, MonthEventsWidgetService.class);
        intent.putExtras(bundle);

        // When intents are compared, the extras are ignored, so we need to embed the extras
        // into the data so that the extras will not be ignored.
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.month_events_widget_layout);
        rv.setRemoteAdapter(R.id.id_list_events_month, intent);
        rv.setTextViewText(R.id.id_wtxt_month_name, labsCalendarUtils.getMonthName(month) + " " + year);

        // The empty view is displayed when the collection has no items. It should be a sibling
        // of the collection view.
        rv.setEmptyView(R.id.id_list_events_month, R.id.empty_view);

        CalendarMonthYearClass obj = new CalendarMonthYearClass(month, year);

        CalendarMonthYearClass retObj = labsCalendarUtils.addMonthYear(obj, 1);
        PendingIntent nextPendingIntent = getPendingIntent(context, appWidgetId, retObj.getMonth(), retObj.getYear(), 2);
        rv.setOnClickPendingIntent(R.id.id_wbtn_next, nextPendingIntent);

        retObj = labsCalendarUtils.subtractMonthYear(obj, 1);
        PendingIntent prevPendingIntent = getPendingIntent(context, appWidgetId, retObj.getMonth(), retObj.getYear(), 1);
        rv.setOnClickPendingIntent(R.id.id_wbtn_prev, prevPendingIntent);

        PendingIntent currPendingIntent = getPendingIntent(context, appWidgetId, month, year, 0);
        rv.setOnClickPendingIntent(R.id.id_wtxt_month_name, currPendingIntent);

        // Here we setup the a pending intent template. Individuals items of a collection
        // cannot setup their own pending intents, instead, the collection as a whole can
        // setup a pending intent template, and the individual items can set a fillInIntent
        // to create unique before on an item to item basis.
        PendingIntent toastPendingIntent = getPendingIntent(context, appWidgetId, month, year, 0);
        rv.setPendingIntentTemplate(R.id.id_list_events_month, toastPendingIntent);

        return rv;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("eve-widget-debug", "On Receive : " + ((intent != null)? intent.getAction() : ""));
        if(intent.getAction().equals(DATA_UPDATED)){
            UpdateDataToWidget(context, intent);
        }
        else if(intent.getAction().equals(CURRENT_CLICK)){
            startActivityOnClick(context, intent);
        }
        else if(intent.getAction().equals(NEXT_CLICK)){
            startServiceToUpdateWidget(context, intent, true);
        }
        else if(intent.getAction().equals(PREV_CLICK)){
            startServiceToUpdateWidget(context, intent, true);
        }
    }

    public void startServiceToUpdateWidget(Context context, Intent intent, boolean addMonYr){
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        int month = intent.getIntExtra(CUR_MONTH, labsCalendarUtils.getCurrentMonth());
        int year = intent.getIntExtra(CUR_YEAR, labsCalendarUtils.getCurrentYear());
        startServiceToUpdate(context, appWidgetId, month, year, addMonYr);
    }

    public void startServiceToUpdate(Context context, int appWidgetId, int month, int year, boolean addMonYr){
        Intent svcIntent = new Intent(context, MonthsEventsFetchService.class);
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        if(addMonYr) {
            svcIntent.putExtra(CUR_MONTH, month);
            svcIntent.putExtra(CUR_YEAR, year);
        }
        svcIntent.setAction(GET_EVENTS);
        context.startService(svcIntent);
    }

    public void startActivityOnClick(Context context, Intent intent){
        int month = intent.getIntExtra(CUR_MONTH, labsCalendarUtils.getCurrentMonth());
        int year = intent.getIntExtra(CUR_YEAR, labsCalendarUtils.getCurrentYear());

        Intent svcIntent = new Intent(context, DayOnMonthHomeActivity.class);
        svcIntent.putExtra(CUR_MONTH, month);
        svcIntent.putExtra(CUR_YEAR, year);
        svcIntent.setAction(START_ACTIVITY);
        svcIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(svcIntent);
    }

    public void UpdateDataToWidget(Context context, Intent intent){
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        int month = intent.getIntExtra(CUR_MONTH, labsCalendarUtils.getCurrentMonth());
        int year = intent.getIntExtra(CUR_YEAR, labsCalendarUtils.getCurrentYear());

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, EventsListWidgetProvider.class));
        RemoteViews remoteViews = updateAppWidget(context, appWidgetId, month, year);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    public PendingIntent getPendingIntent(Context context, int appWidgetId, int month, int year, int action){
        Intent monIntent = new Intent(context, EventsListWidgetProvider.class);
        monIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        monIntent.putExtra(CUR_MONTH,  month);
        monIntent.putExtra(CUR_YEAR, year);

        switch(action)
        {
            case 0:
                monIntent.setAction(CURRENT_CLICK);
                break;
            case 1:
                monIntent.setAction(PREV_CLICK);
                break;
            case 2:
                monIntent.setAction(NEXT_CLICK);
                break;
        }
        return PendingIntent.getBroadcast(context, 0, monIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
