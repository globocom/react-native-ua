package com.reactnativeua;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;


public class ReactNativeUAModule extends ReactContextBaseJavaModule {

    private Activity mainActivity;

    public ReactNativeUAModule(ReactApplicationContext reactContext, Activity activity) {
        super(reactContext);

        mainActivity = activity;
    }

    @Override
    public String getName() {
        return "ReactNativeUAAndroid";
    }

    @ReactMethod
    public void takeOff(ReadableMap config) {
        UAirShip.takeOff(this);
    }

    @ReactMethod
    public void enableNotification() {}

    @ReactMethod
    public void disableNotification() {}

    @ReactMethod
    public void addTag() {}

    @ReactMethod
    public void removeTag() {}

    @ReactMethod
    public void setTags() {}
}
