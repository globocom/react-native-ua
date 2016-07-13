package com.globo.reactnativeua;

import android.content.Context;
import android.content.Intent;

import com.facebook.react.bridge.LifecycleEventListener;


public class ReactNativeUAReceiverHelper implements LifecycleEventListener {

    private static ReactNativeUAReceiverHelper INSTANCE = null;

    private Context context;
    private Intent pushIntent;

    private ReactNativeUAReceiverHelper(Context context) { this.context = context; }

    public void savePushIntent(Intent intent) { this.pushIntent = intent; }

    @Override
    public void onHostResume() {
        if (pushIntent != null) {
            context.sendBroadcast(pushIntent);
            pushIntent = null;
        }
    }

    @Override
    public void onHostPause() { }

    @Override
    public void onHostDestroy() { }

    public static ReactNativeUAReceiverHelper setup(Context context) {
        if (ReactNativeUAReceiverHelper.INSTANCE == null) {
            ReactNativeUAReceiverHelper.INSTANCE = new ReactNativeUAReceiverHelper(context);
        }

        return ReactNativeUAReceiverHelper.INSTANCE;
    }
}
