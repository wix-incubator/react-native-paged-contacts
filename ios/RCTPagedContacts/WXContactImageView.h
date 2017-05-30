//
//  WXContactImageView.h
//  RCTPagedContacts
//
//  Created by Sergey Ilyevsky on 29/05/2017.
//  Copyright © 2017 rt2zz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface WXContactImageView : UIImageView

@property (nonatomic) NSString *pagedContactsId;
@property (nonatomic) NSString *contactId;
@property (nonatomic) NSString *imageType;

@end
