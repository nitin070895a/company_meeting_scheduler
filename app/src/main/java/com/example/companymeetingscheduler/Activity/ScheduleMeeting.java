package com.example.companymeetingscheduler.Activity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.companymeetingscheduler.Helper.TimeAndDateUtils;
import com.example.companymeetingscheduler.R;

import java.util.Calendar;

/**
 * Screen that lets the user to schedule a meeting
 * Created by Nitin on 01/09/18.
 */
public class ScheduleMeeting extends AppCompatActivity implements View.OnClickListener {

    public static final String INTENT_DATA_DATE = "date";

    /**
     * The current date of the schedule page
     */
    private Calendar currentDate;

    private EditText startTime, endTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_meeting);

        Toolbar toolbar = findViewById(R.id.toolbar);
        EditText date = findViewById(R.id.date);

        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        Button submit = findViewById(R.id.submit);

        submit.setOnClickListener(this);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            // set the passed date to the date field
            date.setText(bundle.getString(INTENT_DATA_DATE));

            currentDate = TimeAndDateUtils.stringDateToCalendar(bundle.getString(INTENT_DATA_DATE));
        }

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                onBackPressed();

                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.startTime:

                showTimePicker(R.id.startTime);

                break;

            case R.id.endTime:

                showTimePicker(R.id.endTime);

                break;

            case R.id.submit:

                break;
        }
    }

    /**
     * Show a time picker on a field click, and sets the result in am/pm format
     * to the {@code editText} field
     *
     * @param id id of the field click
     */
    private void showTimePicker(final int id){


        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                String selTime = selectedHour + ":" + selectedMinute;

                // am pm format
                if(id == R.id.startTime)
                    startTime.setText(TimeAndDateUtils.convertStringDate(selTime, "HH:mm", TimeAndDateUtils.DEFAULT_TIME_FORMAT));
                else if(id == R.id.endTime)
                    endTime.setText(TimeAndDateUtils.convertStringDate(selTime, "HH:mm", TimeAndDateUtils.DEFAULT_TIME_FORMAT));

            }
        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false);

        timePickerDialog.show();

        timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,R.color.colorAccent));
        timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this,R.color.colorAccent));

    }
}
