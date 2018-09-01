package com.example.companymeetingscheduler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.example.companymeetingscheduler.Helper.VolleyTask;
import com.example.companymeetingscheduler.Interface.ApiCallListener;

import org.json.JSONArray;

import java.util.HashMap;

/**
 * Home screen of the app contains the list of scheduled meetings
 * Created by Nitin on 01/09/18.
 */
public class HomeScreen extends AppCompatActivity implements ApiCallListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        HashMap<String, Object> params = new HashMap<>();
        params.put(AppController.API_SCHEDULE_PARAM_DATE, "7/8/2015");

        new VolleyTask(this,
                Request.Method.GET,
                AppController.API_SCHEDULE,
                params, this)
                .execute();
    }

    @Override
    public void onApiResult(boolean success, String message, String callingUrl) {

        Log.e("HomeScreen", "Result " + success);
    }

    @Override
    public void parseResult(JSONArray result, String callingUrl) {

        Log.e("HomeScreen", "Result " + result.toString());
    }
}
