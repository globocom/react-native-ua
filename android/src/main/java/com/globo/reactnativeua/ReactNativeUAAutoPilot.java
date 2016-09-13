package com.globo.reactnativeua;

import android.util.Log;

import com.urbanairship.Autopilot;
import com.urbanairship.UAirship;
import com.urbanairship.actions.OpenExternalUrlAction;
import com.urbanairship.push.notifications.DefaultNotificationFactory;
import com.urbanairship.push.notifications.NotificationFactory;

public class ReactNativeUAAutoPilot extends Autopilot {

    private static final String TAG = "ReactNativeUAAutoPilot";

    @Override
    public void onAirshipReady(UAirship airship) {
		UAirship.shared().getActionRegistry().unregisterAction(OpenExternalUrlAction.DEFAULT_REGISTRY_NAME);

        Log.w(TAG, "Airship ready!");

        // Customize icons
        DefaultNotificationFactory defaultNotifFactory = getDefaultNotificationFactory();
        int smallIconResourceId = Preferences.getInstance().getAndroidSmallIconResourceId();
        if (smallIconResourceId > 0) {
            defaultNotifFactory.setSmallIconId(smallIconResourceId);
        }
        int largeIconResourceId = Preferences.getInstance().getAndroidLargeIconResourceId();
        if (largeIconResourceId > 0) {
            defaultNotifFactory.setLargeIcon(largeIconResourceId);
        }
        UAirship.shared().getPushManager().setNotificationFactory(defaultNotifFactory);
    }

    private DefaultNotificationFactory getDefaultNotificationFactory() {
        final NotificationFactory notifFactory = UAirship.shared().getPushManager().getNotificationFactory();
        if (notifFactory instanceof DefaultNotificationFactory) {
            return (DefaultNotificationFactory) notifFactory;
        }
        return new DefaultNotificationFactory(UAirship.getApplicationContext());
    }
}
