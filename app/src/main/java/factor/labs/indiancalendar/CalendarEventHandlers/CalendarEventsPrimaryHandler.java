package factor.labs.indiancalendar.CalendarEventHandlers;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import factor.labs.indiancalendar.CalendarJSONLib.CalendarJsonParser;
import factor.labs.indiancalendar.CalendarUtils.CalendarDateClass;

/**
 * Created by hassanhussain on 7/29/2015.
 */
public class CalendarEventsPrimaryHandler {

    Context moContext;

    String sTag = "";

    List<CalendarEventYearClass> mListEventsForYear = new ArrayList<CalendarEventYearClass>();

    public CalendarEventsPrimaryHandler(Context oContext) {
        moContext = oContext;
    }

    public CalendarEventYearClass getEventsYearObject(int yr)
    {
        sTag = "CalendarEventsPrimaryHandler.getEventsYearObject";
        CalendarEventYearClass oRetYr = null;
        try {

            for (CalendarEventYearClass oYr : mListEventsForYear) {
                if (oYr.Year == yr) {
                    Log.d(sTag, "Got events in cache for year ["+ yr +"]");
                    oRetYr = oYr;
                    break;
                }
            }

            //  Cache events for year
            if(oRetYr  == null) {
                Log.d(sTag, "no events in cache for year [" + yr + "]");
                Object oListOfEventsForSingleYear = CalendarJsonParser.getEventsForYear(moContext.getAssets(), yr);
                if (oListOfEventsForSingleYear != null) {
                    if (oListOfEventsForSingleYear instanceof CalendarEventYearClass) {
                        oRetYr = (CalendarEventYearClass) oListOfEventsForSingleYear;
                        Log.d(sTag, "Got and Added events for year [" + yr + "]");
                        mListEventsForYear.add(oRetYr);
                    }
                }
            }
        }
        catch(Exception ex)
        {
            Log.e(sTag, "Exception message : " + ex.getMessage());
        }
        return oRetYr;
    }

    public List<CalendarEventDateClass> getEventsForMonth(int nMonth, int nYear)
    {
        sTag = "CalendarEventsPrimaryHandler.getEventsForMonth";
        List<CalendarEventDateClass> oListDateEvents = null;
        try {
            CalendarEventYearClass oFoundYear = getEventsYearObject(nYear);
            if (oFoundYear == null) {
                Log.w(sTag, "Failed to get events object for year ["+ nYear +"]");
                return oListDateEvents;
            }

            Log.d(sTag, "Get events object for month ["+ nMonth +"]");
            for (CalendarEventMonthClass oMonth : oFoundYear.months) {
                if (nMonth == oMonth.month) {
                    Log.d(sTag, "Found events object for month ["+ nMonth +"]");
                    oListDateEvents = new ArrayList<>(oMonth.days);
                    break;
                }
            }
        }
        catch(Exception exState)
        {
            Log.e(sTag, "Exception message : " + exState.getMessage());
        }
        return oListDateEvents;
    }

}
