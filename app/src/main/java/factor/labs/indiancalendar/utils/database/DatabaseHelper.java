package factor.labs.indiancalendar.utils.database;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import factor.labs.indiancalendar.DayOnApp;

/**
 * Created by hassanhussain on 5/27/2017.
 */

public class DatabaseHelper {

    public static List<Events> getEventsForGivenTimeFrame(long start, long end){
        QueryBuilder oBuilderQuery =  DayOnApp.getDaoSession().getEventsDao().queryBuilder();
        oBuilderQuery
                .where(
                        EventsDao.Properties.Start_date.ge(start),
                        EventsDao.Properties.Start_date.le(end),
        oBuilderQuery
                .or(
                        EventsDao.Properties.End_date.ge(start),
                        EventsDao.Properties.End_date.le(end)));
        return oBuilderQuery.list();
    }

}
