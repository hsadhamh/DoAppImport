package factor.labs.indiancalendar;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;


import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;

/**
 * Created by AHAMED on 9/10/2016.
 */
public class CalendarWidgetProvider extends AppWidgetProvider {
    public static String EXTRA_WORD = "factor.labs.indiancalendar.WORD";
    public static final String TOAST_ACTION = "factor.labs.indiancalendar.TOAST_ACTION";
    public static String NAV_CLICK_NEXT = "factor.labs.indiancalendar.ACTION_NEXT_MONTH";
    public static String NAV_CLICK_PREV = "factor.labs.indiancalendar.ACTION_PREV_MONTH";
    public static String NAV_CLICK_CURR = "factor.labs.indiancalendar.ACTION_CURRENT_MONTH";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {


        for (int i = 0; i < appWidgetIds.length; i++) {

            Intent svcIntent = new Intent(context, WidgetService.class);
            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews rvWidgetMain = new RemoteViews(context.getPackageName(), R.layout.widget_main_4x3);
            //Setting Month
            rvWidgetMain.setTextViewText(R.id.monthyear, labsCalendarUtils.getMonthName(labsCalendarUtils.getCurrentMonth()) + " " + labsCalendarUtils.getCurrentYear());
            //setting adapter to listview of the widget
            rvWidgetMain.setRemoteAdapter(R.id.id_widget_gridview, svcIntent);


            Intent toastIntent = new Intent(context, CalendarWidgetProvider.class);
            // Set the action for the intent.
            // When the user touches a particular view, it will have the effect of
            // broadcasting TOAST_ACTION.
            toastIntent.setAction(CalendarWidgetProvider.TOAST_ACTION);
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            rvWidgetMain.setPendingIntentTemplate(R.id.id_widget_gridview, toastPendingIntent);


            appWidgetManager.updateAppWidget(appWidgetIds[i], rvWidgetMain);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
