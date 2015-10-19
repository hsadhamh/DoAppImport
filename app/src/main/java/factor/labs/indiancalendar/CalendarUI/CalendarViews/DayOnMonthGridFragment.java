package factor.labs.indiancalendar.CalendarUI.CalendarViews;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import factor.labs.indiancalendar.CalendarAdapters.DayonMonthDateAdapter;
import factor.labs.indiancalendar.CalendarInterfaces.CalendarDateClickListenerInterface;
import factor.labs.indiancalendar.CalendarUtils.CalendarMonthClass;
import factor.labs.indiancalendar.DayOnMonthHomeActivity;
import factor.labs.indiancalendar.R;

/**
 * Created by hassanhussain on 9/30/2015.
 */
public class DayOnMonthGridFragment extends Fragment {
    View moGirdHolder  = null;
    GridView moMonthGrid = null;

    String sTag;
    int mnMonth, mnYear, mnSelectedDate;

    CalendarMonthClass moMonthObject = null;
    Activity moCallBack = null;
    CalendarDateClickListenerInterface moClickCallback = null;

    DayonMonthDateAdapter moDateAdapter = null;

    boolean mbViewCreated = false;

    public static DayOnMonthGridFragment init(int mon, int yr, Activity oCallback)
    {
        DayOnMonthGridFragment oDayOn = new DayOnMonthGridFragment();
        oDayOn.setMonth(mon);
        oDayOn.setYear(yr);
        oDayOn.PrepareMonth();
        oDayOn.setCallBack(oCallback);

        Bundle oArguments = new Bundle();
        oArguments.putInt("month", mon);
        oDayOn.setArguments(oArguments);
        return oDayOn;
    }

    public void setCallBack(Activity oCallback) {
        moCallBack = oCallback;
        moClickCallback = (CalendarDateClickListenerInterface)oCallback;
    }

    public DayOnMonthGridFragment(){
        super();
    }

    public void PrepareMonth(){
        moMonthObject = new CalendarMonthClass(mnMonth, mnYear);
        moMonthObject.prepareCalendarMonthDates();
    }

    public void setMonth(int mon){ mnMonth = mon; }
    public void setYear(int yr){ mnYear = yr; }

    public int getMonth(){ return mnMonth; }
    public int getYear(){ return mnYear; }

    public void setSelectedDate(int date){ mnSelectedDate = date; }
    public int getSelectedDate(){ return mnSelectedDate; }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sTag = "DayOnMonthGridFragment.onCreateView";
        try {
            moGirdHolder = (View) inflater.inflate(R.layout.layout_dayon_month_grid, container, false);
            moMonthGrid = (GridView) moGirdHolder.findViewById(R.id.id_cal_month_grid);

            moDateAdapter =
                    new DayonMonthDateAdapter(moMonthObject.getDateForGrid(), moCallBack.getApplicationContext(), moClickCallback);
            moMonthGrid.setAdapter(moDateAdapter);

            mbViewCreated = true;

            refreshMonthGridView();
            setGridTouchListener();

        } catch (Exception ex) {

        }
        return moGirdHolder;
    }

    public void refreshMonthGridView(){
        if(!mbViewCreated){
            onCreateView(moCallBack.getLayoutInflater(), null,null);
        }
        moDateAdapter.setGridSelectionDate(mnSelectedDate, mnMonth, mnYear);
        moDateAdapter.setGridDateValues(moMonthObject.getDateForGrid());
        moDateAdapter.notifyDataSetChanged();
    }

    public CalendarMonthClass getMonthClass(){ return moMonthObject; }

    public void setGridTouchListener(){
        moMonthGrid.setOnTouchListener(new View.OnTouchListener() {
            static final int MIN_DISTANCE = 100;
            // TODO change this runtime based on screen resolution.
            // for 1920x1080 is to small the 100 distance
            private float downX, downY, upX, upY;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        downX = event.getX();
                        downY = event.getY();
                        return true;
                    }
                    case MotionEvent.ACTION_UP: {
                        upX = event.getX();
                        upY = event.getY();
                        float deltaX = downX - upX;
                        float deltaY = downY - upY;
                        // swipe vertical?
                        if (Math.abs(deltaY) > MIN_DISTANCE) {
                            // top or down
                            if (deltaY < 0) {
                                // TopToBottomSwipe
                                return true;
                            }
                            if (deltaY > 0) {
                                // BottomToTopSwipe
                                ((DayOnMonthHomeActivity)moCallBack).onSwipeDetect();
                                return true;
                            }
                        }
                        return true; // no swipe horizontally and no swipe vertically
                    }// case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_MOVE: {
                        break;
                    }
                }
                return true;
            }
        });
    }
}
