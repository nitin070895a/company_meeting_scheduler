package com.example.companymeetingscheduler.Helper;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.companymeetingscheduler.AppController;
import com.example.companymeetingscheduler.Interface.ApiCallListener;
import com.example.companymeetingscheduler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Contains the API calling mechanism using {@link com.android.volley.toolbox.Volley}
 *
 * Created by Nitin on 01/09/18.
 */
public class VolleyTask implements Response.ErrorListener, Response.Listener<JSONObject> {

    /**
     * Context of the caller
     */
    private Context context;

    /**
     * The url of the api that is required to be hit
     */
    private String URL;

    /**
     * The listener that will be listening to the Api call events
     * success, failure etc.
     */
    private ApiCallListener apiCallListener;

    /**
     * The request instance corresponding to the {@code URL} givev
     */
    private JsonObjectRequest jsonObjectRequest;

    /**
     * If the API was called and result was fetched successfully
     */
    private boolean success = false;

    /**
     * The message to be shown to the user in case of API failure
     */
    private String message;

    /**
     * Constructor
     * @param context the context of the caller
     * @param method either of {@link com.android.volley.Request.Method#GET} or {@link com.android.volley.Request.Method#POST}
     * @param URL the url that you want to hit
     * @param apiCallListener the class that will be listening to the api call events
     */
    public VolleyTask(Context context, int method, String URL, ApiCallListener apiCallListener) {

        this.context = context;
        this.URL = URL;
        this.apiCallListener = apiCallListener;

        // TODO: 01/09/18 Add stuff related to post methods

        // create a request with the {@code url} using the {@code method} specified
        jsonObjectRequest = new JsonObjectRequest(method, URL, null, this, this);

    }

    /**
     * Executes the API call based on the params passed inside the constructor
     * {@link VolleyTask#VolleyTask(Context, int, String, ApiCallListener)}
     */
    public void execute() {

        // add the request to the request queue, queued in {@link AppController}
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public final void onErrorResponse(VolleyError error) {
        // APi was failed

        success = false;
        message = context.getString(R.string.could_not_connect);

        // give the callbacks to the listener listening to the events
        apiCallListener.onApiResult(success, message, URL);
    }

    @Override
    public final void onResponse(JSONObject apiResponse) {
        // received response from the api call

        try {

            JSONArray result = apiResponse.getJSONArray("");

            success = true;

            // callback to the listener to do the parsing
            apiCallListener.parseResult(result, URL);

        }
        catch (JSONException e) {

            success = false;
            message = context.getString(R.string.there_was_a_problem);  // server/api error, i.e bad resposne from the API

            e.printStackTrace();
        }
        catch (Exception e) {

            success = false;
            message = context.getString(R.string.could_not_connect);  // null pointer exception i.e error in network

            e.printStackTrace();
        }

        // api execution along with the parsing is completed
        apiCallListener.onApiResult(success, message, URL);
    }
}