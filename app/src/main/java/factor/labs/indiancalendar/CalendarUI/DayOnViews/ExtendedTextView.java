package factor.labs.indiancalendar.CalendarUI.DayOnViews;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;

/**
 * Created by hassanhussain on 10/17/2015.
 */
public class ExtendedTextView extends TextView {

    public ExtendedTextView(Context context) {
        super(context);
        setRobotoFont();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExtendedTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setRobotoFont();
    }

    public ExtendedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setRobotoFont();
    }

    public ExtendedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRobotoFont();
    }

    public void setRobotoFont(){
        setTypeface(labsCalendarUtils.getTypeFace());
    }
}
