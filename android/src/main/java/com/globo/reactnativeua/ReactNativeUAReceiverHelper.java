package com.globo.reactnativeua;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


public class ReactNativeUAReceiverHelper {

    private static ReactNativeUAReceiverHelper INSTANCE = null;

    private Context context;
    private Intent pushIntent;

    private ReactNativeUAReceiverHelper(Context context) {
        this.context = context;
    }

    public void savePushIntent(Intent intent) { this.pushIntent = intent; }

    public void sendPushIntent() {
        if (pushIntent != null) {
            context.sendBroadcast(pushIntent);
            pushIntent = null;
        }
    }

    public boolean isActionUrl() {
        SharedPreferences preferences = context.getSharedPreferences("enable_action_url", Context.MODE_PRIVATE);

        return preferences.getBoolean("isActionUrl", false);
    }

    public void setActionUrl(boolean isEnable) {
        SharedPreferences preferences = context.getSharedPreferences("enable_action_url", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isActionUrl", isEnable);
        editor.commit();
    }

    public static ReactNativeUAReceiverHelper setup(Context context) {
        if (ReactNativeUAReceiverHelper.INSTANCE == null) {
            ReactNativeUAReceiverHelper.INSTANCE = new ReactNativeUAReceiverHelper(context);
        }

        return ReactNativeUAReceiverHelper.INSTANCE;
    }
}
