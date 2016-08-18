package com.globo.reactnativeua;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import com.urbanairship.Autopilot;
import com.urbanairship.UAirship;


public class ReactNativeUA extends ReactContextBaseJavaModule {

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
    }
}
