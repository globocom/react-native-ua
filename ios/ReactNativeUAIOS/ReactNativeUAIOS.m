#import "UAirship.h"
#import "UAConfig.h"
#import "RCTBridge.h"
#import "RCTEventDispatcher.h"
#import "ReactNativeUAIOS.h"
#import "UALocationService.h"


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

+ (void)setupUrbanAirship:(NSDictionary *) launchOptions {
    UAConfig *config = [UAConfig defaultConfig];

    [UAirship takeOff:config];

    pushHandler = [PushHandler new];
    [UAirship push].pushNotificationDelegate = pushHandler;

    [[ReactNativeUAIOS getInstance] verifyLaunchOptions:launchOptions];
}

- (void)verifyLaunchOptions:(NSDictionary *) launchOptions {
    NSDictionary *notification = [launchOptions objectForKey:UIApplicationLaunchOptionsRemoteNotificationKey];
    
    if (notification == nil) [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"push_notification_opened_from_background"];
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

RCT_EXPORT_METHOD(setNamedUserId:(NSString *)nameUserId) {
    [UAirship push].namedUser.identifier = nameUserId;
}

RCT_EXPORT_METHOD(handleBackgroundNotification) {
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    
    if ([defaults objectForKey:@"push_notification_opened_from_background"]) {
        
        NSDictionary *notification = [defaults objectForKey:@"push_notification_opened_from_background"];
        
        [[ReactNativeUAIOS getInstance] dispatchEvent:@"receivedNotification" body:@{@"event": @"launchedFromNotification",
                                                                                     @"data": notification}];
        
        [defaults removeObjectForKey:@"push_notification_opened_from_background"];
    }
}

RCT_EXPORT_METHOD(enableGeolocation) {
    if([CLLocationManager authorizationStatus] == kCLAuthorizationStatusNotDetermined){
        static CLLocationManager* lm = nil;
        static dispatch_once_t once;
        
        dispatch_once(&once, ^ {
            // Code to run once
            lm = [[CLLocationManager alloc] init];
        });
        
        [lm requestAlwaysAuthorization];
        
        [UAirship shared].locationService.requestAlwaysAuthorization = YES;
        [UALocationService locationServicesEnabled];
        [UALocationService locationServiceAuthorized];
        [UALocationService airshipLocationServiceEnabled];
        [UALocationService setAirshipLocationServiceEnabled:YES];
        [UALocationService coreLocationWillPromptUserForPermissionToRun];
    }
}

@end


@implementation PushHandler

- (void)receivedForegroundNotification:(NSDictionary *)notification fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {
    [[ReactNativeUAIOS getInstance] dispatchEvent:@"receivedNotification" body:@{@"event": @"receivedForegroundNotification",
                                                                                 @"data": notification}];

    completionHandler(UIBackgroundFetchResultNoData);
}

- (void)launchedFromNotification:(NSDictionary *)notification fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {
    [[ReactNativeUAIOS getInstance] dispatchEvent:@"receivedNotification" body:@{@"event": @"launchedFromNotification",
                                                                                 @"data": notification}];

    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    
    [defaults setObject:notification forKey:@"push_notification_opened_from_background"];
    [defaults synchronize];

    completionHandler(UIBackgroundFetchResultNoData);
}

- (void)launchedFromNotification:(NSDictionary *)notification actionIdentifier:(NSString *)identifier completionHandler:(void (^)())completionHandler {
    [[ReactNativeUAIOS getInstance] dispatchEvent:@"receivedNotification" body:@{@"event": @"launchedFromNotificationActionButton",
                                                                                 @"data": notification}];

    completionHandler();
}

- (void)receivedBackgroundNotification:(NSDictionary *)notification actionIdentifier:(NSString *)identifier completionHandler:(void (^)())completionHandler {
    [[ReactNativeUAIOS getInstance] dispatchEvent:@"receivedNotification" body:@{@"event": @"receivedBackgroundNotificationActionButton",
                                                                                 @"data": notification}];

    completionHandler();
}

- (void)receivedBackgroundNotification:(NSDictionary *)notification fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {
    [[ReactNativeUAIOS getInstance] dispatchEvent:@"receivedNotification" body:@{@"event": @"receivedBackgroundNotification",
                                                                                 @"data": notification}];

    completionHandler(UIBackgroundFetchResultNoData);
}

@end
