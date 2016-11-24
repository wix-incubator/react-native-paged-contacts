//
//  WXContactManager.h
//  ContactsTest
//
//  Created by Leo Natan (Wix) on 23/11/2016.
//  Copyright © 2016 Leo Natan. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <Contacts/Contacts.h>

NS_ASSUME_NONNULL_BEGIN

@interface WXContactsManager : NSObject

@property (nonatomic, copy) NSString* nameMatch;

- (void)requestAccessWithCompletionHandler:(void (^)(BOOL granted,  NSError* __nullable error))completionHandler;

@property (nonatomic, readonly, assign) NSUInteger contactsCount;

- (NSArray<CNContact*>*)contactsWithRange:(NSRange)range keysToFetch:(nullable NSArray<NSString*>*)keysToFetch;
- (NSArray<CNContact*>*)contactsWithIdentifiers:(NSArray<NSString*>*)identifiers keysToFetch:(nullable NSArray<NSString*>*)keysToFetch;

@end

NS_ASSUME_NONNULL_END
