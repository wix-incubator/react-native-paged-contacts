//
//  WXContactImageView.m
//  RCTPagedContacts
//
//  Created by Sergey Ilyevsky on 29/05/2017.
//  Copyright © 2017 rt2zz. All rights reserved.
//

#import "WXContactImageView.h"
#import "WXContactsManager.h"
#import "WXContactsManagersStore.h"

@implementation WXContactImageView

- (void)didMoveToWindow
{
    [super didMoveToWindow];
    
    if(!self.window) {
        self.image = nil;
        return;
    }
    
    if(!_contactId || !_pagedContactsId) {
        return;
    }
    
    WXContactsManager* manager = [[WXContactsManagersStore sharedInstance] managerForIdentifier:_pagedContactsId];
    NSString *key = [_imageType isEqual:@"image"] ? CNContactImageDataKey : CNContactThumbnailImageDataKey;
    NSArray<CNContact *> *contacts = [manager contactsWithIdentifiers:@[_contactId] keysToFetch:@[key]];
    CNContact *contact = contacts.firstObject;
    NSData *imageData = [contact valueForKey:key];
    UIImage *image = imageData ? [UIImage imageWithData:imageData] : nil;
    self.image = image;
}

@end
