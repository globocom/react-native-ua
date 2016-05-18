package com.globo.reactnativeua;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.urbanairship.AirshipReceiver;
import com.urbanairship.push.PushMessage;

import com.globo.reactnativeua.ReactNativeUAEventEmitter;

public class ReactNativeUAReceiver extends AirshipReceiver {

    private static final String TAG = "ReactNativeUAReceiver";

    @Override
    protected void onPushReceived(@NonNull Context context, @NonNull PushMessage message, boolean notificationPosted) {
        ReactNativeUAEventEmitter.getInstance().sendEvent("onPushReceived", message);
    }

    @Override
    protected void onNotificationPosted(@NonNull Context context, @NonNull NotificationInfo notificationInfo) {
        ReactNativeUAEventEmitter.getInstance().sendEvent("onNotificationPosted", notificationInfo.getMessage());
    }

    @Override
    protected boolean onNotificationOpened(@NonNull Context context, @NonNull NotificationInfo notificationInfo) {
        ReactNativeUAEventEmitter.getInstance().sendEvent("onNotificationOpened", notificationInfo.getMessage());

        return false;
    }

    @Override
    protected boolean onNotificationOpened(@NonNull Context context, @NonNull NotificationInfo notificationInfo, @NonNull ActionButtonInfo actionButtonInfo) {
        ReactNativeUAEventEmitter.getInstance().sendEvent("onNotificationOpened", notificationInfo.getMessage());

        return false;
    }
}
