package com.globo.reactnativeua;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Set;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableArray;
import com.urbanairship.Autopilot;
import com.urbanairship.UAirship;
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
    public void getTags(Callback callback) {
        Set<String> tags = UAirship.shared().getPushManager().getTags();
        WritableArray array = new WritableNativeArray();
        for (String tag: tags)
        {
          array.pushString(tag);
        }
        callback.invoke(array);
    }

    @ReactMethod
    public void getChannelId(Callback callback) {
        String channelId = UAirship.shared().getPushManager().getChannelId();
        callback.invoke(channelId);
    }

    @ReactMethod
    public void setQuietTime(ReadableMap time) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm", Locale.getDefault());
            Date startDate = formatter.parse(time.getInt("startHour") + ":" + time.getInt("startMinute"));
            Date endDate = formatter.parse(time.getInt("endHour") + ":" + time.getInt("endMinute"));

            UAirship.shared().getPushManager().setQuietTimeInterval(startDate, endDate);

        } catch (ParseException ex) {
            Log.e("ReactNativeUA.setQuietTime", "Date format exception", ex);
        }
    }

    @ReactMethod
    public void setQuietTimeEnabled(Boolean enabled) {
        UAirship.shared().getPushManager().setQuietTimeEnabled(enabled);
    }

    @ReactMethod
    public void areNotificationsEnabled(Callback callback) {
        Boolean enabled = UAirship.shared().getPushManager().isOptIn();
        callback.invoke(enabled);
    }

    @ReactMethod
    public void setNamedUserId(String namedUserID) {
        UAirship.shared().getNamedUser().setId(namedUserID);
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
        ReactNativeUAReceiverHelper.getInstance(getReactApplicationContext()).sendPushIntent();
    }

    @ReactMethod
    public void enableActionUrl() {
        PreferencesManager.getInstance().setEnabledActionUrl(true);
        Log.d("ActionUrl", "Enable default action url behaviour -> True");
    }

    @ReactMethod
    public void disableActionUrl() {
        PreferencesManager.getInstance().setEnabledActionUrl(false);
        Log.d("ActionUrl", "Disable default action url behaviour -> False");
    }

    @ReactMethod
    public void enableGeolocation() {
        if (shouldRequestPermissions()) {
            RequestPermissionsTask task = new RequestPermissionsTask(getReactApplicationContext(), new RequestPermissionsTask.Callback() {
                @Override
                public void onResult(boolean enabled) {
                    UAirship.shared().getLocationManager().setLocationUpdatesEnabled(enabled);
                }
            });

            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            UAirship.shared().getLocationManager().setLocationUpdatesEnabled(true);
        }
    }

    private boolean shouldRequestPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }

        int coarseLocation = ActivityCompat.checkSelfPermission(getReactApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int fineLocation = ActivityCompat.checkSelfPermission(getReactApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);

        return coarseLocation == PackageManager.PERMISSION_DENIED
                && fineLocation == PackageManager.PERMISSION_DENIED;
    }

    @ReactMethod
    public void setAndroidSmallIcon(String iconName) {
        int iconId = getImageResourceId(iconName);
        if (iconId != 0) {
            PreferencesManager.getInstance().setAndroidSmallIconResourceId(iconId);
            DefaultNotificationFactory defaultNotifFactory = getDefaultNotificationFactory();
            defaultNotifFactory.setSmallIconId(iconId);
            UAirship.shared().getPushManager().setNotificationFactory(defaultNotifFactory);
        }
    }

    @ReactMethod
    public void setAndroidLargeIcon(String iconName) {
        int iconId = getImageResourceId(iconName);
        if (iconId != 0) {
            PreferencesManager.getInstance().setAndroidLargeIconResourceId(iconId);
            DefaultNotificationFactory defaultNotifFactory = getDefaultNotificationFactory();
            defaultNotifFactory.setLargeIcon(iconId);
            UAirship.shared().getPushManager().setNotificationFactory(defaultNotifFactory);
        }
    }

    private DefaultNotificationFactory getDefaultNotificationFactory() {
        final NotificationFactory notifFactory = UAirship.shared().getPushManager()
                .getNotificationFactory();
        if (notifFactory instanceof DefaultNotificationFactory) {
            return (DefaultNotificationFactory) notifFactory;
        }
        return new DefaultNotificationFactory(UAirship.getApplicationContext());
    }

    private int getImageResourceId(String imageName) {
        if (imageName == null || imageName.length() <= 0) {
            return -1;
        }
        int imageId = getImageResourceId(imageName, "drawable");
        if (imageId == 0) {
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
