package com.globo.reactnativeua;

import android.util.Log;

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

	private ReadableMap createReactNativeMessageObject(PushMessage message) {
		ReadableMap messageObject = Arguments.fromBundle(message.getPushBundle());
		return messageObject;
	}

	public void sendEvent(String eventName, ReadableMap message) {
		//Log.d(TAG, "Sending notification: " + message);
		this.context
			.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
			.emit(eventName, message);
	}

	public void sendEvent(String eventName, PushMessage message) {
		this.sendEvent(eventName, this.createReactNativeMessageObject(message));
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
