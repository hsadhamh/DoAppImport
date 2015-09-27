package factor.labs.indiancalendar.CalendarDbHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;

/**
 * Created by hassanhussain on 9/9/2015.
 */
public class CalendarSQLiteCRUD {
    CalendarSQLiteDBHelper oDBHelper = null;

    public CalendarSQLiteCRUD(Context oCon)
    {
        oDBHelper = new CalendarSQLiteDBHelper(oCon, "CalendarEvents.eve", null, 1);
    }

    /*public List<CalendarEventMaster> getAllHolidayReligiousEvents()
    {
        List<CalendarEventMaster> oListEvents = new ArrayList<>();

        try {
            SQLiteDatabase oDB = null;

            if ((oDB = oDBHelper.getDb()) != null) {
                String AllEventsSelect = "SELECT * FROM " + CalendarSQLiteDBConstants.TBL_EVENT_MASTER;
                AllEventsSelect += " where " + CalendarSQLiteDBConstants.COL_EVENT_CATEGORY +
                        " = " + CalendarSQLiteDBConstants.EVENT_CATEGORY_HOLIDAY_RELIGIOUS;

                oDB = oDBHelper.getReadableDatabase();
                Cursor oCur = oDB.rawQuery(AllEventsSelect, null);

                if (oCur.moveToFirst()) {
                    do {
                        CalendarEventMaster oEvent = new CalendarEventMaster();

                        int nEventID = oCur.getInt(CalendarSQLiteDBConstants.iCOL_EVENT_ID);
                        oEvent.setEventID(nEventID);
                        oEvent.setEventName(oCur.getString(CalendarSQLiteDBConstants.iCOL_EVENT_NAME));
                        oEvent.setDisplayName(oCur.getString(CalendarSQLiteDBConstants.iCOL_EVENT_DISPLAY_NAME));

                        String sDateString = oCur.getString(CalendarSQLiteDBConstants.iCOL_EVENT_DATE);
                        oEvent.setDateString(sDateString);

                        String[] sDateArray = sDateString.split("-");
                        oEvent.setDate(Integer.parseInt(sDateArray[0]));
                        oEvent.setYear(Integer.parseInt(sDateArray[2]));
                        int nMonthIndex = labsCalendarUtils.getIndexForShortName(sDateArray[1]);
                        if (nMonthIndex != -1)
                            nMonthIndex = 0;
                        oEvent.setMonth(nMonthIndex + 1);

                        oEvent.setRecurEvent(oCur.getInt(CalendarSQLiteDBConstants.iCOL_EVENT_RECUR) > 0 ? true : false);
                        oEvent.setRecurringAfter(oCur.getInt(CalendarSQLiteDBConstants.iCOL_EVENT_RECUR_INTERVAL));
                        oEvent.setRecurringNumber(oCur.getInt(CalendarSQLiteDBConstants.iCOL_EVENT_RECUR_TIMES));
                        int nEventCategory = oCur.getInt(CalendarSQLiteDBConstants.iCOL_EVENT_CATEGORY);
                        oEvent.setEventCatgeory(nEventCategory);

                        CalendarEventCategoryHolRel oHolidayReligious = (CalendarEventCategoryHolRel) getPropertiesForEvent(CalendarSQLiteDBConstants.EVENT_CATEGORY_HOLIDAY_RELIGIOUS, nEventID);
                        List<Object> oListLocations = getListLocations(nEventID);

                        oEvent.setEventProp(oHolidayReligious);
                        oEvent.setLocationList(oListLocations);

                        oListEvents.add(oEvent);
                    } while (oCur.moveToNext());
                }
            }
        }
        catch(Exception ex)
        {
            Log.e("HolidayReligiousEvents", "Exception Caught : " + ex.getMessage());
        }
        return oListEvents;
    }

    public String getAddress(int type, int ID)
    {
        return "";
    }

    public List<Object> getListLocations(int nEventID)
    {
        List<Object> oListLocationInfo = new ArrayList<>();
        try {


            SQLiteDatabase oDB = oDBHelper.getReadableDatabase();

            String selectForProps = "SELECT * FROM " + CalendarSQLiteDBConstants.TBL_EVENT_PROP +
                    " WHERE " + CalendarSQLiteDBConstants.COL_EVENT_ID + " = " + nEventID +
                    " AND " + CalendarSQLiteDBConstants.COL_PROP_ID + " IN (4,5,6,17,18)";

            Cursor oCurForProps = oDB.rawQuery(selectForProps, null);
            if (oCurForProps.moveToFirst()) {
                do {
                    int nPropID = oCurForProps.getInt(CalendarSQLiteDBConstants.iCOL_PROP_ID);

                    String selectForPropName = "SELECT * FROM " + CalendarSQLiteDBConstants.TBL_EVENT_PROP_NAMES +
                            " WHERE " + CalendarSQLiteDBConstants.COL_PROP_NAME_ID + " = " + nPropID;

                    Cursor oCurForPropName = oDB.rawQuery(selectForPropName, null);
                    if (oCurForPropName.moveToFirst()) {
                        if (oCurForPropName.getString(CalendarSQLiteDBConstants.iCOL_PROP_NAME).equals(CalendarSQLiteDBConstants.PROP_CITY_LOCATION_ID)) {
                            Object oLocation = getLocationObject(oCurForProps.getInt(CalendarSQLiteDBConstants.iCOL_PROP_VAL), 1);
                            if (oLocation != null)
                                oListLocationInfo.add(oLocation);
                        }

                        if (oCurForPropName.getString(CalendarSQLiteDBConstants.iCOL_PROP_NAME).equals(CalendarSQLiteDBConstants.PROP_STATE_LOCATION_ID)) {
                            Object oLocation = getLocationObject(oCurForProps.getInt(CalendarSQLiteDBConstants.iCOL_PROP_VAL), 2);
                            if (oLocation != null)
                                oListLocationInfo.add(oLocation);
                        }

                        if (oCurForPropName.getString(CalendarSQLiteDBConstants.iCOL_PROP_NAME).equals(CalendarSQLiteDBConstants.PROP_COUNTRY_LOCATION_ID)) {
                            Object oLocation = getLocationObject(oCurForProps.getInt(CalendarSQLiteDBConstants.iCOL_PROP_VAL), 3);
                            if (oLocation != null)
                                oListLocationInfo.add(oLocation);
                        }
                    }
                    oCurForPropName.close();
                } while (oCurForProps.moveToNext());
                oCurForProps.close();
            }
        }
        catch(Exception ex)
        {
            Log.e("HolidayReligiousLoc", "Exception Caught : " + ex.getMessage());
        }

        return oListLocationInfo;
    }

    public Object getPropertiesForEvent(int nEventCategory, int nEventID)
    {
        CalendarEventCategoryHolRel oHolidayReligious = new CalendarEventCategoryHolRel();
        try {
            SQLiteDatabase oDB = oDBHelper.getReadableDatabase();

            String selectForProps = "SELECT * FROM " + CalendarSQLiteDBConstants.TBL_EVENT_PROP +
                    " WHERE " + CalendarSQLiteDBConstants.COL_EVENT_ID + " = " + nEventID +
                    " AND " + CalendarSQLiteDBConstants.COL_PROP_ID + " NOT IN (4,5,6,17,18)";


            Cursor oCurForProps = oDB.rawQuery(selectForProps, null);
            if (oCurForProps.moveToFirst()) {
                do {
                    if (nEventCategory == CalendarSQLiteDBConstants.EVENT_CATEGORY_HOLIDAY_RELIGIOUS) {
                        // props settings
                        int nPropID = oCurForProps.getInt(CalendarSQLiteDBConstants.iCOL_PROP_ID);

                        if(nPropID == 1)
                        {
                            oHolidayReligious.bHolidayFlag = oCurForProps.getInt(CalendarSQLiteDBConstants.iCOL_PROP_VAL) > 0 ? true : false;
                        }
                        else if(nPropID == 2)
                        {
                            oHolidayReligious.bReligiousFlag = oCurForProps.getInt(CalendarSQLiteDBConstants.iCOL_PROP_VAL) > 0 ? true : false;
                        }
                        else if(nPropID == 3)
                        {
                            oHolidayReligious.sReligion = CalendarSQLiteDBConstants.ReligionNames[oCurForProps.getInt(CalendarSQLiteDBConstants.iCOL_PROP_VAL)];
                        }
                    }
                } while (oCurForProps.moveToNext());
            }
            oCurForProps.close();
        }
        catch(Exception ex)
        {
            Log.e("getPropertiesForEvent", "Exception Caught : " + ex.getMessage() + " " + ex.toString() +
            "  " );
            ex.printStackTrace();
        }
        return oHolidayReligious;
    }

    public Object getLocationObject(int nLocationID, int nType)
    {
        if(nType == 1) // city
        {
            return getCityLocation(nLocationID);
        }
        else if(nType == 2) // state
        {
            return getStateLocation(nLocationID);
        }
        else if(nType == 3) // country
        {
            return getCountryLocation(nLocationID);
        }
        return null;
    }

    public CalendarEventLocationCityInfo getCityLocation(int nLocationID)
    {
        CalendarEventLocationCityInfo oCityInfo = null;
        try {
            SQLiteDatabase oDB = oDBHelper.getReadableDatabase();
            String selectForProps = "SELECT * FROM " + CalendarSQLiteDBConstants.TBL_EVENT_CITY +
                    " WHERE " + CalendarSQLiteDBConstants.COL_CITY_ID + " = " + nLocationID;

            Cursor oCurForProps = oDB.rawQuery(selectForProps, null);
            if (oCurForProps.moveToFirst()) {
                oCityInfo = new CalendarEventLocationCityInfo();

                oCityInfo.nCityID = nLocationID;
                oCityInfo.sCityName = oCurForProps.getString(CalendarSQLiteDBConstants.iCOL_CITY_NAME);
                int nStateLocationID = oCurForProps.getInt(CalendarSQLiteDBConstants.iCOL_CITY_STATE);
                oCityInfo.oStateInfo = getStateLocation(nStateLocationID);
            }
            oCurForProps.close();
        }
        catch(Exception ex)
        {
            Log.e("getCityLocation", "Exception Caught : " + ex.getMessage());
        }
        return oCityInfo;
    }

    public CalendarEventLocationStateInfo getStateLocation(int nLocationID)
    {
        CalendarEventLocationStateInfo oStateInfo = null;
        try {
            SQLiteDatabase oDB = oDBHelper.getReadableDatabase();
            String selectForProps = "SELECT * FROM " + CalendarSQLiteDBConstants.TBL_EVENT_STATE +
                    " WHERE " + CalendarSQLiteDBConstants.COL_STATE_ID + " = " + nLocationID;

            Cursor oCurForProps = oDB.rawQuery(selectForProps, null);
            if (oCurForProps.moveToFirst()) {
                oStateInfo = new CalendarEventLocationStateInfo();

                oStateInfo.sStateName = oCurForProps.getString(CalendarSQLiteDBConstants.iCOL_STATE_ID);
                int nCountryLocationID = oCurForProps.getInt(CalendarSQLiteDBConstants.iCOL_COUNTRY_ID_STATE);
                oStateInfo.sStateCode = oCurForProps.getString(CalendarSQLiteDBConstants.iCOL_STATE_ISO_CODE);
                oStateInfo.nStateID = nLocationID;
                oStateInfo.oCountryInfo = getCountryLocation(nCountryLocationID);
            }
            oCurForProps.close();
        }
        catch(Exception ex)
        {
            Log.e("getStateLocation", "Exception Caught : " + ex.getMessage());
        }
        return oStateInfo;
    }

    public CalendarEventLocationCountryInfo getCountryLocation(int nLocationID)
    {
        CalendarEventLocationCountryInfo oCountryInfo = null;
        try {
            SQLiteDatabase oDB = oDBHelper.getReadableDatabase();
            String selectForProps = "SELECT * FROM " + CalendarSQLiteDBConstants.TBL_EVENT_COUNTRY +
                    " WHERE " + CalendarSQLiteDBConstants.COL_COUNTRY_ID + " = " + nLocationID;

            Cursor oCurForProps = oDB.rawQuery(selectForProps, null);
            if (oCurForProps.moveToFirst()) {
                oCountryInfo = new CalendarEventLocationCountryInfo();

                oCountryInfo.sCountryName = oCurForProps.getString(CalendarSQLiteDBConstants.iCOL_COUNTRY_NAME);
                oCountryInfo.sCountryCurrency = oCurForProps.getString(CalendarSQLiteDBConstants.iCOL_COUNTRY_CURRENCY);
                oCountryInfo.sCountryCode = oCurForProps.getString(CalendarSQLiteDBConstants.iCOL_COUNTRY_ISO_CODE);
                oCountryInfo.nCountryID = nLocationID;
            }
            oCurForProps.close();
        }
        catch(Exception ex)
        {
            Log.e("getCountryLocation", "Exception Caught : " + ex.getMessage());
        }
        return oCountryInfo;
    }*/

    public List<CalendarEventMaster> getHolidayEventsForMonth(int mon, int yr)
    {
        List<CalendarEventMaster> oListEvents = new ArrayList<>();
        try {
            String sShortMon = labsCalendarUtils.getMonthShortName(mon);

            SQLiteDatabase oDB = null;

            if ((oDB = oDBHelper.getDb()) != null) {
                String AllEventsSelect = "SELECT * FROM vw_holiday_religious ";
                AllEventsSelect += " where " + CalendarSQLiteDBConstants.COL_EVENT_DATE + " LIKE '%-" + sShortMon + "-" + yr + "'";

                String AllEvents = "select * from (" +  "Select ic_EventMaster.EventId,ic_EventMaster.EventName, ic_EventMaster.DisplayName,ic_EventMaster.DateString,\n" +
                        "sum(case when ic_EventProperties.PropertyID=3 then ic_EventProperties.PropertyValue end) Religious_id,\n" +
                        "sum(case when ic_EventProperties.PropertyID=18 then ic_EventProperties.PropertyValue end) Country_code,\n" +
                        "sum(case when ic_EventProperties.PropertyID=1 then ic_EventProperties.PropertyValue end) Holiday_flag,\n" +
                        "sum(case when ic_EventProperties.PropertyID=2 then ic_EventProperties.PropertyValue end) Religious_flag,\n" +
                        "sum(case when ic_EventProperties.PropertyID=4 then ic_EventProperties.PropertyValue end) State_code\n" +
                        "from ic_EventMaster join ic_EventProperties  " +
                        "on ic_EventMaster.EventId = ic_EventProperties.EventID " +
                        "group by ic_EventMaster.EventId,ic_EventProperties.EventID) " +
                        " where " + CalendarSQLiteDBConstants.COL_EVENT_DATE + " LIKE '%-" + sShortMon + "-" + yr + "'"
                        + " AND Holiday_flag = 1";;

                if(labsCalendarUtils.getCountryPref() == 2)
                {
                    AllEvents += " AND Country_Code = " + 4;
                }
                else if(labsCalendarUtils.getCountryPref() == 1)
                {
                    AllEvents += " AND Country_Code = " + 7;
                }

                oDB = oDBHelper.getReadableDatabase();
                Cursor oCur = oDB.rawQuery(AllEvents, null);

                if (oCur.moveToFirst()) {
                    do {
                        CalendarEventMaster oEvent = new CalendarEventMaster();

                        int nEventID = oCur.getInt(CalendarSQLiteDBConstants.iCOL_EVENT_ID);
                        oEvent.setEventID(nEventID);
                        oEvent.setEventName(oCur.getString(CalendarSQLiteDBConstants.iCOL_EVENT_NAME));
                        oEvent.setDisplayName(oCur.getString(CalendarSQLiteDBConstants.iCOL_EVENT_DISPLAY_NAME));

                        String sDateString = oCur.getString(CalendarSQLiteDBConstants.iCOL_EVENT_DATE);
                        oEvent.setDateString(sDateString);

                        String[] sDateArray = sDateString.split("-");

                        Log.d("HolidayReligiousEvents", "Date : " + sDateString + " Split : " + sDateArray[0] + " : " + sDateArray[1] + " : " + sDateArray[2]);

                        oEvent.setDate(Integer.parseInt(sDateArray[0]));
                        oEvent.setYear(Integer.parseInt(sDateArray[2]));
                        int nMonthIndex = labsCalendarUtils.getIndexForShortName(sDateArray[1]);
                        if (nMonthIndex == -1)
                            nMonthIndex = 0;
                        Log.d("HolidayReligiousEvents", "month : " + nMonthIndex);
                        oEvent.setMonth(nMonthIndex + 1);

                        oEvent.setReligionID(oCur.getInt(4));
                        oEvent.setCountry(oCur.getInt(5));
                        if(oCur.getInt(6) > 0)
                            oEvent.setHolidayFlag();
                        else
                            oEvent.resetHolidayFlag();

                        if(oCur.getInt(7) > 0)
                            oEvent.setReligionFlag();
                        else
                            oEvent.resetReligionFlag();

                        oEvent.setState(oCur.getInt(8));

                        oListEvents.add(oEvent);
                    } while (oCur.moveToNext());
                }
                oCur.close();
            }
        }catch(Exception ex)
        {
            Log.e("getEventsForMonth", "Exception Caught : " + ex.getMessage());
        }
        return oListEvents;
    }

    public List<CalendarEventMaster> getHolidayReligiousEventsForMonth(int mon, int yr)
    {
        List<CalendarEventMaster> oListEvents = new ArrayList<>();
        try {
            String sShortMon = labsCalendarUtils.getMonthShortName(mon);

            SQLiteDatabase oDB = null;

            if ((oDB = oDBHelper.getDb()) != null) {

                String AllEvents = "select * from (" +  "Select ic_EventMaster.EventId,ic_EventMaster.EventName, ic_EventMaster.DisplayName,ic_EventMaster.DateString,\n" +
                        "sum(case when ic_EventProperties.PropertyID=3 then ic_EventProperties.PropertyValue end) Religious_id,\n" +
                        "sum(case when ic_EventProperties.PropertyID=18 then ic_EventProperties.PropertyValue end) Country_code,\n" +
                        "sum(case when ic_EventProperties.PropertyID=1 then ic_EventProperties.PropertyValue end) Holiday_flag,\n" +
                        "sum(case when ic_EventProperties.PropertyID=2 then ic_EventProperties.PropertyValue end) Religious_flag,\n" +
                        "sum(case when ic_EventProperties.PropertyID=4 then ic_EventProperties.PropertyValue end) State_code\n" +
                        "from ic_EventMaster join ic_EventProperties  " +
                        "on ic_EventMaster.EventId = ic_EventProperties.EventID " +
                        "group by ic_EventMaster.EventId,ic_EventProperties.EventID) " +
                        " where " + CalendarSQLiteDBConstants.COL_EVENT_DATE + " LIKE '%-" + sShortMon + "-" + yr + "'";

                if(labsCalendarUtils.getCountryPref() == 2)
                {
                    AllEvents += " AND Country_Code = " + 4;
                }
                else if(labsCalendarUtils.getCountryPref() == 1)
                {
                    AllEvents += " AND Country_Code = " + 7;
                }

                oDB = oDBHelper.getReadableDatabase();
                Cursor oCur = oDB.rawQuery(AllEvents, null);

                if (oCur.moveToFirst()) {
                    do {
                        CalendarEventMaster oEvent = new CalendarEventMaster();

                        int nEventID = oCur.getInt(CalendarSQLiteDBConstants.iCOL_EVENT_ID);
                        oEvent.setEventID(nEventID);
                        oEvent.setEventName(oCur.getString(CalendarSQLiteDBConstants.iCOL_EVENT_NAME));
                        oEvent.setDisplayName(oCur.getString(CalendarSQLiteDBConstants.iCOL_EVENT_DISPLAY_NAME));

                        String sDateString = oCur.getString(CalendarSQLiteDBConstants.iCOL_EVENT_DATE);
                        oEvent.setDateString(sDateString);

                        String[] sDateArray = sDateString.split("-");

                        Log.d("HolidayReligiousEvents", "Date : " + sDateString + " Split : " + sDateArray[0] + " : " + sDateArray[1] + " : " + sDateArray[2]);

                        oEvent.setDate(Integer.parseInt(sDateArray[0]));
                        oEvent.setYear(Integer.parseInt(sDateArray[2]));
                        int nMonthIndex = labsCalendarUtils.getIndexForShortName(sDateArray[1]);
                        if (nMonthIndex == -1)
                            nMonthIndex = 0;
                        Log.d("HolidayReligiousEvents", "month : " + nMonthIndex);
                        oEvent.setMonth(nMonthIndex + 1);

                        oEvent.setReligionID(oCur.getInt(4));
                        oEvent.setCountry(oCur.getInt(5));
                        if(oCur.getInt(6) > 0)
                            oEvent.setHolidayFlag();
                        else
                            oEvent.resetHolidayFlag();

                        if(oCur.getInt(7) > 0)
                            oEvent.setReligionFlag();
                        else
                            oEvent.resetReligionFlag();

                        oEvent.setState(oCur.getInt(8));

                        oListEvents.add(oEvent);
                    } while (oCur.moveToNext());
                }
                oCur.close();
            }
        }catch(Exception ex)
        {
            Log.e("getEventsForMonth", "Exception Caught : " + ex.getMessage());
        }
        return oListEvents;
    }

    public List<CalendarEventMaster> getReligiousEventsForMonth(int mon, int yr, int nFilter)
    {
        List<CalendarEventMaster> oListEvents = new ArrayList<>();
        try {
            String sShortMon = labsCalendarUtils.getMonthShortName(mon);

            SQLiteDatabase oDB = null;

            if ((oDB = oDBHelper.getDb()) != null) {
                String AllEvents = "select * from (" +  "Select ic_EventMaster.EventId,ic_EventMaster.EventName, ic_EventMaster.DisplayName,ic_EventMaster.DateString,\n" +
                        "sum(case when ic_EventProperties.PropertyID=3 then ic_EventProperties.PropertyValue end) Religious_id,\n" +
                        "sum(case when ic_EventProperties.PropertyID=18 then ic_EventProperties.PropertyValue end) Country_code,\n" +
                        "sum(case when ic_EventProperties.PropertyID=1 then ic_EventProperties.PropertyValue end) Holiday_flag,\n" +
                        "sum(case when ic_EventProperties.PropertyID=2 then ic_EventProperties.PropertyValue end) Religious_flag,\n" +
                        "sum(case when ic_EventProperties.PropertyID=4 then ic_EventProperties.PropertyValue end) State_code\n" +
                        "from ic_EventMaster join ic_EventProperties  " +
                        "on ic_EventMaster.EventId = ic_EventProperties.EventID " +
                        "group by ic_EventMaster.EventId,ic_EventProperties.EventID) " +
                        " where " + CalendarSQLiteDBConstants.COL_EVENT_DATE + " LIKE '%-" + sShortMon + "-" + yr + "'"
                        + " AND Religious_flag = 1";

                if(nFilter != 0)
                {
                    //  1: HINDU
                    //  2: Muslim
                    //  3: Christian
                    //  4: Jewish
                    //  5: Sikhs
                    //  6: Buddish
                    //  7: Orthodox
                    AllEvents+= " AND Religious_id = " + nFilter + "";
                }

                if(labsCalendarUtils.getCountryPref() == 2)
                {
                    AllEvents += " AND Country_Code = " + 4;
                }
                else if(labsCalendarUtils.getCountryPref() == 1)
                {
                    AllEvents += " AND Country_Code = " + 7;
                }
                AllEvents += ";";

                oDB = oDBHelper.getReadableDatabase();
                Cursor oCur = oDB.rawQuery(AllEvents, null);

                if (oCur.moveToFirst()) {
                    do {
                        CalendarEventMaster oEvent = new CalendarEventMaster();

                        int nEventID = oCur.getInt(CalendarSQLiteDBConstants.iCOL_EVENT_ID);
                        oEvent.setEventID(nEventID);
                        oEvent.setEventName(oCur.getString(CalendarSQLiteDBConstants.iCOL_EVENT_NAME));
                        oEvent.setDisplayName(oCur.getString(CalendarSQLiteDBConstants.iCOL_EVENT_DISPLAY_NAME));

                        String sDateString = oCur.getString(CalendarSQLiteDBConstants.iCOL_EVENT_DATE);
                        oEvent.setDateString(sDateString);

                        String[] sDateArray = sDateString.split("-");

                        Log.d("HolidayReligiousEvents", "Date : " + sDateString + " Split : " + sDateArray[0] + " : " + sDateArray[1] + " : " + sDateArray[2]);

                        oEvent.setDate(Integer.parseInt(sDateArray[0]));
                        oEvent.setYear(Integer.parseInt(sDateArray[2]));
                        int nMonthIndex = labsCalendarUtils.getIndexForShortName(sDateArray[1]);
                        if (nMonthIndex == -1)
                            nMonthIndex = 0;
                        Log.d("HolidayReligiousEvents", "month : " + nMonthIndex);
                        oEvent.setMonth(nMonthIndex + 1);

                        oEvent.setReligionID(oCur.getInt(4));
                        oEvent.setCountry(oCur.getInt(5));
                        if(oCur.getInt(6) > 0)
                            oEvent.setHolidayFlag();
                        else
                            oEvent.resetHolidayFlag();

                        if(oCur.getInt(7) > 0)
                            oEvent.setReligionFlag();
                        else
                            oEvent.resetReligionFlag();

                        oEvent.setState(oCur.getInt(8));

                        oListEvents.add(oEvent);
                    } while (oCur.moveToNext());
                }
                oCur.close();
            }
        }catch(Exception ex)
        {
            Log.e("getEventsForMonth", "Exception Caught : " + ex.getMessage());
        }
        return oListEvents;
    }
}
