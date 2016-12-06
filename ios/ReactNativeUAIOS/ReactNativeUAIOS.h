#import <React/RCTBridgeModule.h>
#import "UAPush.h"

@interface ReactNativeUAIOS : NSObject <RCTBridgeModule>
+ (void)setupUrbanAirship:(NSDictionary *) launchOptions;
+ (void)setupUrbanAirship:(NSDictionary *) launchOptions withConfig:(UAConfig *) config;
+ (void)dispatchNotificationEvent:(NSString *)eventType notificationData:(NSDictionary *)notificationData;
@end

@interface PushHandler : NSObject <UAPushNotificationDelegate>
@end
