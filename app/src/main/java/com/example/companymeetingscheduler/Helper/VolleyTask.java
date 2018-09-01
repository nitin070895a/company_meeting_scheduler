package com.example.companymeetingscheduler.Helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.companymeetingscheduler.AppController;
import com.example.companymeetingscheduler.Interface.ApiCallListener;
import com.example.companymeetingscheduler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Contains the API calling mechanism using {@link com.android.volley.toolbox.Volley}
 *
 * Created by Nitin on 01/09/18.
 */
public class VolleyTask implements Response.ErrorListener, Response.Listener<JSONArray> {

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
    private JsonArrayRequest jsonArrayRequest;

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
     * @param params the parameters to be sent via the {@code URL} api
     * @param apiCallListener the class that will be listening to the api call events
     *
     */
    public VolleyTask(Context context, int method, String URL, HashMap<String, Object> params,
                      ApiCallListener apiCallListener) {

        this.context = context;
        this.URL = URL;
        this.apiCallListener = apiCallListener;

        if (method == Request.Method.GET) {

            if(params != null) {

                Iterator it = params.entrySet().iterator();
                while (it.hasNext()) {

                    Map.Entry pair = (Map.Entry)it.next();
                    this.URL += pair.getKey() + "=" + pair.getValue() + "&";

                    it.remove(); // avoids a ConcurrentModificationException
                }
            }
        }

        // TODO: 01/09/18 Add stuff related to post methods

        Log.i(this.getClass().getSimpleName(), "Api call to " + this.URL);

        // create a request with the {@code url} using the {@code method} specified
        jsonArrayRequest = new JsonArrayRequest(method, this.URL, null, this, this);

    }

    /**
     * Executes the API call based on the params passed inside the constructor
     * {@link VolleyTask#VolleyTask(Context, int, String, HashMap, ApiCallListener)}
     */
    public void execute() {

        // check if internet is available
        if(NetworkHelper.isConnected(context)) {

            // add the request to the request queue, queued in {@link AppController}
            AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        }
        else{
            onErrorResponse(new VolleyError());
        }

    }

    @Override
    public final void onErrorResponse(VolleyError error) {
        // APi was failed

        success = false;
        message = context.getString(R.string.could_not_connect);

        // give the callbacks to the listener listening to the events
        apiCallListener.onApiResult(success, message, URL);

        error.printStackTrace();
    }

    @Override
    public void onResponse(JSONArray apiResponse) {
        // received response from the api call

        try {

            // you can do any pre-parsing here, i.e common stuff related to the server,
            // i.e encryption-decryption, some common params received, i.e database query success,
            // common message from the server
            JSONObject testPreProcessing = (JSONObject) apiResponse.get(0);
            Log.d(this.getClass().getSimpleName(), "Test Data " + testPreProcessing.toString());

            success = true;
            message = context.getString(R.string.success);

            // callback to the listener to do the parsing
            apiCallListener.parseResult(apiResponse, URL);

        }
        catch (JSONException e) {

            // will be cause from any-pre parsing

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