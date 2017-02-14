# React Native Module for Urban Airship

This plugin provides client-side integration for the [Urban Airship Engage Platform](https://www.urbanairship.com/products/engage) in _iOS_ and _Android_ app environment.

<!-- TOC depthFrom:2 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Supported React Native platforms](#supported-react-native-platforms)
- [Prerequisites](#prerequisites)
  - [Android](#android)
  - [iOS](#ios)
  - [Urban Airship](#urban-airship)
- [Getting Started](#getting-started)
  - [Android setup](#android-setup)
  - [Xcode setup](#xcode-setup)
- [Methods](#methods)
- [Usage](#usage)

<!-- /TOC -->

## Supported React Native platforms

- Android (4.1+)
- iOS (8+)
- React Native (0.27)

## Prerequisites

### Android

- Android Studio 2.0 or higher
- Node 4.4
- React Native Commnad Line Tools
- [Recommended] Watchman
- [Recommended] Flow

### iOS

- Xcode 8.0 or higher
- Node 4.4
- React Native Command Line Tools
- Certificate from a Certificate Authority (CA)
- iOS App Development Certificate
- [Recommended] Watchman
- [Recommended] Flow

### Urban Airship

- App properly set up. See [Urban Airship > Add New App](https://go.urbanairship.com/apps/new/).

## Getting Started

In your React Native project, install the following module:

```shell
npm install react-native-ua --save
```

### Android setup

1. Include the following module to your `android/settings.gradle` in your React Native project:

  ```java
  include ':react-native-ua'
  project(':react-native-ua').projectDir = file('../node_modules/react-native-ua/android')
  ```

2. Include the `react-native-ua` module in your app compile dependencies, inside the `android/app/build.gradle` file:

  ```java
  // ...

  dependencies {
      // ...

      compile project(':react-native-ua') // add react-native-ua module
  }
  ```

3. Create the `android/app/src/main/assets/airshipconfig.properties` file and update it with your Urban Airship App's data:

  ```java
  gcmSender = Your GCM sender ID (Your Google API project number)

  developmentAppKey = Your Development App Key
  developmentAppSecret = Your Development App Secret

  # If inProduction is true set production key and secret
  inProduction = false

  productionAppKey = Your Production App Key
  productionAppSecret = Your Production Secret
  ```

4. Inside `MainApplication.java`, located at `android/app/src/main/java/your/app/domain`, add the `ReactNativeUAPackage` to your app package list:

  ```java
  // ...

  import com.globo.reactnativeua.ReactNativeUAPackage; // import react-native-ua package

  public class MainApplication extends Application implements ReactApplication {
      // ...

      @Override
      protected List<ReactPackage> getPackages() {
          return Arrays.<ReactPackage>asList(
              // ...
              new ReactNativeUAPackage() // set react-native-ua package using application
          );
      }
  }
  ```

### Xcode setup

1. Open your iOS React Native project.
2. Select the project node, in Targets section:

  - Inside _Capabilities_ tab, turn **Push Notification** on
  - Then turn **Background Modes** on.
  - Inside _Background Modes_, enable **Remote Notifications**.

3. Find the `ReactNativeUAIOS.xcodeproj` file within `node_modules/react-native-ua/ios` and drag it into the `Libraries` node in Xcode.

4. Add `AirshipConfig.plist` from folder `ios/` in the project node

5. Edit the file and add your _App Key_, _App Secret_ and _App Master Secret_, the same used within Urban Airship setup (`⚙ > APIs & Integrations > Urban Airship API`).

6. Back to the project node, select the Targets section:

  - In the _Build Settings_ tab, go to _Linking > Other Linker Flags_ and include the following tags:

    ```
    -ObjC
    -lz
    -lsqlite3
    ```

  - Then go to _Search Paths > Header Search Paths_, add the following path and select the **recursive** option:

    ```
    $(SRCROOT)/../node_modules/react-native-ua/ios
    ```

7. In the _Build Phases_ tab:

  - Open the _Link Binary With Libraries_ section, click on the plus sign (➕) and select `libReactNativeUAIOS.a` from _Workspace_.
  - Now expand the _Copy Bundle Resources_, click on the plus sign (➕) and add the following file:

    ```
    node_modules/react-native-ua/ios/Libraries/Airship/AirshipResources.bundle
    ```

8. Inside `AppDelegate.m`, import `ReactNativeUAIOS.h` and setup the module. Follow the example below:

  ```objective-c
  #import "ReactNativeUAIOS.h"

  // ...

  @implementation AppDelegate

  - (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
  {
    // setup react native urban airship using AirshipConfig.plist (the default way)
    [ReactNativeUAIOS setupUrbanAirship:launchOptions];

    // OR setup react native urban airship programmatically. The following example use the content of AirshipConfig-dev.plist instead of the default AirshipConfig.plist
    NSString *configPath = [[NSBundle mainBundle] pathForResource:@"AirshipConfig-dev" ofType:@"plist"];
    UAConfig *config = [UAConfig configWithContentsOfFile:configPath];
    [ReactNativeUAIOS setupUrbanAirship:launchOptions withConfig:config];
    // ...
  }

  // ...

  @end
  ```

9. To enable location add this two string keys with their values inside Info.plist:

  - NSLocationAlwaysUsageDescription: Urban Airship location service
  - NSLocationWhenInUseUsageDescription: Urban Airship location service when app is in use

## Methods
- **[ReactNativeUA.enable_notification()](https://github.com/globocom/react-native-ua/blob/master/index.js#L71)**: Prompt user to enable notification receivement;
- **[ReactNativeUA.disable_notification()](https://github.com/globocom/react-native-ua/blob/master/index.js#L75)**: Prompt user to disable notification receivement;
- **[ReactNativeUA.enable_geolocation()](https://github.com/globocom/react-native-ua/blob/master/index.js#L79)**: Prompt user to enable geolocation;
- **[ReactNativeUA.enable_action_url()](https://github.com/globocom/react-native-ua/blob/master/index.js#L83)**: Enable url action. The app will open the default browser with passed url;
- **[ReactNativeUA.disable_action_url()](https://github.com/globocom/react-native-ua/blob/master/index.js#L87)**: Disable url action (Default). The notification handler will receive payload with a `url` property;
- **[ReactNativeUA.handle_background_notification()](https://github.com/globocom/react-native-ua/blob/master/index.js#L91)**: Handle notifications when app is in background;
- **[ReactNativeUA.add_tag("tag")](https://github.com/globocom/react-native-ua/blob/master/index.js#L95)**: Set tag to the user;
- **[ReactNativeUA.remove_tag("tag")](https://github.com/globocom/react-native-ua/blob/master/index.js#L99)**: Remove added tag;
- **[ReactNativeUA.set_quiet_time_enabled(true)](https://github.com/globocom/react-native-ua/blob/master/index.js#L114)**: Enable/disable a quiet notification period.
- **[ReactNativeUA.set_quiet_time({
  startHour: 22,
  startMinute: 0,
  endHour: 7,
  endMinute: 0
})](https://github.com/globocom/react-native-ua/blob/master/index.js#L110)**: Set the quiet period.
- **[ReactNativeUA.are_notifications_enabled((error, enabled) => {})](https://github.com/globocom/react-native-ua/blob/master/index.js#L118)**: Check if notifications are enabled by user. The callback is optional, this method also returns a promise.
- **[ReactNativeUA.set_named_user_id("nameUserId")](https://github.com/globocom/react-native-ua/blob/master/index.js#L127)**: Set named user id;
- **[ReactNativeUA.on_notification((notification) => {})](https://github.com/globocom/react-native-ua/blob/master/index.js#L131)**: Add handler to handle all incoming notifications. **Attention:** this method need to be called on `componentWillMount()` of the component lifecycle.
- **[ReactNativeUA.get_channel_id((error, channelId) => {})](https://github.com/globocom/react-native-ua/blob/master/index.js#L135)**: Get channel id for device. The callback is optional, this method also returns a promise.

## Usage

```javascript
import React, { Component } from 'react';

import {
    AppRegistry,
    Text,
    View
} from 'react-native';

import ReactNativeUA from 'react-native-ua'; // import module


class SampleApp extends Component {

    constructor(props) {
      super(props);

      ReactNativeUA.enable_notification(); // prompt user to enable notification

      ReactNativeUA.enable_geolocation(); // prompt user to enable geolocation

      ReactNativeUA.enable_action_url(); // enable url action

      ReactNativeUA.handle_background_notification(); // handle notifications when app is in background

      ReactNativeUA.add_tag('tag'); // set tag to the user

      ReactNativeUA.set_named_user_id('user_id'); // set named user id

      ReactNativeUA.set_quiet_time_enabled(true); // activate a quiet period

      ReactNativeUA.set_quiet_time({
        startHour: 22,
        startMinute: 0,
        endHour: 7,
        endMinute: 0
      }); // set the period to 22:00-07:00
    }

    componentWillMount() {
        // add handler to handle all incoming notifications
        ReactNativeUA.on_notification((notification) => {
            console.log('notification:',
                        notification.url, // if action url is disabled
                        notification.platform,
                        notification.event,
                        notification.alert,
                        notification.data);

            alert(notification.alert);
        });

        // Check if user enabled notifications
        ReactNativeUA.are_notifications_enabled().then(enabled => {
          console.log('notifications enabled:', enabled);
        })

        // Get channel id for device
        ReactNativeUA.get_channel_id().then(channelId => {
          console.log('channel id:', channelId);
        })
    }

    render () {
        return (
            <View>
                <Text>ReactNativeUA</Text>
            </View>
        );
    }
}

AppRegistry.registerComponent('SampleApp', () => SampleApp);
```
