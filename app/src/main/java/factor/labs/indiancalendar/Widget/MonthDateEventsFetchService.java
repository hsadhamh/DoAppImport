package factor.labs.indiancalendar.Widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.util.Log;

import factor.labs.indiancalendar.CalendarUtils.CalendarMonthClass;
import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;

/**
 * Created by hassanhussain on 9/12/2016.
 */
public class MonthDateEventsFetchService extends IntentService {
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    public static CalendarMonthClass monthClass;
    int month, year;

    public MonthDateEventsFetchService(String name) {
        super(name);
    }

    public MonthDateEventsFetchService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int currentMonth = labsCalendarUtils.getCurrentMonth();
        int currentYear = labsCalendarUtils.getCurrentYear();
        if(intent.getAction().equals(EventsListWidgetProvider.MONTH_GRID_GET_DATES)){
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            month = intent.getIntExtra(EventsListWidgetProvider.CUR_MONTH, currentMonth);
            year = intent.getIntExtra(EventsListWidgetProvider.CUR_YEAR, currentYear);

            fetchDateForMonth(month, year);
        }
        this.stopSelf();
    }

    private void fetchDateForMonth(int mon, int yr) {
        try {
            monthClass = new CalendarMonthClass(mon, yr);
            monthClass.setmContext(getApplicationContext());
            monthClass.prepareCalendarMonthDates();
            monthClass.getEventsForMonth();
        }catch(Exception exec) {
            Log.e(this.getClass().toString(), "Exception caught : " + exec.getMessage());
        }
        populateMonthGridWidget();
    }

    private void populateMonthGridWidget(){
        Intent widgetUpdateIntent = new Intent(this.getApplicationContext(), MonthGridWidgetProvider.class);
        widgetUpdateIntent.setAction(EventsListWidgetProvider.DATA_UPDATED);
        widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        widgetUpdateIntent.putExtra(EventsListWidgetProvider.CUR_MONTH, month);
        widgetUpdateIntent.putExtra(EventsListWidgetProvider.CUR_YEAR, year);
        sendBroadcast(widgetUpdateIntent);
    }
}
