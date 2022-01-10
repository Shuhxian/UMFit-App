package com.healthy.umfit.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

import static com.healthy.umfit.TagName.IS_LOGGED_IN;
import static com.healthy.umfit.TagName.LANG_PREF;

public class SharedPreferencesManager {
    public static final String TAG = SharedPreferencesManager.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String APP_PREFERENCES = "AppPrefs";

    public SharedPreferencesManager(Activity activity) {
        sharedPreferences = activity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public SharedPreferencesManager(Context context){
        sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void updatePref(String key, String value) {
        Log.d(TAG, "sharedpref all: " + sharedPreferences.getAll());
        if (sharedPreferences.contains(key)) {
            removePref(key);
        }

        editor = sharedPreferences.edit();

        editor.putString(key, value);

        editor.apply();
    }

    public void updatePref(String key, boolean value) {
        Log.d(TAG, "sharedpref all: " + sharedPreferences.getAll());
        if (sharedPreferences.contains(key)) {
            removePref(key);
        }

        editor = sharedPreferences.edit();

        editor.putBoolean(key, value);

        editor.apply();
    }

    public HashMap<String, String> getPrefList(String key) {
        HashMap<String, String> hmData = new HashMap<>();

        String[] keyArr = key.split("||");
        for (int i = 0; i < keyArr.length; i++) {
            hmData.put(keyArr[i], sharedPreferences.getString(keyArr[i], null));
        }

        return hmData;
    }

    public boolean hasKey(String key) {
        if (sharedPreferences.contains(key)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public boolean isHuamiTokenUpdateNeeded() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public String getLang(){
        return sharedPreferences.getString(LANG_PREF, "en");
    }

    public String getPref(String key) {
        return sharedPreferences.getString(key, null);
    }

    private void removePref(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public void clearAllPref(boolean isForce) {
        Log.d(TAG, "before clear: " + sharedPreferences.getAll());
        if (isForce) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }

        Log.d(TAG, "after clear: " + sharedPreferences.getAll());
    }

}
