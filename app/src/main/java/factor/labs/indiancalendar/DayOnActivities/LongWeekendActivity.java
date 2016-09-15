package factor.labs.indiancalendar.DayOnActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import factor.labs.indiancalendar.LongWeekend.LongWeekendSuggestions;
import factor.labs.indiancalendar.R;

/**
 * Created by hassanhussain on 9/15/2016.
 */
public class LongWeekendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekend_layout_main);
        TextView vew = (TextView)findViewById(R.id.textView);

        vew.setText("Hellow");

        LongWeekendSuggestions suggest = new LongWeekendSuggestions(getApplicationContext(), 1, 2017);
        suggest.LoadMonthInfo();
        suggest.onPrepareListDates();
        suggest.onCalculateLongWeekends();
        String s = "";
        for(LongWeekendSuggestions.LongWeekend weekend : suggest.getLongWeekendList()){
            s += (" [" + weekend.tag + "] :: " );
        }

        vew.setText(s);
    }


}
