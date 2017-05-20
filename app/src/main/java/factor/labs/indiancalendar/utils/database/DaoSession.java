package factor.labs.indiancalendar.utils.database;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import factor.labs.indiancalendar.utils.database.Events;

import factor.labs.indiancalendar.utils.database.EventsDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig eventsDaoConfig;

    private final EventsDao eventsDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        eventsDaoConfig = daoConfigMap.get(EventsDao.class).clone();
        eventsDaoConfig.initIdentityScope(type);

        eventsDao = new EventsDao(eventsDaoConfig, this);

        registerDao(Events.class, eventsDao);
    }
    
    public void clear() {
        eventsDaoConfig.clearIdentityScope();
    }

    public EventsDao getEventsDao() {
        return eventsDao;
    }

}
