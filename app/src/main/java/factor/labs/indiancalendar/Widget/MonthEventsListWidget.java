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
import factor.labs.indiancalendar.R;

/**
 * Created by hassanhussain on 9/8/2016.
 */
public class MonthEventsListWidget extends AppWidgetProvider {

    public static final String TOAST_ACTION = "com.example.android.stackwidget.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.example.android.stackwidget.EXTRA_ITEM";
    public static final String DATA_UPDATED = "com.factor.DATA_UPDATED";
    public static final String NEXT_CLICK = "com.factor.NEXT_CLICK";
    public static final String PREV_CLICK = "com.factor.PREV_CLICK";
    public static final String CURRENT_CLICK = "com.factor.CURRENT_CLICK";
    public static final String CUR_MONTH = "com.factor.CUR_MONTH";
    public static final String CUR_YEAR = "com.factor.CUR_YEAR";
    public static final String GET_EVENTS = "com.factor.GET_EVENTS";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(TOAST_ACTION)) {
            Log.d("widget", "start activity");
        }
        else if(intent.getAction().equals(DATA_UPDATED)){
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int month = intent.getIntExtra(CUR_MONTH, labsCalendarUtils.getCurrentMonth());
            int year = intent.getIntExtra(CUR_YEAR, labsCalendarUtils.getCurrentYear());
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RemoteViews remoteViews = updateAppWidget(context, appWidgetId, month, year);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
        else if(intent.getAction().equals(CURRENT_CLICK)){
            Log.d("widget", "start activity");
        }
        else if(intent.getAction().equals(NEXT_CLICK)){
            Log.d("widget", "refresh next month");
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int month = intent.getIntExtra(CUR_MONTH, labsCalendarUtils.getCurrentMonth());
            int year = intent.getIntExtra(CUR_YEAR, labsCalendarUtils.getCurrentYear());

            Intent svcIntent = new Intent(context, MonthsEventsFetchService.class);
            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            svcIntent.putExtra(CUR_MONTH, month);
            svcIntent.putExtra(CUR_YEAR, year);
            svcIntent.setAction(GET_EVENTS);
            context.startService(svcIntent);
        }
        else if(intent.getAction().equals(PREV_CLICK)){
            Log.d("widget", "refresh prev month");
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int month = intent.getIntExtra(CUR_MONTH, labsCalendarUtils.getCurrentMonth());
            int year = intent.getIntExtra(CUR_YEAR, labsCalendarUtils.getCurrentYear());

            Intent svcIntent = new Intent(context, MonthsEventsFetchService.class);
            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            svcIntent.putExtra(CUR_MONTH, month);
            svcIntent.putExtra(CUR_YEAR, year);
            svcIntent.setAction(GET_EVENTS);
            context.startService(svcIntent);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        /* Start the service */
        for(int n : appWidgetIds){
            Intent svcIntent = new Intent(context, MonthsEventsFetchService.class);
            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, n);
            svcIntent.setAction(GET_EVENTS);
            context.startService(svcIntent);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    public RemoteViews updateAppWidget(Context context, int appWidgetId, int month, int year){
        Bundle bundle = new Bundle();
        bundle.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        bundle.putInt(CUR_MONTH, month);
        bundle.putInt(CUR_YEAR, year);

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

        Intent nextMonIntent = new Intent(context, MonthEventsListWidget.class);
        nextMonIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        CalendarMonthYearClass obj = new CalendarMonthYearClass(month, year);
        CalendarMonthYearClass retObj = labsCalendarUtils.addMonthYear(obj, 1);
        nextMonIntent.putExtra(CUR_MONTH,  retObj.getMonth());
        nextMonIntent.putExtra(CUR_YEAR, retObj.getYear());
        nextMonIntent.setAction(NEXT_CLICK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, nextMonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.id_wbtn_next, pendingIntent);

        Intent prevMonIntent = new Intent(context, MonthEventsListWidget.class);
        prevMonIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        CalendarMonthYearClass obj1 = new CalendarMonthYearClass(month, year);
        CalendarMonthYearClass retObj1 = labsCalendarUtils.subtractMonthYear(obj1, 1);
        prevMonIntent.putExtra(CUR_MONTH,  retObj1.getMonth());
        prevMonIntent.putExtra(CUR_YEAR, retObj1.getYear());
        prevMonIntent.setAction(PREV_CLICK);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 0, prevMonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.id_wbtn_prev, pendingIntent1);


        Intent curMonIntent = new Intent(context, MonthEventsListWidget.class);
        curMonIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        curMonIntent.putExtra(CUR_MONTH,  month);
        curMonIntent.putExtra(CUR_YEAR, year);
        curMonIntent.setAction(CURRENT_CLICK);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 0, curMonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.id_wtxt_month_name, pendingIntent2);

        // Here we setup the a pending intent template. Individuals items of a collection
        // cannot setup their own pending intents, instead, the collection as a whole can
        // setup a pending intent template, and the individual items can set a fillInIntent
        // to create unique before on an item to item basis.
        Intent toastIntent = new Intent(context, MonthEventsListWidget.class);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        toastIntent.putExtra(CUR_MONTH, month);
        toastIntent.putExtra(CUR_YEAR, year);
        toastIntent.setAction(CURRENT_CLICK);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setPendingIntentTemplate(R.id.id_list_events_month, toastPendingIntent);

        return rv;
    }
}
