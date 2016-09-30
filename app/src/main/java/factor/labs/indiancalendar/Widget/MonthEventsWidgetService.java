package factor.labs.indiancalendar.Widget;

/**
 * Created by hassanhussain on 9/8/2016.
 */
import android.content.Intent;
import android.widget.RemoteViewsService;

public class MonthEventsWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        MonthEventsListFactory lstFactory = new MonthEventsListFactory(this.getApplicationContext(), intent);
        return lstFactory;
    }
}