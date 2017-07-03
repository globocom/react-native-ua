#import <React/RCTEventEmitter.h>
#import <React/RCTBridgeModule.h>
#import "UAPush.h"

@interface ReactNativeUAIOS : RCTEventEmitter <RCTBridgeModule>
+ (void)setupUrbanAirship:(NSDictionary *) launchOptions;
+ (void)setupUrbanAirship:(NSDictionary *) launchOptions withConfig:(UAConfig *) config;
+ (void)dispatchNotificationEvent:(NSString *)eventType notificationData:(NSDictionary *)notificationData;
@end

@interface PushHandler : NSObject <UAPushNotificationDelegate>
@end
