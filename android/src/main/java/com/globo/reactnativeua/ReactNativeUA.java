package com.globo.reactnativeua;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import com.urbanairship.Autopilot;
import com.urbanairship.UAirship;


public class ReactNativeUA extends ReactContextBaseJavaModule {

    private final int LOCATION_PERMISSION = 1;

    public ReactNativeUA(ReactApplicationContext reactContext, Application application) {
        super(reactContext);

        Autopilot.automaticTakeOff(application.getApplicationContext());
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

        if (activity != null) ReactNativeUAReceiverHelper.setup(getCurrentActivity().getApplicationContext()).sendPushIntent();
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
        Activity activity = getCurrentActivity();

        this.enableLocationUpdates();
        this.disableBackgroundLocation();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        if (!(ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    LOCATION_PERMISSION);

            return;
        }
    }
}
