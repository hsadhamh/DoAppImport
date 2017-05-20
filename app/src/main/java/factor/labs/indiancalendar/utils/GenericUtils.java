package factor.labs.indiancalendar.utils;

import android.content.Context;

import com.arasthel.asyncjob.AsyncJob;
import com.orhanobut.logger.Logger;

import factor.labs.indiancalendar.DayOnApp;
import factor.labs.indiancalendar.utils.database.Events;
import factor.labs.indiancalendar.utils.database.EventsDao;
import factor.labs.indiancalendar.utils.json.Event;
import factor.labs.indiancalendar.utils.json.EventList;

/**
 * Created by hassanhussain on 4/29/2017.
 */

public class GenericUtils {
    public static void startAsyncDbSynchronization(final boolean bReCreatedDB, final Context context){

        AsyncJob.OnBackgroundJob job = new AsyncJob.OnBackgroundJob() {
            @Override
            public void doOnBackground() {
                updateProgress(0);

                if(bReCreatedDB){ } //  TODO: Re-Create DBs.
                try {
                    String sJsonData = FileReader.readData(context, FileReader.FILE_DATA_ENCRYPTED);
                    String sJsonDecrypted = Defense.getInstance().decryptData(sJsonData);
                    EventList listEvents = (EventList) JsonSerializer.getInstance().UnserializeToObject(sJsonDecrypted, EventList.class);
                    Thread.sleep(1000);

                    updateProgress(5);
                    EventsDao newEvent = DayOnApp.getDaoSession().getEventsDao();

                    int total = listEvents.events.size();
                    int incr = 1;

                    for(Event event : listEvents.events){
                        //  TODO: insert to table.
                        Events eventInfo = new Events();
                        eventInfo.setGUID(event.getEuid());
                        eventInfo.setCategory(event.getCategory());
                        eventInfo.setFlags(event.getFlags());

                        eventInfo.setLocations(JsonSerializer.getInstance().SerializeToString(event.getLocations(), false));

                        eventInfo.setName(event.getName());
                        eventInfo.setProperty(JsonSerializer.getInstance().SerializeToString(event.getProperty(), false));
                        eventInfo.setSub_category(event.getSub_category());
                        eventInfo.setTags("");

                        newEvent.insert(eventInfo);
                        Logger.d("Inserted information [%s].", eventInfo.getName());
                        Logger.d(eventInfo);

                        updateProgress(5 + Math.abs((incr/total)*95));
                        ++incr;
                    }

                } catch (Exception e) {
                    Logger.e(e, "Exception on DB initialize and app cannot work.");
                }

                updateProgress(100);
            }

            public void updateProgress(final int action) {
                // This toast should show a difference of 1000ms between calls
                AsyncJob.doOnMainThread(new AsyncJob.OnMainThreadJob() {
                    @Override
                    public void doInUIThread() {
                        if(action == 0) {
                            //  TODO: show dialog
                        }
                        else if(action > 0 && action < 99) {
                            //  TODO: update progress
                        }
                        else if(action > 99) {
                            //  TODO: close dialog
                        }
                    }
                });
            }

            public void cancel(){ }
        };

        //  start the thread.
        job.doOnBackground();

    }
}
