package com.globo.reactnativeua;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
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
    protected void onPushReceived(@NonNull Context context, @NonNull PushMessage message, boolean notificationPosted) {
        boolean isRunning = isApplicationRunning(context);

        if (isRunning && ReactNativeUAEventEmitter.getInstance() != null) {

            if (message.getActions().get("^u") != null && ReactNativeUAReceiverHelper.setup(context).isActionUrl()) {
                ActionRunRequest.createRequest(new OpenExternalUrlAction()).setSituation(Action.SITUATION_MANUAL_INVOCATION).setValue(message.getActions().get("^u")).run();
                return;
            }

            String event = message.getPushBundle().getString("Event", "onNotificationReceived");

            ReactNativeUAEventEmitter.getInstance().sendEvent(event, message);
        }
    }

    @Override
    protected boolean onNotificationOpened(@NonNull Context context, @NonNull NotificationInfo notificationInfo) {
        Intent intent = new Intent();

        Bundle push = notificationInfo.getMessage().getPushBundle();

        push.putString("Event", "onNotificationOpened");

        intent.setAction("com.urbanairship.push.RECEIVED");
        intent.putExtra("com.urbanairship.push.EXTRA_PUSH_MESSAGE_BUNDLE", push);

        if (ReactNativeUAEventEmitter.getInstance() == null) ReactNativeUAReceiverHelper.setup(context).savePushIntent(intent);
        else context.sendBroadcast(intent);

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