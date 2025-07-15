#import "KeychainStore.h"
#import <Security/Security.h>

@implementation KeychainStore
RCT_EXPORT_MODULE()

- (void)clear {
  NSDictionary *query = @{(__bridge id)kSecClass: (__bridge id)kSecClassGenericPassword};
  SecItemDelete((__bridge CFDictionaryRef)query);
}

- (NSString *)getItem:(NSString *)key {
  NSDictionary *query = @{(__bridge id)kSecClass: (__bridge id)kSecClassGenericPassword,
                          (__bridge id)kSecAttrAccount: key,
                          (__bridge id)kSecReturnData: @YES,
                          (__bridge id)kSecMatchLimit: (__bridge id)kSecMatchLimitOne};

  CFDataRef result = NULL;
  if (SecItemCopyMatching((__bridge CFDictionaryRef)query, (CFTypeRef *)&result) == errSecSuccess) {
    NSData *data = (__bridge_transfer NSData *)result;
    return [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
  }
  return nil;
}

- (NSNumber *)removeItem:(NSString *)key {
  NSDictionary *query = @{(__bridge id)kSecClass: (__bridge id)kSecClassGenericPassword,
                          (__bridge id)kSecAttrAccount: key};
  OSStatus status = SecItemDelete((__bridge CFDictionaryRef)query);
  return @(status == errSecSuccess);
}

- (NSNumber *)setItem:(NSString *)key val:(NSString *)val {
  [self removeItem:key];

  NSDictionary *query = @{
    (__bridge id)kSecClass: (__bridge id)kSecClassGenericPassword,
    (__bridge id)kSecAttrAccount: key,
    (__bridge id)kSecValueData: [val dataUsingEncoding:NSUTF8StringEncoding],
    (__bridge id)kSecAttrAccessible: (__bridge id)kSecAttrAccessibleAfterFirstUnlockThisDeviceOnly
  };

  OSStatus status = SecItemAdd((__bridge CFDictionaryRef)query, NULL);
  return @(status == errSecSuccess);
}


- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeKeychainStoreSpecJSI>(params);
}

@end
