package com.example.companymeetingscheduler;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Represents the App
 * Contains all the things that will be common to the entire app
 *
 * Created by Nitin on 01/09/18.
 */
public class AppController extends Application {

    /**
     * Instance of the app
     */
    private static AppController instance;

    public static final String API_SCHEDULE = "http://fathomless-shelf-5846.herokuapp.com/api/schedule?";
    public static final String API_SCHEDULE_PARAM_DATE = "date";

    /**
     * RequestQueue i.e queue of API calls of {@link Volley}
     */
    private RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();

        // init the instance with the App on starup
        instance = this;
    }

    /**
     * Get the current instance of this app
     * @return the {@link AppController} instance of this app
     */
    public static synchronized AppController getInstance(){
        return instance;
    }

    /**
     * Returns the current request queue of {@link Volley}
     * @return the api call request queue
     */
    private RequestQueue getRequestQueue(){

        // first time
        if(requestQueue == null)
            requestQueue = Volley.newRequestQueue(getApplicationContext());

        return requestQueue;
    }

    /**
     * Add an api request call to the current {@link AppController#requestQueue}
     * @param request the request to be added in the queue
     * @param tag the tag that will be used to recognize the {@code request}
     */
    public void addToRequestQueue(Request request, String tag){
        request.setTag(tag);
        addToRequestQueue(request);
    }

    /**
     * Add an api call request to the current {@link AppController#requestQueue}
     * @param request the request to be added in the queue
     */
    public void addToRequestQueue(Request request){
        getRequestQueue().add(request);
    }

    /**
     * Cancel all the requested pending api calls with the {@code tag} specified
     * @param tag the tag of the api calls that are to be canceled
     */
    public void cancelPendingRequests(String tag){
        if(requestQueue != null)
            requestQueue.cancelAll(tag);
    }

}
