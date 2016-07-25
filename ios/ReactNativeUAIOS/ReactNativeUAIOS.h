#import "RCTBridgeModule.h"
#import "UAPush.h"

@interface ReactNativeUAIOS : NSObject <RCTBridgeModule>
+ (void)setupUrbanAirship:(NSDictionary *) launchOptions;
@end

@interface PushHandler : NSObject <UAPushNotificationDelegate>
@end
