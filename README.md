# React Native: Urban Airship Module

React Native module for [Urban Airship](http://docs.urbanairship.com) platforms: *iOS* and *Android*

### Installation (**W.I.P**)
`npm install react-native-ua`

#### iOS setup
- Habilitar suporte a notificação no projeto IOS do xcode
- - Dentro de capabilities, selecione *Push Notification* e mude para *Enable*;
- - Dentro de capabilities habilitar dentro de *Background Modes*, *Remote notifications*.
- Copiar todos os arquivos existentes na pasta [IOS](https://github.com/globocom/react-native-ua/tree/master/ios) para a raiz do projeto.
- Atualizar os dados relativos ao urbanairship no arquivo de configuração `AirshipConfig.plist` na raiz do projeto baseado neste: [AirshipConfig.plist](https://github.com/globocom/react-native-ua/blob/master/ios/AirshipConfig.plist)
- Mover arquivos [ReactNativeUAIOS.h](https://github.com/globocom/react-native-ua/blob/master/ios/ReactNativeUAIOS.h), [ReactNativeUAIOS.m](https://github.com/globocom/react-native-ua/blob/master/ios/ReactNativeUAIOS.m) para a raiz da aplicação (projeto/app) ou outro local para ser importado.
- Dentro de *General* do projeto, em *Embeded Binaries* adicionar o arquivo *AirshipKit.framework*
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

