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

    public CalendarSQLiteCRUD(Context oCon) {
        oDBHelper = new CalendarSQLiteDBHelper(oCon, "CalendarEvents.eve", null, 1);
    }

    public void close(){ if(oDBHelper != null) oDBHelper.close(); }

    public List<CalendarEventMaster> getHolidayEventsForMonth(int mon, int yr) {
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

                try {
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

                            //Log.d("HolidayReligiousEvents", "Date : " + sDateString + " Split : " + sDateArray[0] + " : " + sDateArray[1] + " : " + sDateArray[2]);

                            oEvent.setDate(Integer.parseInt(sDateArray[0]));
                            oEvent.setYear(Integer.parseInt(sDateArray[2]));
                            int nMonthIndex = labsCalendarUtils.getIndexForShortName(sDateArray[1]);
                            if (nMonthIndex == -1)
                                nMonthIndex = 0;
                            //Log.d("HolidayReligiousEvents", "month : " + nMonthIndex);
                            oEvent.setMonth(nMonthIndex + 1);

                            oEvent.setReligionID(oCur.getInt(4));
                            oEvent.setCountry(oCur.getInt(5));
                            if (oCur.getInt(6) > 0)
                                oEvent.setHolidayFlag();
                            else
                                oEvent.resetHolidayFlag();

                            if (oCur.getInt(7) > 0)
                                oEvent.setReligionFlag();
                            else
                                oEvent.resetReligionFlag();

                            oEvent.setState(oCur.getInt(8));

                            oListEvents.add(oEvent);
                        } while (oCur.moveToNext());
                    }
                }
                catch(Exception ex){
                    Log.e("getEventsForMonth", "Exception Caught : " + ex.getMessage());
                }
                finally {
                    oCur.close();
                    oDB.close();
                }
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

                String AllEvents = "select EventDetails.*, Description, Link, WikiInfo from (" +
                        "Select " +
                        "ic_EventMaster.EventId,ic_EventMaster.EventName, " +
                        "ic_EventMaster.DisplayName,ic_EventMaster.DateString,\n" +
                        "sum(case when ic_EventProperties.PropertyID=3 then ic_EventProperties.PropertyValue end) Religious_id,\n" +
                        "sum(case when ic_EventProperties.PropertyID=18 then ic_EventProperties.PropertyValue end) Country_code,\n" +
                        "sum(case when ic_EventProperties.PropertyID=1 then ic_EventProperties.PropertyValue end) Holiday_flag,\n" +
                        "sum(case when ic_EventProperties.PropertyID=2 then ic_EventProperties.PropertyValue end) Religious_flag,\n" +
                        "sum(case when ic_EventProperties.PropertyID=4 then ic_EventProperties.PropertyValue end) State_code, \n" +
                        "sum(case when ic_EventProperties.PropertyID=7 then ic_EventProperties.PropertyValue end) Info_Id\n" +
                        "from ic_EventMaster join ic_EventProperties  " +
                        "on ic_EventMaster.EventId = ic_EventProperties.EventID " +
                        "group by ic_EventMaster.EventId,ic_EventProperties.EventID) EventDetails " +
                        " join EventInfo \n" +
                        " on EventDetails.Info_Id = EventInfo.InfoId" +
                        " where " + CalendarSQLiteDBConstants.COL_EVENT_DATE + " LIKE '%-" + sShortMon + "-" + yr + "'";

                if (labsCalendarUtils.getCountryPref() == 2) {
                    AllEvents += " AND Country_Code = " + 4;
                } else if (labsCalendarUtils.getCountryPref() == 1) {
                    AllEvents += " AND Country_Code = " + 7;
                }

                oDB = oDBHelper.getReadableDatabase();
                Cursor oCur = oDB.rawQuery(AllEvents, null);
                try {
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

                            //Log.d("HolidayReligiousEvents", "Date : " + sDateString + " Split : " + sDateArray[0] + " : " + sDateArray[1] + " : " + sDateArray[2]);

                            oEvent.setDate(Integer.parseInt(sDateArray[0]));
                            oEvent.setYear(Integer.parseInt(sDateArray[2]));
                            int nMonthIndex = labsCalendarUtils.getIndexForShortName(sDateArray[1]);
                            if (nMonthIndex == -1)
                                nMonthIndex = 0;
                            //Log.d("HolidayReligiousEvents", "month : " + nMonthIndex);
                            oEvent.setMonth(nMonthIndex + 1);

                            oEvent.setReligionID(oCur.getInt(4));
                            oEvent.setCountry(oCur.getInt(5));
                            if (oCur.getInt(6) > 0)
                                oEvent.setHolidayFlag();
                            else
                                oEvent.resetHolidayFlag();

                            if (oCur.getInt(7) > 0)
                                oEvent.setReligionFlag();
                            else
                                oEvent.resetReligionFlag();

                            oEvent.setState(oCur.getInt(8));
                            oEvent.setInfoID(oCur.getInt(9));
                            oEvent.setInfoDescription(oCur.getString(10));
                            oEvent.setWikiLinkPart(oCur.getString(11));
                            oEvent.setInfoWiki(oCur.getString(12));

                            oListEvents.add(oEvent);
                        } while (oCur.moveToNext());
                    }
                } catch (Exception ex) {
                    Log.e("getEventsForMonth", "Exception Caught : " + ex.getMessage());
                } finally {
                    oCur.close();
                    oDB.close();
                }
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

                try {
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

                            //Log.d("HolidayReligiousEvents", "Date : " + sDateString + " Split : " + sDateArray[0] + " : " + sDateArray[1] + " : " + sDateArray[2]);

                            oEvent.setDate(Integer.parseInt(sDateArray[0]));
                            oEvent.setYear(Integer.parseInt(sDateArray[2]));
                            int nMonthIndex = labsCalendarUtils.getIndexForShortName(sDateArray[1]);
                            if (nMonthIndex == -1)
                                nMonthIndex = 0;
                            //Log.d("HolidayReligiousEvents", "month : " + nMonthIndex);
                            oEvent.setMonth(nMonthIndex + 1);

                            oEvent.setReligionID(oCur.getInt(4));
                            oEvent.setCountry(oCur.getInt(5));
                            if (oCur.getInt(6) > 0)
                                oEvent.setHolidayFlag();
                            else
                                oEvent.resetHolidayFlag();

                            if (oCur.getInt(7) > 0)
                                oEvent.setReligionFlag();
                            else
                                oEvent.resetReligionFlag();

                            oEvent.setState(oCur.getInt(8));

                            oListEvents.add(oEvent);
                        } while (oCur.moveToNext());
                    }
                }
                catch (Exception ex) {
                    Log.e("getReligiousEvents", "Exception Caught : " + ex.getMessage());
                } finally {
                    oCur.close();
                    oDB.close();
                }
            }
        }catch(Exception ex)
        {
            Log.e("getEventsForMonth", "Exception Caught : " + ex.getMessage());
        }
        return oListEvents;
    }
}
