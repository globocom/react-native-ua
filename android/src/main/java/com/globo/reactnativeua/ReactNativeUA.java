package com.globo.reactnativeua;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.urbanairship.Autopilot;
import com.urbanairship.UAirship;
import com.urbanairship.actions.Action;
import com.urbanairship.actions.ActionArguments;
import com.urbanairship.actions.ActionCompletionCallback;
import com.urbanairship.actions.ActionResult;
import com.urbanairship.actions.ActionRunRequest;
import com.urbanairship.actions.ActionValue;
import com.urbanairship.push.notifications.DefaultNotificationFactory;
import com.urbanairship.push.notifications.NotificationFactory;


public class ReactNativeUA extends ReactContextBaseJavaModule {

    public ReactNativeUA(final ReactApplicationContext reactContext) {
        super(reactContext);
        Autopilot.automaticTakeOff(getReactApplicationContext());
    }

    @Override
    public String getName() {
        return "ReactNativeUAAndroid";
    }

    @ReactMethod
    public void enableNotification() {
        UAirship.shared().getPushManager().setUserNotificationsEnabled(true);
    }

    @ReactMethod
    public void disableNotification() {
        UAirship.shared().getPushManager().setUserNotificationsEnabled(false);
    }

    @ReactMethod
    public void addTag(String tag) {
        UAirship.shared().getPushManager().editTags().addTag(tag).apply();
    }

    @ReactMethod
    public void removeTag(String tag) {
        UAirship.shared().getPushManager().editTags().removeTag(tag).apply();
    }

    @ReactMethod
    public void setNamedUserId(String namedUserID) {
        UAirship.shared().getPushManager().getNamedUser().setId(namedUserID);
    }

    @ReactMethod
    public void enableLocationUpdates() {
        UAirship.shared().getLocationManager().setLocationUpdatesEnabled(true);
    }

    @ReactMethod
    public void disableLocationUpdates() {
        UAirship.shared().getLocationManager().setLocationUpdatesEnabled(false);
    }

    @ReactMethod
    public void enableBackgroundLocation() {
        UAirship.shared().getLocationManager().setBackgroundLocationAllowed(true);
    }

    @ReactMethod
    public void disableBackgroundLocation() {
        UAirship.shared().getLocationManager().setBackgroundLocationAllowed(false);
    }

    @ReactMethod
    public void handleBackgroundNotification() {
        Activity activity = getCurrentActivity();
        if (activity != null) {
            ReactNativeUAReceiverHelper.setup(getCurrentActivity().getApplicationContext()).sendPushIntent();
        }
    }

    @ReactMethod
    public void enableActionUrl() {
        Activity activity = getCurrentActivity();
        if (activity != null) {
            ReactNativeUAReceiverHelper.setup(getCurrentActivity().getApplicationContext()).setActionUrl(true);
            Log.d("ActionUrl", "Enable default action url behaviour -> True");
        }
    }

    @ReactMethod
    public void disableActionUrl() {
        Activity activity = getCurrentActivity();
        if (activity != null) {
            ReactNativeUAReceiverHelper.setup(getCurrentActivity().getApplicationContext()).setActionUrl(false);
            Log.d("ActionUrl", "Disable default action url behaviour -> False");
        }
    }

    @ReactMethod
    public void enableGeolocation() {
        final Activity activity = getCurrentActivity();

        if (activity != null) {
            if (shouldRequestPermissions(activity)) {
                ActionRunRequest.createRequest(new Action() {
                    @NonNull
                    @Override
                    public ActionResult perform(ActionArguments arguments) {
                        int[] result = requestPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION);

                        for (int i = 0; i < result.length; i++) {
                            if (result[i] == PackageManager.PERMISSION_GRANTED) {
                                return ActionResult.newResult(ActionValue.wrap(true));
                            }
                        }
                        return ActionResult.newResult(ActionValue.wrap(false));
                    }
                }).run(new ActionCompletionCallback() {
                    @Override
                    public void onFinish(ActionArguments arguments, ActionResult result) {
                        if (result.getValue().getBoolean(false)) {
                            UAirship.shared().getLocationManager().setLocationUpdatesEnabled(true);
                        }
                    }
                });

            } else {
                UAirship.shared().getLocationManager().setLocationUpdatesEnabled(true);
            }
        }
    }

    private boolean shouldRequestPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;

        int corseLocation = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        int fineLocation = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);

        return corseLocation == PackageManager.PERMISSION_DENIED && fineLocation == PackageManager.PERMISSION_DENIED;
    }

    @ReactMethod
    public void setIcon(String iconName) {
        int iconId = getImageResourceId(iconName);
        if (iconId > 0) {
            DefaultNotificationFactory defaultNotifFactory = getDefaultNotificationFactory();
            defaultNotifFactory.setSmallIconId(iconId);
            UAirship.shared().getPushManager().setNotificationFactory(defaultNotifFactory);
        }
    }

    @ReactMethod
    public void setLargeIcon(String iconName) {
        int iconId = getImageResourceId(iconName);
        if (iconId > 0) {
            DefaultNotificationFactory defaultNotifFactory = getDefaultNotificationFactory();
            defaultNotifFactory.setLargeIcon(iconId);
            UAirship.shared().getPushManager().setNotificationFactory(defaultNotifFactory);
        }
    }

    private DefaultNotificationFactory getDefaultNotificationFactory() {
        final NotificationFactory notifFactory = UAirship.shared().getPushManager().getNotificationFactory();
        if (notifFactory instanceof DefaultNotificationFactory) {
            return (DefaultNotificationFactory) notifFactory;
        }
        return new DefaultNotificationFactory(getReactApplicationContext());
    }

    private int getImageResourceId(String imageName) {
        if (imageName == null || imageName.length() <= 0) {
            return -1;
        }
        int imageId = getImageResourceId(imageName, "drawable");
        if (imageId <= 0) {
            imageId = getImageResourceId(imageName, "mipmap");
        }
        return imageId;
    }

    private int getImageResourceId(String imageName, String imageResourceType) {
        return getReactApplicationContext().getResources().getIdentifier(
                imageName,
                imageResourceType,
                getReactApplicationContext().getPackageName());
    }

}
