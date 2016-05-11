# React Native: Urban Airship Module

React Native module for [Urban Airship](http://docs.urbanairship.com) platforms: *iOS* and *Android*

### Installation (**W.I.P**)
`npm install react-native-ua --save`

#### iOS setup
- Abrir o projeto IOS da sua app React Native no Xcode;
- Habilitar suporte a notificação no projeto:
- - Dentro de capabilities, selecione **Push Notification** e mude para **Enable**;
- - Dentro de capabilities habilitar dentro de **Background Modes**, **Remote notifications**.
- Adicionar através do Xcode todos os arquivos existentes na pasta [node_modules/react-native-ua/ios](https://github.com/globocom/react-native-ua/tree/master/ios) na raiz do projeto.
- Atualizar os dados relativos a sua app do Urbanairship no arquivo de configuração [AirshipConfig.plist](https://github.com/globocom/react-native-ua/blob/master/ios/AirshipConfig.plist) existente na raiz do projeto;
- Mover arquivos [ReactNativeUAIOS.h](https://github.com/globocom/react-native-ua/blob/master/ios/ReactNativeUAIOS.h) e [ReactNativeUAIOS.m](https://github.com/globocom/react-native-ua/blob/master/ios/ReactNativeUAIOS.m) para a raiz da sua aplicação dentro do projeto Xcode.
- Configurar SDK do UrbanAirship:
- - Em *Build Settings* inclua o diretório `$(SRCROOT)/Airship/**` de forma recursiva no item **Header Search Paths**;
- - Em *Build Settings* inclua as flags `-ObjC`, `-lz` e `-lsqlite3` em **Other Linker Flags**;
- - Em *Build Phases* inclua a `libUAirship-7.1.0.a` existente na pasta `Airship` do seu projeto em **Link Binary With Files**;
- - Em *Build Phaces* inclua o arquivo `AirshipResources.bundle` existente na pasta `Airship` em **Copy Bundle Resources**;
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


### Usage (**W.I.P**)

