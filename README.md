# React Native: Urban Airship Module

React Native module for [Urban Airship](http://docs.urbanairship.com) platforms: *iOS* and *Android*

### Pre-requisitos
- Ter o certificado de desenvolvimento IOS configurado no seu ambiente;
- Ter uma app configurada para as plataformas IOS e/ou Android no UrbanAirship.

### Installation
- Acesse seu projeto React Native e instale o módulo: `npm install react-native-ua --save`

#### iOS setup
- Abrir o projeto IOS da sua app React Native no Xcode;
- Habilitar suporte a notificação no projeto:
- - Dentro de capabilities, selecione **Push Notification** e mude para **Enable**;
- - Dentro de capabilities habilitar dentro de **Background Modes**, **Remote notifications**.
- Adicionar [ReactNativeUAIOS.xcodeproj](https://github.com/globocom/react-native-ua/tree/master/ios/ReactNativeUAIOS.xcodeproj), existente na pasta `node_modules/react-native-ua/ios` da sua app, dentro das **Libraries** do seu projeto no Xcode.
- Criar o arquivo [AirshipConfig.plist](https://github.com/globocom/react-native-ua/blob/master/ios/AirshipConfig.plist) na raiz do seu projeto com os dados de configuração da sua app do urbanAirship;
- Configurar Bridge:
- - Em *Build Settings* inclua as flags `-ObjC`, `-lz` e `-lsqlite3` em **Other Linker Flags**;
- - Em *Build Settings* inclua o path `$(SRCROOT)/../node_modules/react-native-ua/ios` em **Header Search Paths**;
- - Em *Build Phases* inclua a `libReactNativeUAIOS.a` do seu projeto em **Link Binary With Files**;
- - Em *Build Phaces* inclua o arquivo `AirshipResources.bundle` existente na pasta `node_modules/react-native-ua/ios/Libraries/Airship` em **Copy Bundle Resources**;
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
