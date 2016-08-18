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
            call_notification_listeners({
                'platform': 'ios',
                'event': notification.event,
                'alert': notification.data.aps.alert,
                'data': notification.data,
                'url': notification.data['^u'] || false
            });
        });

        break;

    case 'android':
        bridge = ReactNativeUAAndroid;

        DeviceEventEmitter.addListener('receivedNotification', (notification) => {
            call_notification_listeners({
                'platform': 'android',
                'event': notification.event,
                'alert': notification['com.urbanairship.push.ALERT'],
                'data': notification.data,
                'url': false
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

    static set_named_user_id (nameUserId) {
        bridge.setNamedUserId(nameUserId);
    }

    static on_notification (callback) {
        notification_listeners.push(callback);
    }

}

export default ReactNativeUA
