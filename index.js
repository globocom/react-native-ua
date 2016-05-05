import {
  NativeModules,
  Platform
} from 'react-native';

const ReactNativeUrbanAirshipIOS = NativeModules.ReactNativeUrbanAirshipIOS;


export default class ReactNativeUrbanAirship {

    static enable() {
      ReactNativeUrbanAirshipIOS.enable();
    }

}
