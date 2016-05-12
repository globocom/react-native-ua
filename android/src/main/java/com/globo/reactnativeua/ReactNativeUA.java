package com.globo.reactnativeua;

import java.util.Set;
import java.util.List;
import java.util.Collections;
import java.util.HashSet;

import android.app.Activity;
import android.app.Application;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;

import com.urbanairship.UAirship;


public class ReactNativeUA extends ReactContextBaseJavaModule {

    private Activity mainActivity;

    public ReactNativeUA(ReactApplicationContext reactContext, Activity activity) {
        super(reactContext);
        mainActivity = activity;
    }

    @Override
    public String getName() {
        return "ReactNativeUAAndroid";
    }

    public static void takeOff(Application app) {
        UAirship.takeOff(app);
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
        UAirship.shared().getPushManager().editTags()
            .addTag(tag)
            .apply();
    }

    @ReactMethod
    public void removeTag(String tag) {
        UAirship.shared().getPushManager().editTags()
            .removeTag(tag)
            .apply();
    }

    // @ReactMethod
    // public void setTags(ReadableArray tags) {
    //     Set<String> tagsSet = new HashSet<String>((ReadableNativeArray)tags.toArrayList());
    //     UAirship.shared().getPushManager().editTags()
    //         .clear()
    //         .addTags(tagsSet)
    //         .apply();
    // }
}
