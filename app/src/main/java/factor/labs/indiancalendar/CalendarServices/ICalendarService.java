package factor.labs.indiancalendar.CalendarServices;

import android.content.Intent;

/**
 * Created by hassanhussain on 10/25/2015.
 */
public interface ICalendarService {
    void SyncLocalData(Intent intent);
    void OnSharedPreferenceChange();
}
