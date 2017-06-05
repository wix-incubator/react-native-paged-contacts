//
// Created by Sergey Ilyevsky on 29/05/2017.
// Copyright (c) 2017 rt2zz. All rights reserved.
//

#import <Foundation/Foundation.h>

@class WXContactsManager;


@interface WXContactsManagersStore : NSObject
+ (WXContactsManagersStore *)sharedInstance;

- (WXContactsManager *)managerForIdentifier:(NSString *)identifier;

- (void)removeManagerWithIdentifier:(NSString *)identifier;
@end