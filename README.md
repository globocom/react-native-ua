# React Native: Urban Airship Module

React Native module for [Urban Airship](http://docs.urbanairship.com) platforms: *iOS* and *Android*

### Installation (**W.I.P**)
`npm install react-native-ua`

#### iOS Installation
- Make sure if you already have your urban app credentials and enable apns; *[Help](http://docs.urbanairship.com/reference/push-providers/apns.html#ios-apns-setup)*
- Open your `react_native_app_folder/ios/YourApp.xcodeproj` using xcode, them copy all content from `node_modules/react-native-ua/ios/` and past into the root level of your app project;
- Using Xcode update `AirshipConfig.plist` with your app credentials from UrbanAirship;
- On Xcode add the AirshipKit.framework file to the Embedded Binaries section in the General tab for your target. *[Help](http://docs.urbanairship.com/platform/ios.html#including-the-urban-airship-sdk)*

### Usage (**W.I.P**)

Into your `AppDelegate.m` call `ReactNativeUrbanAirshipIOS setupUrbanAirship]` in `didFinishLaunchingWithOptions` method:
```object-c
// ...

#import "ReactNativeUrbanAirshipIOS.m"

// ...

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
  // ...
  
  [ReactNativeUrbanAirshipIOS setupUrbanAirship]
  
  // ...
}

@end

```

To enable push notification call the `enable` method from `ReactNativeUrbanAirship` module.

```javascript
import ReactNativeUrbanAirship from 'react-native-ua';

ReactNativeUrbanAirship.enable()
```
