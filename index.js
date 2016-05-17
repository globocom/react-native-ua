import {
  DeviceEventEmitter,
  NativeAppEventEmitter,
  NativeModules,
  Platform
} from 'react-native';

const ReactNativeUAIOS = NativeModules.ReactNativeUAIOS;
const ReactNativeUAAndroid = NativeModules.ReactNativeUAAndroid;

let bridge = null;
let event_emitter = null;
let subscriptions = {};

switch (Platform.OS) {
    case 'ios':
        bridge = ReactNativeUAIOS;
        event_emitter = NativeAppEventEmitter;
        break;

    case 'android':
        bridge = ReactNativeUAAndroid;
        event_emitter = DeviceEventEmitter;
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

    static subscribe_to (event, callback) {
        return event_emitter.addListener(event, callback);
    }
}

export default ReactNativeUA
