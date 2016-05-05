#import "ReactNativeUrbanAirshipIOS.h"

@implementation ReactNativeUrbanAirshipIOS

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(enable) {
  [UAirship push].userPushNotificationsEnabled = YES;
}

+ (void)setupUrbanAirship {
  // Log level
  [UAirship setLogLevel:UALogLevelTrace];
  
  // Load config from AirshipConfig.plist
  UAConfig *config = [UAConfig defaultConfig];
  
  // Call takeOff (which creates the UAirship singleton)
  [UAirship takeOff:config];
}

@end