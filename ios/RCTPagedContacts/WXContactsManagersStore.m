//
// Created by Sergey Ilyevsky on 29/05/2017.
// Copyright (c) 2017 rt2zz. All rights reserved.
//

#import "WXContactsManagersStore.h"
#import "WXContactsManager.h"


@implementation WXContactsManagersStore {
    dispatch_queue_t _managerDispatchQueue;
    NSMutableDictionary<NSString*, WXContactsManager*>* _managerMapping;
}

+ (WXContactsManagersStore *)sharedInstance
{
    static WXContactsManagersStore *instance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[WXContactsManagersStore alloc] init];
    });
    
    return instance;
}

- (instancetype)init {
    self = [super init];
    if (self) {
        _managerDispatchQueue = dispatch_queue_create("_managerDispatchQueue", NULL);
        _managerMapping = [NSMutableDictionary new];
    }

    return self;
}

- (WXContactsManager*)managerForIdentifier:(NSString*)identifier
{
    __block WXContactsManager* manager;
    dispatch_sync(_managerDispatchQueue, ^{
        manager = _managerMapping[identifier];
        if(manager == nil)
        {
            manager = [WXContactsManager new];
            _managerMapping[identifier] = manager;
        }
    });

    return manager;
}

- (void)removeManagerWithIdentifier:(NSString *)identifier
{
    dispatch_sync(_managerDispatchQueue, ^{
        [_managerMapping removeObjectForKey:identifier];
    });
}

@end
