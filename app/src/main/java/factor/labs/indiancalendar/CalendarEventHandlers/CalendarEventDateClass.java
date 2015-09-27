package factor.labs.indiancalendar.CalendarEventHandlers;

import java.util.ArrayList;
import java.util.List;

import factor.labs.indiancalendar.CalendarDbHelper.CalendarEventMaster;

/**
 * Created by hassanhussain on 7/20/2015.
 */
public class CalendarEventDateClass implements Comparable{
    int Year;
    int Month;
    int Date;
    List<CalendarEventInfoClass> Events = new ArrayList<CalendarEventInfoClass>();

    List<CalendarEventMaster> oEvents = new ArrayList<CalendarEventMaster>();

    public int getDate() {return Date;}
    public int getYear() {return Year;}
    public int getMonth() {return Month;}

    public void setDate(int n) { Date = n; }
    public void setYear(int n) { Year = n; }
    public void setMonth(int n) { Month = n; }

    public String getFullDateString()
    {
        return Date + "-" + Month + "-" + Year;
    }

    public List<CalendarEventInfoClass> getEvents(){ return Events; }

    public List<CalendarEventMaster> getoEvents(){ return oEvents; }
    public void addEvent(CalendarEventMaster obj){ oEvents.add(obj); }

    @Override
    public int compareTo(Object another) {
        int date = ((CalendarEventDateClass)another).getDate();
        return this.getDate() - date;
    }
}
