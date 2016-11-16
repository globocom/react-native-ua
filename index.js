import {
  DeviceEventEmitter,
  NativeAppEventEmitter,
  NativeModules,
  Platform
} from 'react-native';

const ReactNativeUAIOS = NativeModules.ReactNativeUAIOS;
const ReactNativeUAAndroid = NativeModules.ReactNativeUAAndroid;

let bridge = null;
let notification_listeners = [];

let call_notification_listeners = function (notification) {
    var i = notification_listeners.length - 1;

    for (i; i >= 0; i--) {
        notification_listeners[i]({
            'platform': notification.platform,
            'event': notification.event,
            'alert': notification.alert,
            'data': notification.data,
            'url': notification.url
        });
    }
}

switch (Platform.OS) {
    case 'ios':
        bridge = ReactNativeUAIOS;

        NativeAppEventEmitter.addListener('receivedNotification', (notification) => {
            var action_url = notification.data['^u'] || false;

            if (notification && notification.data && notification.data.aps && notification.data.aps.alert) {
              call_notification_listeners({
                  'platform': 'ios',
                  'event': notification.event,
                  'alert': notification.data.aps.alert,
                  'data': notification.data,
                  'url': action_url
              });
            }
        });

        break;

    case 'android':
        bridge = ReactNativeUAAndroid;

        DeviceEventEmitter.addListener('receivedNotification', (notification) => {
            var actions_json = notification['com.urbanairship.actions'] || false;
            var actions = actions_json ? JSON.parse(actions_json) : false;
            var action_url = actions ? actions['^u'] || false : false;

            call_notification_listeners({
                'platform': 'android',
                'event': notification.event,
                'alert': notification['com.urbanairship.push.ALERT'],
                'data': notification,
                'url': action_url
            });
        });

        break;
}


class ReactNativeUA {

    static enable_notification () {
        bridge.enableNotification();
    }

    static disable_notification () {
        bridge.disableNotification();
    }

    static enable_geolocation () {
        bridge.enableGeolocation();
    }

    static enable_action_url () {
        bridge.enableActionUrl();
    }

    static disable_action_url () {
        bridge.disableActionUrl();
    }

    static handle_background_notification () {
        bridge.handleBackgroundNotification();
    }

    static add_tag (tag) {
        bridge.addTag(tag);
    }

    static remove_tag (tag) {
        bridge.removeTag(tag);
    }

    /*
    * @param {Object} time
    * @param {number} time.startHour
    * @param {number} time.startMinute
    * @param {number} time.endHour
    * @param {number} time.endMinute
    */
    static set_quiet_time (time) {
      bridge.setQuietTime(time);
    }

    static set_quiet_time_enabled (enabled) {
      bridge.setQuietTimeEnabled(enabled);
    }

    static are_notifications_enabled (callback) {
        return new Promise((resolve, reject) => {
            bridge.areNotificationsEnabled(enabled => {
                callback && callback(null, enabled);
                resolve(enabled);
            })
        })
    }

    static set_named_user_id (nameUserId) {
        bridge.setNamedUserId(nameUserId);
    }

    static on_notification (callback) {
        notification_listeners.push(callback);
    }

    static set_android_small_icon(iconName) {
        if (Platform.OS === 'android') {
            bridge.setAndroidSmallIcon(iconName);
        }
    }

    static set_android_large_icon (iconName) {
        if (Platform.OS === 'android') {
            bridge.setAndroidLargeIcon(iconName);
        }
    }

}

export default ReactNativeUA
