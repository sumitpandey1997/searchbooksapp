package com.example.user.searchbooks;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by Ashutosh on 06-03-2018.
 */

public class MyPreferenceManager {

    public static void setUserDetail(Context context, UserDetails userDetails) {

        if (context != null) {
            Gson gson = new Gson();
            String objectString = gson.toJson(userDetails);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("userDetails", objectString);
            editor.commit();
        }
    }

    public static UserDetails getUserDetail(Context context) {

        if (context != null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            Gson gson = new Gson();

            String userDetailString = preferences.getString("userDetails", "");
            Log.d("userDetails",userDetailString);
            if (!userDetailString.isEmpty()) {
                UserDetails userDetails = gson.fromJson(userDetailString, UserDetails.class);
                return userDetails;
            } else {
                //return  new UserDetail(null);
                return null;
            }
        } else
            return null;
    }

    public static void deleteAllData(Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

}
