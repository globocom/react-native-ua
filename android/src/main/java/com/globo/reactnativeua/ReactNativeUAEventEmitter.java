package com.globo.reactnativeua;

import android.util.Log;

import android.os.Bundle;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import com.urbanairship.push.PushMessage;

class ReactNativeUAEventEmitter {

    private static final String TAG = "ReactNativeUAEventEmitter";

	private static ReactNativeUAEventEmitter INSTANCE = null;

	private ReactContext context;

	private ReactNativeUAEventEmitter(ReactContext reactContext) {
    	this.context = reactContext;
	}

	private ReadableMap createReactNativeMessageObject(CharSequence eventName, PushMessage message) {
		Bundle messageBundle = message.getPushBundle();

		messageBundle.putCharSequence("event", eventName);

		ReadableMap messageObject = Arguments.fromBundle(messageBundle);

		return messageObject;
	}

	public void sendEvent(String eventName, PushMessage message) {
		this.context
			.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
			.emit("receivedNotification", this.createReactNativeMessageObject(eventName, message));
	}

	public static void setup(ReactContext reactContext) {
		if (ReactNativeUAEventEmitter.INSTANCE == null) {
			ReactNativeUAEventEmitter.INSTANCE = new ReactNativeUAEventEmitter(reactContext);
			return;
		} else {
			Log.w(TAG, "Event Emmitter initialized more than once");
			return;
		}
	}

	public static ReactNativeUAEventEmitter getInstance() {
		return ReactNativeUAEventEmitter.INSTANCE;
	}
}
