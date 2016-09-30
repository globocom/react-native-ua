package com.globo.reactnativeua;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.facebook.react.bridge.LifecycleEventListener;


public class ReactNativeUAReceiverHelper implements LifecycleEventListener {

    private static ReactNativeUAReceiverHelper instance = null;
    private Context context;
    private Intent pushIntent;

    private ReactNativeUAReceiverHelper(Context context) {
        this.context = context;
    }

    public static synchronized ReactNativeUAReceiverHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ReactNativeUAReceiverHelper(context);
        }
        return instance;
    }

    public void savePushIntent(Intent intent) {
        this.pushIntent = intent;
    }

    public void sendPushIntent() {
        if (pushIntent != null) {
            context.sendBroadcast(pushIntent);
            pushIntent = null;
        }
    }

    public boolean isActionUrl() {
        SharedPreferences preferences = context.getSharedPreferences(
                "enable_action_url",
                Context.MODE_PRIVATE);
        return preferences.getBoolean("isActionUrl", false);
    }

    public void setActionUrl(boolean isEnable) {
        SharedPreferences preferences = context.getSharedPreferences(
                "enable_action_url",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isActionUrl", isEnable);
        editor.apply();
    }

    @Override
    public void onHostResume() {
        sendPushIntent();
    }

    @Override
    public void onHostPause() {
    }

    @Override
    public void onHostDestroy() {
    }
}
