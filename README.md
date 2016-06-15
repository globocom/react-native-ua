# React Native Module for Urban Airship

This plugin provides client-side integration for the [Urban Airship Engage Platform](https://www.urbanairship.com/products/engage) in _iOS_ and _Android_ app environment.


[Sample app using this module](https://github.com/globocom/react-native-ua-sample)


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

- Xcode 7.0 or higher
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

4. Inside `MainActivity.java`, located at `android/app/src/main/java/your/app/domain`, add the `ReactNativeUAPackage` to your app package list:

  ```java
  // ...

  import com.globo.reactnativeua.ReactNativeUAPackage; // import react-native-ua package

  public class MainActivity extends ReactActivity {
      // ...

      @Override
      protected List<ReactPackage> getPackages() {
          return Arrays.<ReactPackage>asList(
              // ...
              new ReactNativeUAPackage(this.getApplication()) // add react-native-ua package
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

8. Inside `AppDelegate.m`, import `ReactNativeUAIOS.h` and call the module with `[ReactNativeUAIOS setupUrbanAirship]`. Follow the example below:

  ```objective-c
  #import "ReactNativeUAIOS.h"

  // ...

  @implementation AppDelegate

  - (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
  {
    // setup react native urban airship
    [ReactNativeUAIOS setupUrbanAirship];

    // ...
  }

  // ...

  @end
  ```
  
## Methods
- **[ReactNativeUA.enable_notification()](https://github.com/globocom/react-native-ua/blob/master/index.js#L60)**: Prompt user to enable notification receivement;
- **[ReactNativeUA.disable_notification()](https://github.com/globocom/react-native-ua/blob/master/index.js#L64)**: Prompt user to disable notification receivement;
- **[ReactNativeUA.add_tag("tag")](https://github.com/globocom/react-native-ua/blob/master/index.js#L68)**: Set tag to the user;
- **[ReactNativeUA.remove_tag("tag")](https://github.com/globocom/react-native-ua/blob/master/index.js#L72)**: Remove added tag;
- **[ReactNativeUA.on_notification((notification) => {})](https://github.com/globocom/react-native-ua/blob/master/index.js#L76)**: Add handler to handle all incoming notifications. **Attention:** this method need to be called on `componentWillMount()` of the component lifecycle.

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

    constructor (props) {
        super(props);

        ReactNativeUA.enable_notification(); // prompt user to enable notification
    } 

    componentWillMount () {
    
        // add handler to handle all incoming notifications
        ReactNativeUA.on_notification((notification) => {
            console.log('notification:',
                        notification.platform,
                        notification.event,
                        notification.alert,
                        notification.data);

            alert(notification.alert);
        });
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
