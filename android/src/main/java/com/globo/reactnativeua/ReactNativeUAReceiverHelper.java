package com.globo.reactnativeua;

import android.content.Context;
import android.content.Intent;

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
