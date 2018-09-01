package com.example.companymeetingscheduler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.example.companymeetingscheduler.Adapter.MeetingsAdapter;
import com.example.companymeetingscheduler.Helper.NonAvailabilityHolder;
import com.example.companymeetingscheduler.Helper.TimeAndDateUtils;
import com.example.companymeetingscheduler.Helper.VolleyTask;
import com.example.companymeetingscheduler.Interface.ApiCallListener;
import com.example.companymeetingscheduler.Model.Meeting;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Home screen of the app contains the list of scheduled meetings
 * Created by Nitin on 01/09/18.
 */
public class HomeScreen extends AppCompatActivity implements ApiCallListener {

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
    private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // find views
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressbar);

        // setup the toolbar
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

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
        currentDate = TimeAndDateUtils.getCurrentDateInDefaultFormat();

        // fetch the contents of the current date
        fetchMeetings(currentDate);
    }

    @Override
    public void onApiResult(boolean success, String message, String callingUrl) {
        // update ui here

        // hide loading view and show the list of scheduled meetings
        progressBar.setVisibility(View.GONE);

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

        meetings.clear(); // clear old meetings

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        meetings = new ArrayList<>(Arrays.asList(gson.fromJson(String.valueOf(result), Meeting[].class)));
    }

    /**
     * Makes an api call based on the passed {@code date}
     * and fetches the scheduled meetings for that date.
     *
     * @param date the date in format 'dd/mm/yyyy' at which the scheduled meetings are desired
     */
    private void fetchMeetings(String date) {

        // show loading view
        recyclerView.setVisibility(View.GONE);
        nonAvailabilityHolder.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        // make hash-map containing all the required params by the api
        HashMap<String, Object> params = new HashMap<>();
        params.put(AppController.API_SCHEDULE_PARAM_DATE, date);

        // call the api using a .GET request
        new VolleyTask(this,
                Request.Method.GET,
                AppController.API_SCHEDULE,
                params, this)
                .execute();

    }

}
