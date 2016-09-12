package factor.labs.indiancalendar.Widget;

/**
 * Created by hassanhussain on 9/8/2016.
 */
import android.content.Intent;
import android.widget.RemoteViewsService;

public class MonthEventsWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int widgetType = intent.getIntExtra(EventsListWidgetProvider.WIDGET_TYPE, 0);
        if(widgetType == 1)
            return(new MonthGridWeekFactory(this.getApplicationContext(), intent));
        else
            return (new MonthEventsListFactory(this.getApplicationContext(), intent));
    }
}