package com.example.companymeetingscheduler.Helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Contains some helper methods that will be used for
 * networking purposes
 *
 * Created by Nitin on 01/09/18.
 */
public class NetworkHelper {

    /**
     * Tells if Internet is available or not
     * @param context The context of the caller
     * @return true/false for internet connected/disconnected respectively
     */
    public static boolean isConnected(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
