package factor.labs.indiancalendar;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by AHAMED on 9/10/2016.
 */
public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new WidgetViewFactory(this.getApplicationContext(),
                intent));
    }
}
