//
//  WXContactImageViewManager.m
//  RCTPagedContacts
//
//  Created by Sergey Ilyevsky on 29/05/2017.
//  Copyright © 2017 rt2zz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <React/RCTViewManager.h>
#import "WXContactImageView.h"

@interface WXContactImageViewManager : RCTViewManager

@end

@implementation WXContactImageViewManager

RCT_EXPORT_MODULE()

- (UIView *)view
{
    return [[WXContactImageView alloc] initWithFrame:CGRectZero];
}

RCT_EXPORT_VIEW_PROPERTY(pagedContactsId, NSString *)
RCT_EXPORT_VIEW_PROPERTY(contactId, NSString *)
RCT_EXPORT_VIEW_PROPERTY(imageType, NSString *)

@end
