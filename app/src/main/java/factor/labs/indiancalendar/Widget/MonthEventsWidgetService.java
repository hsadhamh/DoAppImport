package factor.labs.indiancalendar.Widget;

/**
 * Created by hassanhussain on 9/8/2016.
 */
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;

public class MonthEventsWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        return (new MonthEventsDataProvider(this.getApplicationContext(), intent));
    }
}