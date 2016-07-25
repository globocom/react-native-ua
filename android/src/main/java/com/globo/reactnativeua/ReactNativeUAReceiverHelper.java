package com.globo.reactnativeua;

import android.content.Context;
import android.content.Intent;


public class ReactNativeUAReceiverHelper {

    private static ReactNativeUAReceiverHelper INSTANCE = null;

    private Context context;
    private Intent pushIntent;

    private ReactNativeUAReceiverHelper(Context context) { this.context = context; }

    public void savePushIntent(Intent intent) { this.pushIntent = intent; }

    public void sendPushIntent() {
        if (pushIntent != null) {
            context.sendBroadcast(pushIntent);
            pushIntent = null;
        }
    }

    public static ReactNativeUAReceiverHelper setup(Context context) {
        if (ReactNativeUAReceiverHelper.INSTANCE == null) {
            ReactNativeUAReceiverHelper.INSTANCE = new ReactNativeUAReceiverHelper(context);
        }

        return ReactNativeUAReceiverHelper.INSTANCE;
    }
}
