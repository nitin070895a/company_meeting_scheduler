package com.example.companymeetingscheduler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.example.companymeetingscheduler.Helper.TimeAndDateUtils;
import com.example.companymeetingscheduler.Helper.VolleyTask;
import com.example.companymeetingscheduler.Interface.ApiCallListener;

import org.json.JSONArray;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressbar);

        fetchMeetings(TimeAndDateUtils.getCurrentDateInDefaultFormat());
    }

    @Override
    public void onApiResult(boolean success, String message, String callingUrl) {
        // update ui here

        // hide loading view and show the list of scheduled meetings
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void parseResult(JSONArray result, String callingUrl) {
        // do parsing here

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
