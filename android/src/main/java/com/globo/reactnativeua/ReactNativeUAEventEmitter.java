package com.globo.reactnativeua;

import android.os.Bundle;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.urbanairship.push.PushMessage;


public class ReactNativeUAEventEmitter {

    private static final String TAG = "RNUAEventEmitter";

    private static ReactNativeUAEventEmitter INSTANCE = null;

    private ReactContext context;

    private ReactNativeUAEventEmitter(ReactContext reactContext) {
        this.context = reactContext;
        this.context.addLifecycleEventListener(ReactNativeUAReceiverHelper.getInstance(context));
    }

    private ReadableMap createReactNativeMessageObject(CharSequence eventName, PushMessage message) {
        Bundle messageBundle = message.getPushBundle();

        messageBundle.putCharSequence("event", eventName);

        return Arguments.fromBundle(messageBundle);
    }

    public void sendEvent(String eventName, PushMessage message) {
        if (context.hasActiveCatalystInstance() && this.context.hasCurrentActivity()) {
                context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
                        "receivedNotification",
                        createReactNativeMessageObject(eventName, message)
                );
        }
    }

    public static void setup(ReactContext reactContext) {
        if (ReactNativeUAEventEmitter.INSTANCE == null) {
            ReactNativeUAEventEmitter.INSTANCE = new ReactNativeUAEventEmitter(reactContext);
        } else {
            Log.w(TAG, "Event Emitter initialized more than once");
            if (ReactNativeUAEventEmitter.INSTANCE.context.getCatalystInstance().isDestroyed()) {
                ReactNativeUAEventEmitter.INSTANCE = new ReactNativeUAEventEmitter(reactContext);
            }
        }
    }

    public static ReactNativeUAEventEmitter getInstance() {
        return ReactNativeUAEventEmitter.INSTANCE;
    }
}
