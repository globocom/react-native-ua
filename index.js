import {
  NativeAppEventEmitter,
  NativeModules,
  Platform
} from 'react-native';


const ReactNativeUrbanAirshipIOS = NativeModules.ReactNativeUrbanAirshipIOS;

let subscriptions = {};

export default class ReactNativeUrbanAirship {

    static enable_notification () {
        ReactNativeUrbanAirshipIOS.enableNotification();
    }

    static disable_notification () {
        ReactNativeUrbanAirshipIOS.disableNotification();
    }

    static add_tag (tag) {
        ReactNativeUrbanAirshipIOS.addTag(tag);
    }

    static remove_tag (tag) {
        ReactNativeUrbanAirshipIOS.removeTag(tag);
    }

    static set_tags (tags) {
        ReactNativeUrbanAirshipIOS.setTags(tags);
    }

    static subscribe_to (event, callback) {
        subscriptions[event] = NativeAppEventEmitter.addListener(event, callback);
    }

    static unsubscribe_to (event) {
        delete subscriptions[event].remove();
    }
}
