package factor.labs.indiancalendar.LongWeekend;

import android.content.Context;
import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import factor.labs.indiancalendar.CalendarObjects.CalendarMonthYearClass;
import factor.labs.indiancalendar.CalendarUtils.CalendarDateClass;
import factor.labs.indiancalendar.CalendarUtils.CalendarMonthClass;
import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;

/**
 * Created by hassanhussain on 9/15/2016.
 */
public class LongWeekendSuggestions {
    int mMonth, mYear;
    List<LongWeekend> mListWeekend = new ArrayList<>();
    List<CalendarDateClass> mListDates = new ArrayList<>();
    Context mContext;
    CalendarMonthClass mCurrentMonth, mAfterMonth, mBeforeMonth;

    int mWeekDaysThreshold = 1;
    /* 0 - 1 SUN - 2 MON - 3 TUE - 4 WED - 5 THU - 6 FRI - 7 SAT */
    SparseBooleanArray mWeekDaysSelected = new SparseBooleanArray(8);

    public class LongWeekend{
        public int totCountDays;
        public List<CalendarDateClass> listDate;
        public List<CalendarDateClass> listWeekDate;
        public String tag;
        public int totWeekDays;
    }

    public List<LongWeekend> getLongWeekendList() { return mListWeekend; }

    public LongWeekendSuggestions(Context context, int m, int y){
        mContext = context;
        mMonth = m;
        mYear = y;

        for(int i=0; i<8; i++)
            mWeekDaysSelected.put(i, false);
    }

    public void LoadMonthInfo(){
        mWeekDaysSelected.put(1, true);
        mWeekDaysSelected.put(7, true);
        mCurrentMonth = LoadMonthInfo(mMonth, mYear);

        CalendarMonthYearClass monYr = new CalendarMonthYearClass(mMonth, mYear);
        CalendarMonthYearClass retMonYr = labsCalendarUtils.subtractMonthYear(monYr, 1);
        mBeforeMonth = LoadMonthInfo(retMonYr.getMonth(), retMonYr.getYear());

        monYr = new CalendarMonthYearClass(mMonth, mYear);
        retMonYr = labsCalendarUtils.addMonthYear(monYr, 1);
        mAfterMonth = LoadMonthInfo(retMonYr.getMonth(), retMonYr.getYear());
    }

    public CalendarMonthClass LoadMonthInfo(int m, int y){
        CalendarMonthClass month = new CalendarMonthClass(m, y);
        month.setmContext(mContext);
        month.prepareCalendarMonthDates();
        month.getEventsForMonth();
        return month;
    }

    public void onPrepareListDates(){
        int weekDayCount = 0;
        for(int n = (mBeforeMonth.getDateForGrid().size()-1); n >= 0; n-- ){
            CalendarDateClass currDate = mBeforeMonth.getDateForGrid().get(n);
            if(currDate.isCurrentMonthDate()) {
                if (!currDate.hasHolidayFlagSet())
                    weekDayCount++;

                if (weekDayCount <= mWeekDaysThreshold) {
                    mListDates.add(0, currDate);
                } else {
                    break;
                }
            }
        }

        for(CalendarDateClass date : mCurrentMonth.getDateForGrid()){
            if(date.isCurrentMonthDate()) {
                mListDates.add(date);
            }
        }

        weekDayCount = 0;
        for(CalendarDateClass date : mAfterMonth.getDateForGrid()){
            if(date.isCurrentMonthDate()) {
                if (!date.hasHolidayFlagSet())
                    weekDayCount++;
                if (weekDayCount <= mWeekDaysThreshold) {
                    mListDates.add(date);
                } else {
                    break;
                }
            }
        }
    }

    public void onCalculateLongWeekends(){
        int totOffsWeek = 0;
        for(int n = 1; n < mWeekDaysSelected.size(); n++) {
            if(mWeekDaysSelected.get(n))
                totOffsWeek++;
        }

        /* Any weekend can be taken as leave */
        if((totOffsWeek + mWeekDaysThreshold) > 7)
        {
            LongWeekend weekend = new LongWeekend();
            weekend.tag = "";
            weekend.listDate = new ArrayList<>();
            weekend.listWeekDate = new ArrayList<>();
            weekend.totCountDays = 0;
            weekend.totWeekDays = 0;

            //  all days will be holidays ;)
            for(CalendarDateClass date : mCurrentMonth.getDateForGrid()){
                weekend.tag += ((weekend.tag.length() > 0 ? ("->") : ("")) + date.getDate());
            }
        }
        else
        {
            for(int n=0; n<mListDates.size(); n++){
                // start index & find the week.
                onCalculateWeekendFrom(n);
            }

            /* sort list based on number of days and weekdays */
            Collections.sort(mListWeekend, new Comparator<LongWeekend>() {
                @Override
                public int compare(LongWeekend lhs, LongWeekend rhs) {
                    if(lhs.totCountDays > rhs.totCountDays)
                        return -1;
                    else if(lhs.totCountDays < rhs.totCountDays)
                        return 1;
                    else if(lhs.totCountDays == rhs.totCountDays) {
                        if (lhs.totWeekDays > rhs.totWeekDays)
                            return -1;
                        else if (lhs.totWeekDays < rhs.totWeekDays)
                            return 1;
                    }
                    return 0;
                }
            });


        }
    }

    public void onCalculateWeekendFrom(int index){
        int weekDaysCount = 0;
        List<CalendarDateClass> listDates = new ArrayList<>();
        List<CalendarDateClass> weekDate = new ArrayList<>();
        String tag = "";
        int totCountDays = 0;
        for(int i = index; i < mListDates.size(); i++){
            CalendarDateClass currDate = mListDates.get(i);
            if(!currDate.hasHolidayFlagSet() && !(mWeekDaysSelected.get(currDate.getWeekNameNumber()))) {
                weekDaysCount++;
                weekDate.add(currDate);
            }

            if(weekDaysCount <= mWeekDaysThreshold){
                tag += ((tag.length() > 0 ? ("->") : ("")) + currDate.getDate());
                listDates.add(currDate);
                totCountDays++;
            }
            else{
                break;
            }
        }

        if(!listDates.isEmpty()){
            LongWeekend weekend = new LongWeekend();
            weekend.tag = tag;
            weekend.listDate = new ArrayList<>();
            weekend.listDate.addAll(listDates);
            weekend.listWeekDate = new ArrayList<>();
            weekend.listWeekDate.addAll(weekDate);
            weekend.totCountDays = totCountDays;
            weekend.totWeekDays = weekDaysCount;

            mListWeekend.add(weekend);
        }
    }

}
