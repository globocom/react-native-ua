#import "RCTBridge.h"
#import "RCTEventDispatcher.h"
#import "ReactNativeUAIOS.h"
#import "AirshipLib.h"

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

    pushHandler = [[PushHandler alloc] init];
    [UAirship push].pushNotificationDelegate = pushHandler;

    // Update open URL action's predicate to only run if enabled
    [[UAirship shared].actionRegistry updatePredicate:^BOOL(UAActionArguments *args) {
        return [[NSUserDefaults standardUserDefaults] boolForKey:@"enable_action_url"];
    } forEntryWithName:kUAOpenExternalURLActionDefaultRegistryName];

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
    [UAirship namedUser].identifier = nameUserId;
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
        
        [UAirship location].locationUpdatesEnabled = YES;
        [UAirship location].autoRequestAuthorizationEnabled = YES;
        [UAirship location].backgroundLocationUpdatesAllowed = YES;
        
    }
}

RCT_EXPORT_METHOD(enableActionUrl) {
    [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"enable_action_url"];
    [[NSUserDefaults standardUserDefaults] synchronize];
    
    BOOL isActionUrl = [[NSUserDefaults standardUserDefaults] boolForKey:@"enable_action_url"] ? YES : NO;
    
    NSLog(@"Habilitou o comportamento DEFAULT da action URL -> %@", isActionUrl ? @"YES": @"NO");
}

RCT_EXPORT_METHOD(disableActionUrl) {
    [[NSUserDefaults standardUserDefaults] setBool:NO forKey:@"enable_action_url"];
    [[NSUserDefaults standardUserDefaults] synchronize];
    
    BOOL isActionUrl = [[NSUserDefaults standardUserDefaults] boolForKey:@"enable_action_url"] ? YES : NO;
    
    NSLog(@"Desabilitou o comportamento DEFAULT da action URL -> %@", isActionUrl ? @"YES": @"NO");
}

@end


@implementation PushHandler

-(void)receivedBackgroundNotification:(UANotificationContent *)notificationContent completionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {
    [self handleNotification:notificationContent event:@"receivedBackgroundNotificationActionButton"];
    completionHandler(UIBackgroundFetchResultNoData);
}

-(void)receivedForegroundNotification:(UANotificationContent *)notificationContent completionHandler:(void (^)())completionHandler {
    [self handleNotification:notificationContent event:@"receivedForegroundNotification"];
    completionHandler();
}

-(void)receivedNotificationResponse:(UANotificationResponse *)notificationResponse completionHandler:(void (^)())completionHandler {

    NSString *event;

    if ([notificationResponse.actionIdentifier isEqualToString:UANotificationDefaultActionIdentifier]) {
        event = @"launchedFromNotification";
    } else {
        UANotificationAction *notificationAction = [self notificationActionForCategory:notificationResponse.notificationContent.categoryIdentifier
                                                                      actionIdentifier:notificationResponse.actionIdentifier];

        if (notificationAction.options & UNNotificationActionOptionForeground) {
            event = @"launchedFromNotificationActionButton";
        } else {
            event = @"receivedBackgroundNotificationActionButton";
        }
    }


    [self handleNotification:notificationResponse.notificationContent event:event];
    completionHandler();
}

- (void)handleNotification:(UANotificationContent *)notificationContent event:(NSString *)event {
    BOOL isUrlActionEnabled = [[NSUserDefaults standardUserDefaults] boolForKey:@"enable_action_url"] ? YES : NO;

    if (isUrlActionEnabled && notificationContent.notificationInfo[@"^u"]) {
        return;
    }

    if ([event isEqualToString:@"launchedFromNotification"]) {
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        [defaults setObject:notificationContent.notificationInfo forKey:@"push_notification_opened_from_background"];
        [defaults synchronize];
    }

    [[ReactNativeUAIOS getInstance] dispatchEvent:@"receivedNotification" body:@{@"event": event,
                                                                                 @"data": notificationContent.notificationInfo}];
}

- (UANotificationAction *)notificationActionForCategory:(NSString *)category actionIdentifier:(NSString *)identifier {
    NSSet *categories = [UAirship push].combinedCategories;

    UANotificationCategory *notificationCategory;
    UANotificationAction *notificationAction;

    for (UANotificationCategory *possibleCategory in categories) {
        if ([possibleCategory.identifier isEqualToString:category]) {
            notificationCategory = possibleCategory;
            break;
        }
    }

    if (!notificationCategory) {
        return nil;
    }

    NSMutableArray *possibleActions = [NSMutableArray arrayWithArray:notificationCategory.actions];

    for (UANotificationAction *possibleAction in possibleActions) {
        if ([possibleAction.identifier isEqualToString:identifier]) {
            notificationAction = possibleAction;
            break;
        }
    }

    return notificationAction;
}


@end
