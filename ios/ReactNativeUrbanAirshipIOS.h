#import "RCTBridgeModule.h"
#import <AirshipKit/AirshipKit.h>

@interface ReactNativeUrbanAirshipIOS : NSObject <RCTBridgeModule>
+ (void)setupUrbanAirship;
@end

@interface PushHandler : NSObject <UAPushNotificationDelegate>
@end
