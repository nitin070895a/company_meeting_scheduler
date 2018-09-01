package com.example.companymeetingscheduler.Interface;

import org.json.JSONArray;

/**
 * The listener invoked from {@link com.example.companymeetingscheduler.Helper.VolleyTask}
 * Contains the callback methods from the API call, i.e success, failure etc.
 *
 * Created by Nitin on 01/09/18.
 */
public interface ApiCallListener {

    /**
     * Called after the execution of the API is completed along with the parsing i.e
     * after {@link ApiCallListener#parseResult(JSONArray, String)} callback
     *
     * <br>
     * <br>
     * Called irrespective of the {@code success} failure/success of the api
     *
     * <br>
     * <br>
     * <b>Note:</b>  Do the UI Stuff here
     *
     * @param success true if api executed successfully
     * @param message the message to be shown to the user in case of failure
     * @param callingUrl the URL of the called API
     */
    void onApiResult(boolean success, String message, String callingUrl);

    /**
     * Called when the response of the API is received
     * Parsing should be done in this method
     *
     * <br>
     * <br>
     * This callback method is only called when api executes with success,
     * it won't be executed for failure case
     *
     * <br>
     * <br>
     * <b>Note:</b> Do not do any UI stuff here, for updating the UI use method
     * {@link ApiCallListener#onApiResult(boolean, String, String)}
     *
     * @param result The result array from the response of the API
     * @param callingUrl the URL of the called API
     */
    void parseResult(JSONArray result, String callingUrl);
}
