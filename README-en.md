# React Native Module for Urban Airship

This plugin provides client-side integration for the [Urban Airship Engage Platform](http://docs.urbanairship.com) in _iOS_ and _Android_ app environment.

<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [React Native Module for Urban Airship](#react-native-module-for-urban-airship)
	- [Supported React Native platforms](#supported-react-native-platforms)
	- [Prerequisites](#prerequisites)
		- [iOS](#ios)
		- [Urban Airship](#urban-airship)
	- [Getting Started](#getting-started)
		- [Xcode setup](#xcode-setup)
	- [Usage](#usage)

<!-- /TOC -->

## Supported React Native platforms

- iOS (8+)
- Android (4.1+)

## Prerequisites

### iOS

- Xcode 7.0 or higher
- Homebrew
- Node 4.4
- React Native Command Line Tools
- Watchman
- Flow
- Certificate from a Certificate Authority (CA)
- iOS App Development Certificate

### Urban Airship

- App properly set up. See [Urban Airship > Add New App](https://go.urbanairship.com/apps/new/).

## Getting Started

In your React Native project, install the following module:

```shell
npm install react-native-ua --save
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

## Usage

```javascript
import ReactNativeUA from 'react-native-ua'; // import module

ReactNativeUA.enable_notification(); // prompt user to enable notification
ReactNativeUA.disable_notification(); // prompt user to disable notification
ReactNativeUA.add_tag("tag"); // add only one tag
ReactNativeUA.remove_tag("tag"); // remove only one tag
ReactNativeUA.set_trags(["tag-a", "tag-b"]); // overwrite all tags

// add handler to handle all incoming notifications
ReactNativeUA.subscribe_to("receivedNotification", (notification) => {
  console.log(notification.type,
              notification.data.aps.alert,
              notification.data.link);
});

// remove already added handler
ReactNativeUA.unsubscribe_to("receivedNotification");
```
