#import "ReactNativeUrbanAirshipIOS.h"

@implementation ReactNativeUrbanAirshipIOS

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(enableNotification) {
  [UAirship push].userPushNotificationsEnabled = YES;
}

RCT_EXPORT_METHOD(disableNotification) {
  [UAirship push].userPushNotificationsEnabled = NO;
}

RCT_EXPORT_METHOD(addTag:(NSString *)tag) {
  [[UAirship push] addTag:tag];
  [[UAirship push] updateRegistration];
}

RCT_EXPORT_METHOD(removeTag:(NSString *)tag) {
  [[UAirship push] removeTag:tag];
  [[UAirship push] updateRegistration];
}

RCT_EXPORT_METHOD(setTags:(NSArray *)tags) {
  [UAirship push].tags = tags;
  [[UAirship push] updateRegistration];
}

- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

+ (void)setupUrbanAirship {
  [UAirship setLogLevel:UALogLevelTrace];
  
  // Load config from AirshipConfig.plist
  UAConfig *config = [UAConfig defaultConfig];
  
  [UAirship takeOff:config];
}

@end
