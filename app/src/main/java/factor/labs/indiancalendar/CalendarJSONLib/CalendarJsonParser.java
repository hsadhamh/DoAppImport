package factor.labs.indiancalendar.CalendarJSONLib;

import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import factor.labs.indiancalendar.CalendarEventHandlers.CalendarEventDateClass;
import factor.labs.indiancalendar.CalendarEventHandlers.CalendarEventYearClass;
import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;

/**
 * Created by hassanhussain on 7/20/2015.
 */
public class CalendarJsonParser {

    static String sTag;

    static List<CalendarEventDateClass> oListOfEvents = new ArrayList<CalendarEventDateClass>();

    static List<Object> oListOfEventsForYear = new ArrayList<Object>();

    public static List<CalendarEventDateClass> getEventsForMonth(AssetManager oAssets, int nMonth, int nYear)
    {
        sTag = "CalendarJSONWrapper.getEventsForMonth";
        try {
            oListOfEvents.clear();

            String sMonthName = labsCalendarUtils.getMonthName(nMonth); // get from UTILS.
            String sShortMonthName = labsCalendarUtils.getMonthShortName(nMonth); //  get from UTILS.

            String sJsonMonthFile = sMonthName + "_" + nYear + ".json";
            Log.d(sTag, "Read file name ["+sJsonMonthFile+"].");

            String sJSONString = readFile(sJsonMonthFile, oAssets);

            if(sJSONString.length() == 0)
            {
                Log.d(sTag, "No JSON String read from file.");
                return null;
            }

            Gson gson = new Gson();
            JsonReader jsonReader = new JsonReader(new StringReader(sJSONString));

            //begin parsing
            CalendarEventDateClass[] DaysEvents = gson.fromJson(jsonReader, CalendarEventDateClass[].class);

            if(DaysEvents.length == 0)
                Log.d(sTag, "No events read from ["+sJsonMonthFile+"].");

            //add the events object to the list
            for(CalendarEventDateClass oda : DaysEvents) {
                oListOfEvents.add(oda);
            }
        }
        catch(Exception exState)
        {
            Log.e(sTag, "Exception message : " + exState.getMessage());
        }
        return oListOfEvents;
    }

    static String readFile(String sFileName, AssetManager oAssetMan)
    {
        sTag = "readFile";
        String sJSONReadData = "";

        try {
            InputStream oStream = oAssetMan.open(sFileName);
            int nSize = oStream.available();

            byte[] buffer = new byte[nSize];
            oStream.read(buffer);

            sJSONReadData = new String(buffer);
        }
        catch(IOException ioExc)
        {
            Log.e(sTag, ioExc.getMessage());
        }
        catch(Exception exState)
        {
            Log.e(sTag, "Exception message : " + exState.getMessage());
        }

        return sJSONReadData;
    }

    public static Object getEventsForYear(AssetManager oAssets, int nYear)
    {
        sTag = "CalendarJSONWrapper.getEventsForMonth";
        try {
            oListOfEventsForYear.clear();

            String sJsonMonthFile = "holidays_" + nYear + ".json";
            Log.d(sTag, "Read file name ["+sJsonMonthFile+"].");

            String sJSONString = readFile(sJsonMonthFile, oAssets);

            if(sJSONString.length() == 0)
            {
                Log.d(sTag, "No JSON String read from file.");
                return null;
            }

            Gson gson = new Gson();
            JsonReader jsonReader = new JsonReader(new StringReader(sJSONString));

            //begin parsing
            CalendarEventYearClass oEventsForYear = gson.fromJson(jsonReader, CalendarEventYearClass.class);

            if(oEventsForYear == null)
                Log.d(sTag, "No events read from ["+sJsonMonthFile+"].");

            return oEventsForYear;

        }
        catch(Exception exState)
        {
            Log.e(sTag, "Exception message : " + exState.getMessage());
        }
        return oListOfEventsForYear;
    }

    public static boolean isHolidaysFileFound(AssetManager oAssets, int nYear)
    {
        try
        {
            String sJsonMonthFile = "holidays_" + nYear + ".json";
            Log.d(sTag, "Read file name ["+sJsonMonthFile+"].");

            String sJSONString = readFile(sJsonMonthFile, oAssets);

            if(sJSONString.length() == 0)
            {
                Log.d(sTag, "No JSON String read from file.");
                return false;
            }
            return true;
        }
        catch(Exception exState)
        {
            Log.e(sTag, "Exception message : " + exState.getMessage());
            return false;
        }
    }
}
