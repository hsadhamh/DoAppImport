package factor.labs.indiancalendar.CalendarUI.DayOnViews;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;

/**
 * Created by hassanhussain on 10/17/2015.
 */
public class ExtendedButton extends Button {
    public ExtendedButton(Context context) {
        super(context);
        setRobotoFont();
    }

    public ExtendedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRobotoFont();
    }

    public ExtendedButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setRobotoFont();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExtendedButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setRobotoFont();
    }

    public void setRobotoFont(){
        setTypeface(labsCalendarUtils.getTypeFace());
    }
}
