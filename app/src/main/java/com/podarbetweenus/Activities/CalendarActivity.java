package com.podarbetweenus.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.Toast;

import com.podarbetweenus.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by Gayatri on 1/31/2017.
 */
public class CalendarActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.calendar);
        HashSet<Date> events = new HashSet<>();
        events.add(new Date());

        Log.d("<<Date events", events.toString());

        ShowCalendarrActivity cv = ((ShowCalendarrActivity)findViewById(R.id.calendar_view));
        // cv.updateCalendar(events);
        cv.updateCalendar(events);

        // assign event handler
        cv.setEventHandler(new ShowCalendarrActivity.EventHandler()
        {
            @Override
            public void onDayLongPress(Date date)
            {
                // show returned day
                DateFormat df = SimpleDateFormat.getDateInstance();
               // Log.d("<<Date")
                Toast.makeText(CalendarActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("<<onBackPressed","CalenderActivity");

    }


}
