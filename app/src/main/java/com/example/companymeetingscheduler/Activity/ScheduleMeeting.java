package com.example.companymeetingscheduler.Activity;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.companymeetingscheduler.Helper.TimeAndDateUtils;
import com.example.companymeetingscheduler.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Screen that lets the user to schedule a meeting
 * Created by Nitin on 01/09/18.
 */
public class ScheduleMeeting extends AppCompatActivity implements View.OnClickListener {

    public static final String INTENT_DATA_DATE = "date";
    public static final String INTENT_DATA_START_TIMES = "start_times";
    public static final String INTENT_DATA_END_TIMES = "end_times";

    /**
     * The current date of the schedule page
     */
    private Calendar currentDate;

    private EditText startTime, endTime, desc;

    // all the start and end times of the {@code currentDate}
    private ArrayList<String> startTimes = new ArrayList<>();
    private ArrayList<String> endTimes = new ArrayList<>();

    // will be sued to show messages
    private Snackbar snackbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_meeting);

        Toolbar toolbar = findViewById(R.id.toolbar);
        EditText date = findViewById(R.id.date);

        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        desc = findViewById(R.id.description);
        Button submit = findViewById(R.id.submit);

        submit.setOnClickListener(this);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            // set the passed date to the date field
            date.setText(bundle.getString(INTENT_DATA_DATE));

            currentDate = TimeAndDateUtils.stringDateToCalendar(bundle.getString(INTENT_DATA_DATE), TimeAndDateUtils.DEFAULT_DATE_FORMAT);

            ArrayList<String> startTimes = bundle.getStringArrayList(INTENT_DATA_START_TIMES);
            ArrayList<String> endTimes = bundle.getStringArrayList(INTENT_DATA_END_TIMES);

            // init the activities start and end times
            if(startTimes != null) this.startTimes = new ArrayList<>(startTimes);
            if(endTimes != null) this.endTimes = new ArrayList<>(endTimes);

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

                submit();

                break;
        }
    }

    /**
     * Submit button is pressed
     */
    private void submit() {

        // check if any field is left empty
        if(startTime.getText().toString().equals("")
                || endTime.getText().toString().equals("")
                || desc.getText().toString().equals("")) {

            showSnackBar(getString(R.string.please_fill_the_form), Snackbar.LENGTH_SHORT);
            return;
        }

        // get the date instances of the selected start and end times
        Date selectedStartTime = TimeAndDateUtils.stringDateToCalendar(startTime.getText().toString(), TimeAndDateUtils.DEFAULT_TIME_FORMAT).getTime();
        Date selectedEndTime = TimeAndDateUtils.stringDateToCalendar(endTime.getText().toString(), TimeAndDateUtils.DEFAULT_TIME_FORMAT).getTime();

        // will hold the availability of the desired meeting
        boolean available = true;

        // loop through all the meetings on this day
        for(int i=0; i<startTimes.size(); i++) {

            // start and end date of the i'th meeting
            String formattedSTime = TimeAndDateUtils.convertStringDate(startTimes.get(i), "HH:mm", TimeAndDateUtils.DEFAULT_TIME_FORMAT);
            Calendar sCal = TimeAndDateUtils.stringDateToCalendar(formattedSTime, TimeAndDateUtils.DEFAULT_TIME_FORMAT);

            String formattedETime = TimeAndDateUtils.convertStringDate(endTimes.get(i), "HH:mm", TimeAndDateUtils.DEFAULT_TIME_FORMAT);
            Calendar eCal = TimeAndDateUtils.stringDateToCalendar(formattedETime, TimeAndDateUtils.DEFAULT_TIME_FORMAT);

            if(sCal != null && eCal != null) {

                // start and end date of the i'th meeting
                Date sDate = sCal.getTime();
                Date eDate = eCal.getTime();

                Log.e("Hurray", "Sel start time " + selectedStartTime.toString());
                Log.e("Hurray", "Sel end time " + selectedEndTime.toString());

                Log.e("Hurray", "Meeting start time " + sDate.toString());
                Log.e("Hurray", "Meeting end time " + eDate.toString());

                // check if the start time or end time of the desired meeting is
                // in between the start and end time of any other meeting
                if(
                        (selectedStartTime.compareTo(sDate) >= 0 && selectedStartTime.compareTo(eDate) < 0) ||
                                (selectedEndTime.compareTo(sDate) >0 && selectedEndTime.compareTo(eDate) <=0) ||
                                (selectedStartTime.compareTo(sDate) <0 && selectedEndTime.compareTo(eDate) > 0 )

                        ) {

                    // this meeting is in the time window of any of the other meetings of this day
                    available = false;

                    break;
                }
            }
        }

        showSnackBar(available ? getString(R.string.slot_av) : getString(R.string.slot_not_av), Snackbar.LENGTH_LONG);
    }

    /**
     * Displays a snack bar at the bottom of the screen
     * @param message the message to be shown in the snackbar
     * @param duration the duration in which the snackbar will be visible
     */
    public void showSnackBar(String message, int duration){

        snackbar = Snackbar.make(findViewById(android.R.id.content), message, duration);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);

        snackbar.show();
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
