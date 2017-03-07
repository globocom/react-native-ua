#import <React/RCTBridgeModule.h>
#import "UAPush.h"

@interface ReactNativeUAIOS : NSObject <RCTBridgeModule>
+ (void)setupUrbanAirship:(NSDictionary *) launchOptions;
+ (void)setupUrbanAirship:(NSDictionary *) launchOptions withConfig:(UAConfig *) config;
@end

@interface PushHandler : NSObject <UAPushNotificationDelegate>
@end
