package com.globo.reactnativeua;

import android.content.Context;
import android.support.annotation.NonNull;

import com.urbanairship.AirshipReceiver;
import com.urbanairship.push.PushMessage;


public class ReactNativeUAReceiver extends AirshipReceiver {

    @Override
    protected void onPushReceived(@NonNull Context context, @NonNull PushMessage message, boolean notificationPosted) {
        if (ReactNativeUAEventEmitter.getInstance() != null) {
            ReactNativeUAEventEmitter.getInstance().sendEvent("onPushReceived", message);
        }
    }

    @Override
    protected void onNotificationPosted(@NonNull Context context, @NonNull NotificationInfo notificationInfo) {
        if (ReactNativeUAEventEmitter.getInstance() != null) {
            ReactNativeUAEventEmitter.getInstance().sendEvent("onNotificationPosted", notificationInfo.getMessage());
        }
    }

    @Override
    protected boolean onNotificationOpened(@NonNull Context context, @NonNull NotificationInfo notificationInfo) {
        if (ReactNativeUAEventEmitter.getInstance() != null) {
            ReactNativeUAEventEmitter.getInstance().sendEvent("onNotificationOpened", notificationInfo.getMessage());
        }

        return false;
    }

    @Override
    protected boolean onNotificationOpened(@NonNull Context context, @NonNull NotificationInfo notificationInfo, @NonNull ActionButtonInfo actionButtonInfo) {
        if (ReactNativeUAEventEmitter.getInstance() != null) {
            ReactNativeUAEventEmitter.getInstance().sendEvent("onNotificationOpened", notificationInfo.getMessage());
        }

        return false;
    }
}
