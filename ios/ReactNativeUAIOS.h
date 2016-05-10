#import "RCTBridgeModule.h"
#import "UAirship.h"
#import "UAConfig.h"
#import "UAPush.h"

@interface ReactNativeUAIOS : NSObject <RCTBridgeModule>
+ (void)setupUrbanAirship;
@end

@interface PushHandler : NSObject <UAPushNotificationDelegate>
@end
