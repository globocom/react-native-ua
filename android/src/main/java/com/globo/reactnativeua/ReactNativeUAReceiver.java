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
        Log.i(TAG, "Received push message. Alert: " + message.getAlert() + ". posted notification: " + notificationPosted);
        ReactNativeUAEventEmitter.getInstance().sendEvent("receivedNotification", message);
    }

    @Override
    protected void onNotificationPosted(@NonNull Context context, @NonNull NotificationInfo notificationInfo) {
        Log.i(TAG, "Notification posted. Alert: " + notificationInfo.getMessage().getAlert() + ". NotificationId: " + notificationInfo.getNotificationId());
        ReactNativeUAEventEmitter.getInstance().sendEvent("receivedNotification", notificationInfo.getMessage());
    }

    @Override
    protected boolean onNotificationOpened(@NonNull Context context, @NonNull NotificationInfo notificationInfo) {
        Log.i(TAG, "Notification opened. Alert: " + notificationInfo.getMessage().getAlert() + ". NotificationId: " + notificationInfo.getNotificationId());
        ReactNativeUAEventEmitter.getInstance().sendEvent("receivedNotification", notificationInfo.getMessage());

        return false;
    }

    @Override
    protected boolean onNotificationOpened(@NonNull Context context, @NonNull NotificationInfo notificationInfo, @NonNull ActionButtonInfo actionButtonInfo) {
        Log.i(TAG, "Notification action button opened. Button ID: " + actionButtonInfo.getButtonId() + ". NotificationId: " + notificationInfo.getNotificationId());
        ReactNativeUAEventEmitter.getInstance().sendEvent("receivedNotification", notificationInfo.getMessage());

        return false;
    }
}
