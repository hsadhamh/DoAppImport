package factor.labs.indiancalendar.CalendarDbHelper;

/**
 * Created by hassanhussain on 9/10/2015.
 */
public class CalendarSQLiteDBConstants {
    public final static String DATABASE_NAME = "CalendarEvents.eve";

    //  Event Master Table
    public final static String TBL_EVENT_MASTER = "ic_EventMaster";
    public final static String COL_EVENT_ID = "EventId";
    public final static String COL_EVENT_NAME = "EventName";
    public final static String COL_EVENT_DISPLAY_NAME = "DisplayName";
    public final static String COL_EVENT_DATE = "DateString";
    public final static String COL_EVENT_RECUR = "RecurringEvent";
    public final static String COL_EVENT_CATEGORY = "EventCategory";
    public final static String COL_EVENT_CREATED = "DateCreated";
    public final static String COL_EVENT_MODIFIED = "DateModified";
    public final static String COL_EVENT_RECUR_INETRVAL = "RecurringInterval";
    public final static String COL_EVENT_RECUR_TIMES= "RecurringTimes";
    public final static int iCOL_EVENT_ID = 0;
    public final static int iCOL_EVENT_NAME = 1;
    public final static int iCOL_EVENT_DISPLAY_NAME = 2;
    public final static int iCOL_EVENT_DATE = 3;
    public final static int iCOL_EVENT_RECUR = 6;
    public final static int iCOL_EVENT_CATEGORY = 9;
    public final static int iCOL_EVENT_CREATED = 4;
    public final static int iCOL_EVENT_MODIFIED = 5;
    public final static int iCOL_EVENT_RECUR_INTERVAL = 7;
    public final static int iCOL_EVENT_RECUR_TIMES = 8;

    //  Events properties table
    public final static String TBL_EVENT_PROP = "ic_EventProperties";
    public final static String COL_EVENT_PROP_ID = "ID";
    public final static String COL_PROP_ID = "PropertyID";
    public final static String COL_PROP_VAL = "PropertyValue";
    public final static int iCOL_EVENT_PROP_ID = 0;
    public final static int iCOL_PROP_ID = 2;
    public final static int iCOL_PROP_VAL = 3;

    //  Events property names table
    public final static String TBL_EVENT_PROP_NAMES = "ic_EventPropertyNames";
    public final static String COL_PROP_NAME_ID = "ID";
    public final static String COL_PROP_NAME = "PropertyName";
    public final static String COL_PROP_TYPE = "PropertyType";
    public final static int iCOL_PROP_NAME_ID = 0;
    public final static int iCOL_PROP_NAME = 1;
    public final static int iCOL_PROP_TYPE = 2;

    //  Country table
    public final static String TBL_EVENT_COUNTRY = "Country";
    public final static String COL_COUNTRY_ID = "CountryId";
    public final static String COL_COUNTRY_NAME = "CountryName";
    public final static String COL_COUNTRY_CURRENCY = "CountryCurrency";
    public final static String COL_COUNTRY_ISO_CODE = "CountryCode";
    public final static int iCOL_COUNTRY_ID = 0;
    public final static int iCOL_COUNTRY_NAME = 3;
    public final static int iCOL_COUNTRY_CURRENCY = 2;
    public final static int iCOL_COUNTRY_ISO_CODE = 1;

    //  State Table
    public final static String TBL_EVENT_STATE = "State";
    public final static String COL_STATE_ID = "StateId";
    public final static String COL_STATE_NAME = "StateName";
    public final static String COL_STATE_ISO_CODE = "StateCode";
    public final static String COL_COUNTRY_ID_STATE= "CountryId";
    public final static int iCOL_STATE_ID = 0;
    public final static int iCOL_STATE_NAME = 1;
    public final static int iCOL_STATE_ISO_CODE = 2;
    public final static int iCOL_COUNTRY_ID_STATE= 3;

    //  city table
    public final static String TBL_EVENT_CITY = "City";
    public final static String COL_CITY_ID = "CityId";
    public final static String COL_CITY_NAME = "CityName";
    public final static String COL_CITY_STATE = "StateId";
    public final static int iCOL_CITY_ID = 0;
    public final static int iCOL_CITY_NAME = 1;
    public final static int iCOL_CITY_STATE = 2;

    //  Coordinates table
    public final static String TBL_EVENT_COORDINATES = "Coordinates";

    //  address table
    public final static String TBL_EVENT_ADDRESS = "Address";

    //  event info table
    public final static String TBL_EVENT_INFO = "EventInfo";

    //  event category table
    public final static String TBL_EVENT_CATEGORY = "EventCategory";

    public final static int EVENT_CATEGORY_HOLIDAY_RELIGIOUS = 1;

    //  Properties of events.
    public final static String PROP_HOLIDAY_FLAG = "Holiday Flag";
    public final static String PROP_RELIGIOUS_FLAG = "Religious Flag";
    public final static String PROP_RELIGION_ID = "Religion ID";
    public final static String PROP_STATE_LOCATION_ID = "State Location ID";
    public final static String PROP_CITY_LOCATION_ID = "City Location ID";
    public final static String PROP_COUNTRY_LOCATION_ID = "Country ID";
    public final static String PROP_COORD_LOCATION_ID = "Coordinate Location ID";
    public final static String PROP_ADDR_LOCATION_ID = "Address ID";
    public final static String PROP_INFO_ID = "Info ID";
    public final static String PROP_REMINDER_ID = "Reminder ID";
    public final static String PROP_KIND_LOCAL_EVENT_ID = "Kind Local Event";
    public final static String PROP_KIND_TODO = "Kind ToDo";
    public final static String PROP_KIND_MEETING = "Kind Meeting";
    public final static String PROP_KIND_SPL_EVENT = "Kind Special Event";
    public final static String PROP_KIND_PLANS = "Kind Plans";
    public final static String PROP_TAG_ID = "Tag ID";
    public final static String PROP_LIST_PEOPLE = "List People";
    public final static String PROP_WEATHER = "Weather";

    // Religion values
    public final static int iHINDU_RELIGION = 1;
    public final static int iMUSLIM_RELIGION = 2;
    public final static int iCHRISTIAN_RELIGION = 3;
    public final static int iJEW_RELIGION = 4;
    public final static int iORTHODOX_RELIGION = 5;
    public final static String[] ReligionNames =  {"Hindu", "Islam", "Christian", "Jew", "Orthodox", "", "", "", "", ""};
    //public final static int iRELIGION = 1;




}
