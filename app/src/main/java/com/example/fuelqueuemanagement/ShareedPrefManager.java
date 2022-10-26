package com.example.fuelqueuemanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.fuelqueuemanagement.LoginActivity;
import com.example.fuelqueuemanagement.StationOwnerSignupActivity;
import com.example.fuelqueuemanagement.dto.FuelStation;

class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "fuelqueuemanagement";
    private static final String KEY_ID = "keyid";
    private static SharedPrefManager mInstance;
    private static Context ctx;

    private SharedPrefManager(Context context)
    {
        ctx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //Store the user data in shared preferences
    public void stationLogin(FuelStation fuelStation) {

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, fuelStation.getId());
        editor.apply();
    }

    //Check whether the user has already logged in or not
    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ID, null) != null;
    }

    //Provide the logged in user
    public String getStationID() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return  sharedPreferences.getString(KEY_ID, null);
    }

    //Logout the user
    public void logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        ctx.startActivity(new Intent(ctx, LoginActivity.class));
    }
}
