import {
  NativeAppEventEmitter,
  NativeModules,
  Platform
} from 'react-native';

const ReactNativeUAIOS = NativeModules.ReactNativeUAIOS;
const ReactNativeUAAndroid = NativeModules.ReactNativeUAAndroid;

let bridge = null;
let subscriptions = {};

switch (Platform.OS) {
    case 'ios':
        bridge = ReactNativeUAIOS;
        break;

    case 'android':
        bridge = ReactNativeUAAndroid;
        break;
}


class ReactNativeUA {

    static enable_notification () {
        bridge.enableNotification();
    }

    static disable_notification () {
        bridge.disableNotification();
    }

    static add_tag (tag) {
        bridge.addTag(tag);
    }

    static remove_tag (tag) {
        bridge.removeTag(tag);
    }

    static set_tags (tags) {
        bridge.setTags(tags);
    }

    static subscribe_to (event, callback) {
        subscriptions[event] = NativeAppEventEmitter.addListener(event, callback);
    }

    static unsubscribe_to (event) {
        delete subscriptions[event].remove();
    }
}

export default ReactNativeUA
