package factor.labs.indiancalendar.Widget;

/**
 * Created by hassanhussain on 9/8/2016.
 */
import android.content.Intent;
import android.widget.RemoteViewsService;

public class MonthDateWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new MonthGridWeekFactory(this.getApplicationContext(), intent));
    }
}