#import "RCTBridge.h"
#import "RCTEventDispatcher.h"
#import "ReactNativeUAIOS.h"


@interface ReactNativeUAIOS ()
@property(nonatomic, strong) PushHandler *pushHandler;
@end


@implementation ReactNativeUAIOS

@synthesize bridge = _bridge;

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(enableNotification) {
  [UAirship push].userPushNotificationsEnabled = YES;
}

RCT_EXPORT_METHOD(disableNotification) {
  [UAirship push].userPushNotificationsEnabled = NO;
}

RCT_EXPORT_METHOD(addTag:(NSString *)tag) {
  [[UAirship push] addTag:tag];
  [[UAirship push] updateRegistration];
}

RCT_EXPORT_METHOD(removeTag:(NSString *)tag) {
  [[UAirship push] removeTag:tag];
  [[UAirship push] updateRegistration];
}

RCT_EXPORT_METHOD(setTags:(NSArray *)tags) {
  [UAirship push].tags = tags;
  [[UAirship push] updateRegistration];
}

static ReactNativeUAIOS *instance = nil;

+ (ReactNativeUAIOS *)getInstance {
  return instance;
}

- (dispatch_queue_t)methodQueue {
  self.pushHandler = [PushHandler new];

  [UAirship push].pushNotificationDelegate = self.pushHandler;

  instance = self;

  return dispatch_get_main_queue();
}

+ (void)setupUrbanAirship {
  UAConfig *config = [UAConfig defaultConfig];
  [UAirship takeOff:config];
}

- (void)dispatchEvent:(NSString *)event body:(NSDictionary *)notification {
  [self.bridge.eventDispatcher sendAppEventWithName:event body:notification];
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
