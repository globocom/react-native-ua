package com.globo.reactnativeua;

import android.content.Context;
import android.content.SharedPreferences;

import com.urbanairship.UAirship;

public class PreferencesManager {

    private final String PREFS_NODE = "preferences";
    private final String PREFS_ANDROID_SMALL_ICON = "prefs_android_small_icon";
    private final String PREFS_ANDROID_LARGE_ICON = "prefs_android_large_icon";
    private final String PREFS_ENABLED_ACTION_URL = "prefs_enabled_action_url";
    private static PreferencesManager instance;
    private SharedPreferences preferences;

    private PreferencesManager() {
        preferences = UAirship.getApplicationContext().getSharedPreferences(
                PREFS_NODE, Context.MODE_PRIVATE);
    }

    public static PreferencesManager getInstance() {
        if (instance == null) {
            instance = new PreferencesManager();
        }
        return instance;
    }

    public int getAndroidSmallIconResourceId() {
        return preferences.getInt(PREFS_ANDROID_SMALL_ICON, 0);
    }

    public void setAndroidSmallIconResourceId(int iconResourceId) {
        preferences.edit()
                .putInt(PREFS_ANDROID_SMALL_ICON, iconResourceId)
                .apply();
    }

    public int getAndroidLargeIconResourceId() {
        return preferences.getInt(PREFS_ANDROID_LARGE_ICON, 0);
    }

    public void setAndroidLargeIconResourceId(int iconResourceId) {
        preferences.edit()
                .putInt(PREFS_ANDROID_LARGE_ICON, iconResourceId)
                .apply();
    }

    public boolean isEnabledActionUrl() {
        return preferences.getBoolean(PREFS_ENABLED_ACTION_URL, false);
    }

    public void setEnabledActionUrl(boolean actionUrl) {
        preferences.edit()
                .putBoolean(PREFS_ENABLED_ACTION_URL, actionUrl)
                .apply();
    }

}
