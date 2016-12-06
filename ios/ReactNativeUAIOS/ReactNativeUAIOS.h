#import "RCTBridgeModule.h"
#import "UAPush.h"

@interface ReactNativeUAIOS : NSObject <RCTBridgeModule>
+ (void)setupUrbanAirship:(NSDictionary *) launchOptions;
+ (void)dispatchNotificationEvent:(NSString *)eventType notificationData:(NSDictionary *)notificationData;
@end

@interface PushHandler : NSObject <UAPushNotificationDelegate>
@end
