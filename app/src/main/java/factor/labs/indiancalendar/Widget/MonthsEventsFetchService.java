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
    public static ArrayList<CalendarEventMaster> listItemList;
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
        if(intent.getAction().equals(MonthEventsListWidget.GET_EVENTS)){
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int currentMonth = labsCalendarUtils.getCurrentMonth();
            int currentYear = labsCalendarUtils.getCurrentYear();
            month = intent.getIntExtra(MonthEventsListWidget.CUR_MONTH, currentMonth);
            year = intent.getIntExtra(MonthEventsListWidget.CUR_YEAR, currentYear);
            fetchDataFromDb(month, year);
        }
        this.stopSelf();
    }

    private void fetchDataFromDb(int mon, int yr) {
        listItemList = new ArrayList<>();
        try {
            List<CalendarEventMaster> listEvents = labsCalendarUtils
                            .getCalendarDBHandler(getApplicationContext())
                            .getHolidayReligiousEventsForMonth(mon, yr);
            if(listEvents == null || listEvents.size() == 0) {
                Log.w(this.getClass().toString(), "Total number of events returned for month [" + mon + "] Year [" + yr + "] is zero.");
            }
            else
                listItemList.addAll(listEvents);
        }catch(Exception exec) {
            Log.e(this.getClass().toString(), "Exception caught : " + exec.getMessage());
        }
        populateWidget();
    }

    private void populateWidget() {
        Intent widgetUpdateIntent = new Intent(this.getApplicationContext(), MonthEventsListWidget.class);
        widgetUpdateIntent.setAction(MonthEventsListWidget.DATA_UPDATED);
        widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        widgetUpdateIntent.putExtra(MonthEventsListWidget.CUR_MONTH, month);
        widgetUpdateIntent.putExtra(MonthEventsListWidget.CUR_YEAR, year);
        sendBroadcast(widgetUpdateIntent);
    }
}
