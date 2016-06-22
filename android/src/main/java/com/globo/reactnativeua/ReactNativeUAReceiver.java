package com.globo.reactnativeua;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.urbanairship.AirshipReceiver;
import com.urbanairship.push.PushMessage;

import java.util.List;


public class ReactNativeUAReceiver extends AirshipReceiver {

    @Override
    protected void onPushReceived(@NonNull Context context, @NonNull PushMessage message, boolean notificationPosted) {
        boolean isRunning = isApplicationRunning(context);

        if (isRunning && ReactNativeUAEventEmitter.getInstance() != null) {
            ReactNativeUAEventEmitter.getInstance().sendEvent("onNotificationOpened", message);
        }
    }

    @Override
    protected boolean onNotificationOpened(@NonNull Context context, @NonNull NotificationInfo notificationInfo) {
        Intent intent = new Intent();

        intent.setAction("com.urbanairship.push.RECEIVED");
        intent.putExtra("com.urbanairship.push.EXTRA_PUSH_MESSAGE_BUNDLE", notificationInfo.getMessage().getPushBundle());

        context.sendBroadcast(intent);

        return false;
    }

    private boolean isApplicationRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
        
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
            if (processInfo.processName.equals(context.getPackageName())) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String d: processInfo.pkgList) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
