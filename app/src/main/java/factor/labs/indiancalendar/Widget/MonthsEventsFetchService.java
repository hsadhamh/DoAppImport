package factor.labs.indiancalendar.Widget;

/**
 * Created by hassanhussain on 9/8/2016.
 */
import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import factor.labs.indiancalendar.CalendarDbHelper.CalendarEventMaster;
import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;

public class MonthsEventsFetchService extends IntentService {

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    public static ArrayList<CalendarEventMaster> listItemList = new ArrayList<>();
    int month, year;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MonthsEventsFetchService(String name) {
        super(name);
    }

    public MonthsEventsFetchService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int currentMonth = labsCalendarUtils.getCurrentMonth();
        int currentYear = labsCalendarUtils.getCurrentYear();

        if(intent.getAction().equals(EventsListWidgetProvider.GET_EVENTS)){
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            month = intent.getIntExtra(EventsListWidgetProvider.CUR_MONTH, currentMonth);
            year = intent.getIntExtra(EventsListWidgetProvider.CUR_YEAR, currentYear);

            fetchDataFromDb(month, year);
        }
        this.stopSelf();
    }

    private void fetchDataFromDb(int mon, int yr) {
        try {
            List<CalendarEventMaster> listEvents = labsCalendarUtils
                            .getCalendarDBHandler(getApplicationContext())
                            .getHolidayReligiousEventsForMonth(mon, yr);
            if(listEvents == null) {
                Log.d("eve-widget-debug", "Total number of events returned for month [" + mon + "] Year [" + yr + "] is zero.");
            }
            else {
                listItemList.clear();
                listItemList.addAll(listEvents);
            }
        }catch(Exception exec) {
            Log.e(this.getClass().toString(), "Exception caught : " + exec.getMessage());
        }
        populateWidget();
    }

    private void populateWidget() {
        Intent widgetUpdateIntent = new Intent(this.getApplicationContext(), EventsListWidgetProvider.class);
        widgetUpdateIntent.setAction(EventsListWidgetProvider.DATA_UPDATED);
        widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        widgetUpdateIntent.putExtra(EventsListWidgetProvider.CUR_MONTH, month);
        widgetUpdateIntent.putExtra(EventsListWidgetProvider.CUR_YEAR, year);
        sendBroadcast(widgetUpdateIntent);
    }

}
