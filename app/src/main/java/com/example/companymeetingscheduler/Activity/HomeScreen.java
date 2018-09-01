package com.example.companymeetingscheduler.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.companymeetingscheduler.Adapter.MeetingsAdapter;
import com.example.companymeetingscheduler.AppController;
import com.example.companymeetingscheduler.Helper.NonAvailabilityHolder;
import com.example.companymeetingscheduler.Helper.TimeAndDateUtils;
import com.example.companymeetingscheduler.Helper.VolleyTask;
import com.example.companymeetingscheduler.Interface.ApiCallListener;
import com.example.companymeetingscheduler.Model.Meeting;
import com.example.companymeetingscheduler.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Home screen of the app contains the list of scheduled meetings
 * Created by Nitin on 01/09/18.
 */
public class HomeScreen extends AppCompatActivity implements ApiCallListener, View.OnClickListener {

    /**
     * The view where list of the scheduled meetings will be shown
     */
    private RecyclerView recyclerView;

    /**
     * The view that represents loading when the api call is being made
     */
    private ProgressBar progressBar;

    /**
     * Error view
     */
    private NonAvailabilityHolder nonAvailabilityHolder;

    /**
     * List of the selected date meetings
     */
    private ArrayList<Meeting> meetings = new ArrayList<>();

    /**
     * The selected date
     */
    private Date currentDate;

    /**
     * The title of the activity
     */
    private TextView titleDate;

    /**
     * Next and previous buttons
     */
    private LinearLayout next, prev;

    /**
     * Button to schedule meeting
     */
    private Button scheduleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // find views
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressbar);
        titleDate = findViewById(R.id.title);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        scheduleButton = findViewById(R.id.schedule);

        prev.setOnClickListener(this);
        next.setOnClickListener(this);
        scheduleButton.setOnClickListener(this);

        // create a view to handle errors
        nonAvailabilityHolder = new NonAvailabilityHolder(this, findViewById(android.R.id.content));
        nonAvailabilityHolder.setButtonVisibility(View.VISIBLE);
        nonAvailabilityHolder.setButton(getString(R.string.retry), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // refresh the current page
                fetchMeetings(currentDate);
            }
        });

        // get the current date
        currentDate = TimeAndDateUtils.getCurrentDate();

        // fetch the contents of the current date
        fetchMeetings(currentDate);
    }

    @Override
    public void onApiResult(boolean success, String message, String callingUrl) {
        // update ui here

        // hide loading view and show the list of scheduled meetings
        progressBar.setVisibility(View.GONE);
        prev.setEnabled(true);
        next.setEnabled(true);

        scheduleButton.setEnabled(currentDate.compareTo(TimeAndDateUtils.getCurrentDate()) >= 0);

        if(success && meetings.size() > 0) {

            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(new MeetingsAdapter(this, meetings));
        }
        else{
            nonAvailabilityHolder.setMessage(message);
            if(success && meetings.size() < 1)
                nonAvailabilityHolder.setMessage(getString(R.string.no_meetings));

            nonAvailabilityHolder.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void parseResult(JSONArray result, String callingUrl) {
        // do parsing here

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        meetings = new ArrayList<>(Arrays.asList(gson.fromJson(String.valueOf(result), Meeting[].class)));

        final SimpleDateFormat format = new SimpleDateFormat(TimeAndDateUtils.DEFAULT_TIME_FORMAT, Locale.ENGLISH);

        Collections.sort(meetings, new Comparator<Meeting>() {
            @Override
            public int compare(Meeting o1, Meeting o2) {

                try {
                    Date o1date = format.parse(o1.getStartTime());
                    Date o2date = format.parse(o2.getStartTime());

                    return o1date.compareTo(o2date);
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }

                return 0;
            }
        });
    }

    /**
     * Makes an api call based on the passed {@code date}
     * and fetches the scheduled meetings for that date.
     *
     * @param date the date in format 'dd/mm/yyyy' at which the scheduled meetings are desired
     */
    private void fetchMeetings(Date date) {
        meetings.clear(); // clear old meetings

        String strDate = TimeAndDateUtils.getDateInFormat(date, TimeAndDateUtils.DEFAULT_DATE_FORMAT);

        // set the title of the activity to the current date
        titleDate.setText(TimeAndDateUtils.convertStringDate(strDate,
                TimeAndDateUtils.DEFAULT_DATE_FORMAT, "dd-MM-yyyy"));

        // show loading view
        recyclerView.setVisibility(View.GONE);
        nonAvailabilityHolder.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        prev.setEnabled(false);
        next.setEnabled(false);
        scheduleButton.setEnabled(false);

        // make hash-map containing all the required params by the api
        HashMap<String, Object> params = new HashMap<>();
        params.put(AppController.API_SCHEDULE_PARAM_DATE, strDate);

        // call the api using a .GET request
        new VolleyTask(this,
                Request.Method.GET,
                AppController.API_SCHEDULE,
                params, this)
                .execute();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.next:

                currentDate = TimeAndDateUtils.addDaysToADate(currentDate, 1, TimeAndDateUtils.DEFAULT_DATE_FORMAT);
                fetchMeetings(currentDate);

                break;

            case R.id.prev:

                currentDate = TimeAndDateUtils.addDaysToADate(currentDate, -1, TimeAndDateUtils.DEFAULT_DATE_FORMAT);
                fetchMeetings(currentDate);

                break;

            case R.id.schedule:

                // take the user to the schedule a meeting page
                Intent intent = new Intent(this, ScheduleMeeting.class);
                intent.putExtra(ScheduleMeeting.INTENT_DATA_DATE,
                        TimeAndDateUtils.getDateInFormat(currentDate, TimeAndDateUtils.DEFAULT_DATE_FORMAT));
                startActivity(intent);

                break;
        }
    }

}
