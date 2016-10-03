package com.globo.reactnativeua;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.urbanairship.AirshipReceiver;
import com.urbanairship.push.PushMessage;
import com.urbanairship.actions.Action;
import com.urbanairship.actions.ActionRunRequest;
import com.urbanairship.actions.OpenExternalUrlAction;

import java.util.List;


public class ReactNativeUAReceiver extends AirshipReceiver {

    @Override
    protected void onPushReceived(@NonNull Context context, @NonNull PushMessage message,
                                  boolean notificationPosted) {
        boolean isRunning = isApplicationRunning(context);

        if (isRunning && ReactNativeUAEventEmitter.getInstance() != null) {
            if (message.getActions().get("^u") != null
                    && PreferencesManager.getInstance().isEnabledActionUrl()) {
                ActionRunRequest.createRequest(new OpenExternalUrlAction())
                        .setSituation(Action.SITUATION_MANUAL_INVOCATION)
                        .setValue(message.getActions().get("^u")).run();
                return;
            }
            String event = message.getPushBundle().getString("Event", "onNotificationReceived");
            ReactNativeUAEventEmitter.getInstance().sendEvent(event, message);
        }
    }

    @Override
    protected boolean onNotificationOpened(@NonNull Context context,
                                           @NonNull NotificationInfo notificationInfo) {
        Bundle push = notificationInfo.getMessage().getPushBundle();
        push.putString("Event", "onNotificationOpened");

        Intent intent = new Intent();
        intent.setAction("com.urbanairship.push.RECEIVED");
        intent.putExtra("com.urbanairship.push.EXTRA_PUSH_MESSAGE_BUNDLE", push);

        boolean shouldSendBroadcast = isApplicationInForeground(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shouldSendBroadcast = isApplicationRunning(context);
        }

        if (!shouldSendBroadcast || ReactNativeUAEventEmitter.getInstance() == null) {
            ReactNativeUAReceiverHelper.getInstance(context).savePushIntent(intent);
        } else {
            context.sendBroadcast(intent);
        }

        return false;
    }

    private boolean isApplicationInForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> task = manager.getRunningAppProcesses();

        String packageName = task.get(0).pkgList[0];

        return packageName.equals(context.getPackageName());
    }

    private boolean isApplicationRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
        
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
            if (processInfo.processName.equals(context.getPackageName())
                    && processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && processInfo.pkgList != null
                    && processInfo.pkgList.length > 0) {
                    return true;
            }
        }

        return false;
    }
}
