package factor.labs.indiancalendar.CalendarUI.CalendarViews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import factor.labs.indiancalendar.R;

/**
 * Created by hassanhussain on 7/21/2015.
 */
public class CalendarAboutUsFragment extends Fragment {

    View moAboutUsLayout;

    String sTag;

    public static CalendarAboutUsFragment init()
    {
        CalendarAboutUsFragment oFragment = new CalendarAboutUsFragment();
        return oFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sTag = "CalendarAboutUsFragment.onCreateView";
        super.onCreate(savedInstanceState);
        moAboutUsLayout = inflater.inflate(R.layout.layout_calendar_about_us, container, false);
        Log.d(sTag, "Created about us fragment.");
        return moAboutUsLayout;

    }

}
