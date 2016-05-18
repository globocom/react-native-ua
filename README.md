# React Native: Urban Airship Module

React Native module for [Urban Airship](http://docs.urbanairship.com) platforms: *iOS* and *Android*

### Pre-requisitos
- Ter o certificado de desenvolvimento IOS configurado no seu ambiente;
- Ter uma app configurada para as plataformas IOS e/ou Android no [UrbanAirship > Add New App](https://go.urbanairship.com/apps/new/).

### Installation
- Acesse seu projeto React Native e instale o módulo: `npm install react-native-ua --save`

#### iOS setup
- Abrir o projeto IOS da sua app React Native no Xcode;
- Habilitar suporte a notificação no projeto:
  - Dentro de *Capabilities*, habilitar **Push Notification**;
  - Dentro de *Capabilities > Background Modes*, habilitar **Remote notifications**.
- Adicionar [ReactNativeUAIOS.xcodeproj](https://github.com/globocom/react-native-ua/tree/master/ios/ReactNativeUAIOS.xcodeproj), existente na pasta `node_modules/react-native-ua/ios` da sua app, dentro das **Libraries** do seu projeto no Xcode.
- Criar o arquivo [AirshipConfig.plist](https://github.com/globocom/react-native-ua/blob/master/ios/AirshipConfig.plist) na raiz do seu projeto com os dados de configuração da sua app do urbanAirship (⚙ > APIs & Integrations > Urban Airship API);
- No Xcode, Configurar Bridge:
  - Em *Build Settings > Linking > Other Linker Flags* inclua as flags `-ObjC`, `-lz` e `-lsqlite3`;
  - Em *Build Settings > Search Paths > Header Search Paths* inclua o path `$(SRCROOT)/../node_modules/react-native-ua/ios` e selecione **recursive**;
  - Em *Build Phases > Link Binary With Files* inclua a `libReactNativeUAIOS.a` do seu projeto;
  - Em *Build Phases > Copy Bundle Resources* inclua o arquivo `node_modules/react-native-ua/ios/Libraries/Airship/AirshipResources.bundle`;
- Dentro do *AppDelegate.m*, importar o módulo [ReactNativeUAIOS.h](https://github.com/globocom/react-native-ua/blob/master/ios/ReactNativeUAIOS.h) e realizar a sua chamada `[ReactNativeUAIOS setupUrbanAirship];`:

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

### Usage

``` javascript
import ReactNativeUA from 'react-native-ua'; // import module

ReactNativeUA.enable_notification(); // prompt user to enable notification
ReactNativeUA.disable_notification(); // prompt user to disable notification
ReactNativeUA.add_tag("tag"); // add only one tag
ReactNativeUA.remove_tag("tag"); // remove only one tag

// add handler to handle all incoming notifications
ReactNativeUA.subscribe_to("receivedNotification", (notification) => {
  console.log(notification.type,
              notification.data.aps.alert,
              notification.data.link);
});
```
