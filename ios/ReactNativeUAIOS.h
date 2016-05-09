#import "RCTBridgeModule.h"
#import <AirshipKit/AirshipKit.h>

@interface ReactNativeUAIOS : NSObject <RCTBridgeModule>
+ (void)setupUrbanAirship;
@end

@interface PushHandler : NSObject <UAPushNotificationDelegate>
@end
