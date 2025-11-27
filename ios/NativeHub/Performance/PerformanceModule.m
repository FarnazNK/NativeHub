#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(PerformanceModule, NSObject)
RCT_EXTERN_METHOD(getMetrics:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(getFrameMetrics:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(startTrace:(NSString *)traceName resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(stopTrace:(NSString *)traceName resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(startFPSMonitoring:(NSNumber * _Nullable)intervalMs resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(stopFPSMonitoring:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
+ (BOOL)requiresMainQueueSetup { return YES; }
@end
