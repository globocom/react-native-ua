#import "RCTBridgeModule.h"
#import "UAPush.h"

@interface ReactNativeUAIOS : NSObject <RCTBridgeModule>
+ (void)setupUrbanAirship;
@end

@interface PushHandler : NSObject <UAPushNotificationDelegate>
@end
