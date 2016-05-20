# React Native (0.25): Urban Airship Bridge

This plugin provides client-side integration for the [Urban Airship Engage Platform](https://www.urbanairship.com/products/engage) in _iOS_ and _Android_ app environment.

<!-- TOC depthFrom:2 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Supported React Native platforms](#supported-react-native-platforms)
- [Prerequisites](#prerequisites)
	- [iOS](#ios)
	- [Urban Airship](#urban-airship)
- [Getting Started](#getting-started)
	- [Xcode setup](#xcode-setup)
- [Usage](#usage)

<!-- /TOC -->

## Supported React Native platforms

- React Native (0.25)
- iOS (8+)
- Android (4.1+)

## Prerequisites

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

#### Android setup
- Dentro da sua aplicação modifique o arquivo `android/settings.gradle` incluindo o modulo **react-native-ua**:

```
include ':react-native-ua'
project(':react-native-ua').projectDir = file('../node_modules/react-native-ua/android')
```
- Adicione a url do repositório Urban Airship em seu projeto `android/build.gradle`:

```
// ...

allprojects {
    repositories {
        // ...

        maven {
            // ...

            url "https://urbanairship.bintray.com/android" // add urban repository url
        }
    }
}
```
- Inclua o modulo do react-native-ua nas depenciais de compilação da sua aplicação dentro do arquivo `android/app/build.gradle`:
```
// ...

dependencies {
    // ...

    compile project(':react-native-ua') // add react-native-ua module
}
```
- Insira no manifesto da sua aplicação `android/app/src/main/AndroidManifest.xml` as permissões do módulo:
```
<manifest ...>

    // ...

    <application ...>

      // ...

      <receiver
          android:name="com.globo.reactnativeua.ReactNativeUAReceiver"
          android:exported="false">
          <intent-filter>
              <action android:name="com.urbanairship.push.CHANNEL_UPDATED"/>
              <action android:name="com.urbanairship.push.OPENED"/>
              <action android:name="com.urbanairship.push.DISMISSED"/>
              <action android:name="com.urbanairship.push.RECEIVED"/>
              <category android:name="${applicationId}"/>
          </intent-filter>
      </receiver>

    </application>
</manifest>
```
- Crie o arquivo `android/app/src/main/assets/airshipconfig.propiertes` ***(crie o diretório caso não exista)*** e atualize o com os dados da sua aplicação Urban Airship:
```
gcmSender = Your GCM sender ID (Your Google API project number)

developmentAppKey = Your Development App Key
developmentAppSecret = Your Development App Secret

# If inProduction is true set production key and secret
inProduction = false

productionAppKey = Your Production App Key
productionAppSecret = Your Production Secret
```
- Dentro da `MainActivity.java` da sua aplicação, importe o pacote `ReactNativeUAPackage` e adicione na lista de pacotes da sua aplicação:
```
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

## Usage

```javascript
import ReactNativeUA from 'react-native-ua'; // import module

ReactNativeUA.enable_notification(); // prompt user to enable notification
ReactNativeUA.disable_notification(); // prompt user to disable notification
ReactNativeUA.add_tag("tag"); // add only one tag
ReactNativeUA.remove_tag("tag"); // remove only one tag

// add handler to handle all incoming notifications
ReactNativeUA.on_notitication((notification) => {
  console.log(notification.platform,
              notification.event,
              notification.alert,
              notification.data);
});
```
