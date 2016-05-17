#import "UAirship.h"
#import "UAConfig.h"
#import "RCTBridge.h"
#import "RCTEventDispatcher.h"
#import "ReactNativeUAIOS.h"


@implementation ReactNativeUAIOS

@synthesize bridge = _bridge;

static ReactNativeUAIOS *instance = nil;
static PushHandler *pushHandler = nil;

+ (ReactNativeUAIOS *)getInstance {
    return instance;
}

+ (PushHandler *)getPushHandler {
    return pushHandler;
}

+ (void)setupUrbanAirship {
    UAConfig *config = [UAConfig defaultConfig];

    [UAirship takeOff:config];

    pushHandler = [PushHandler new];
    [UAirship push].pushNotificationDelegate = pushHandler;
}

- (dispatch_queue_t)methodQueue {
    instance = self;

    return dispatch_get_main_queue();
}

- (void)dispatchEvent:(NSString *)event body:(NSDictionary *)notification {
    [self.bridge.eventDispatcher sendAppEventWithName:event body:notification];
}

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(enableNotification) {
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];

    [UAirship push].userPushNotificationsEnabled = YES;

    if ([defaults objectForKey:@"first_time_notification_enable"]) {

        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:UIApplicationOpenSettingsURLString]];

    } else {

        [defaults setBool:YES forKey:@"first_time_notification_enable"];
        [defaults synchronize];

    }
}

RCT_EXPORT_METHOD(disableNotification) {
    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 8.0) {

        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:UIApplicationOpenSettingsURLString]];

    } else {

        [UAirship push].userPushNotificationsEnabled = NO;

    }
}

RCT_EXPORT_METHOD(addTag:(NSString *)tag) {
    [[UAirship push] addTag:tag];
    [[UAirship push] updateRegistration];
}

RCT_EXPORT_METHOD(removeTag:(NSString *)tag) {
    [[UAirship push] removeTag:tag];
    [[UAirship push] updateRegistration];
}

@end


@implementation PushHandler

- (void)receivedForegroundNotification:(NSDictionary *)notification fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {
    [[ReactNativeUAIOS getInstance] dispatchEvent:@"receivedNotification" body:@{@"type": @"receivedForegroundNotification",
                                                                                 @"data": notification}];

    completionHandler(UIBackgroundFetchResultNoData);
}

- (void)launchedFromNotification:(NSDictionary *)notification fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {
    [[ReactNativeUAIOS getInstance] dispatchEvent:@"receivedNotification" body:@{@"type": @"launchedFromNotification",
                                                                                 @"data": notification}];

    completionHandler(UIBackgroundFetchResultNoData);
}

- (void)launchedFromNotification:(NSDictionary *)notification actionIdentifier:(NSString *)identifier completionHandler:(void (^)())completionHandler {
    [[ReactNativeUAIOS getInstance] dispatchEvent:@"receivedNotification" body:@{@"type": @"launchedFromNotificationActionButton",
                                                                                 @"data": notification}];

    completionHandler();
}

- (void)receivedBackgroundNotification:(NSDictionary *)notification actionIdentifier:(NSString *)identifier completionHandler:(void (^)())completionHandler {
    [[ReactNativeUAIOS getInstance] dispatchEvent:@"receivedNotification" body:@{@"type": @"receivedBackgroundNotificationActionButton",
                                                                                 @"data": notification}];

    completionHandler();
}

- (void)receivedBackgroundNotification:(NSDictionary *)notification fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {
    [[ReactNativeUAIOS getInstance] dispatchEvent:@"receivedNotification" body:@{@"type": @"receivedBackgroundNotification",
                                                                                 @"data": notification}];

    completionHandler(UIBackgroundFetchResultNoData);
}

@end
