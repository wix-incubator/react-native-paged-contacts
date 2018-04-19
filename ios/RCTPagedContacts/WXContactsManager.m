//
//  WXContactManager.m
//  ContactsTest
//
//  Created by Leo Natan (Wix) on 23/11/2016.
//  Copyright © 2016 Leo Natan. All rights reserved.
//

#import "WXContactsManager.h"
@import Contacts;

@implementation WXContactsManager
{
	CNContactStore* _store;
	NSArray* _identifiers;
}

- (instancetype)init
{
	self = [super init];
	
	if(self)
	{
		_store = [CNContactStore new];
	}
	
	return self;
}

- (void)setNameMatch:(NSString *)nameMatch
{
	_nameMatch = nameMatch;
	_identifiers = nil;
}

- (NSArray*)_identifiers
{
	NSMutableArray* identifiers = [NSMutableArray new];
	
	CNContactFetchRequest *request = [[CNContactFetchRequest alloc] initWithKeysToFetch:@[CNContactIdentifierKey]];
	request.unifyResults = YES;
	request.sortOrder = CNContactSortOrderGivenName;
	if(_nameMatch != nil)
	{
		request.predicate = [CNContact predicateForContactsMatchingName:_nameMatch];
	}
	
	NSError* error = nil;
	if([_store enumerateContactsWithFetchRequest:request error:&error usingBlock:^(CNContact * _Nonnull contact, BOOL * _Nonnull stop) {
		[identifiers addObject:contact.identifier];
	}] == NO)
	{
		NSLog(@"Contact manager failed to obtain contact identifiers with error: %@", error);
	}
	
	_identifiers = identifiers;
	return _identifiers;
}

+ (CNAuthorizationStatus)authorizationStatus
{
	return [CNContactStore authorizationStatusForEntityType:CNEntityTypeContacts];
}

- (void)requestAccessWithCompletionHandler:(void (^)(BOOL granted, NSError* error))completionHandler
{
	if([CNContactStore authorizationStatusForEntityType:CNEntityTypeContacts] == CNAuthorizationStatusAuthorized)
	{
		completionHandler(YES, nil);
	}
	else
	{
		[_store requestAccessForEntityType:CNEntityTypeContacts completionHandler:^(BOOL granted, NSError * _Nullable error) {
			if(error)
			{
				NSLog(@"Contact manager got error when attempting to request access: %@", error);
			}
			
			completionHandler(granted, error);
		}];
	}
}

- (NSArray<CNContact*>*)contactsWithRange:(NSRange)range keysToFetch:(NSArray<NSString*>*)keysToFetch
{
	NSMutableArray* contacts = [NSMutableArray new];
    
    range.length = MIN(range.length, self._identifiers.count);
	
	NSArray* identifiers = [self._identifiers subarrayWithRange:range];
	
	CNContactFetchRequest *request = [[CNContactFetchRequest alloc] initWithKeysToFetch:@[CNContactIdentifierKey]];
	request.unifyResults = YES;
	request.sortOrder = CNContactSortOrderUserDefault;
	request.predicate = [CNContact predicateForContactsWithIdentifiers:identifiers];
	request.keysToFetch = keysToFetch;
	
	NSError* error = nil;
	if([_store enumerateContactsWithFetchRequest:request error:&error usingBlock:^(CNContact * _Nonnull contact, BOOL * _Nonnull stop) {
		[contacts addObject:contact];
	}] == NO)
	{
		NSLog(@"Contact manager failed to obtain contact from range with error: %@", error);
	}
	
	return contacts;
}

- (NSArray<CNContact*>*)contactsWithIdentifiers:(NSArray<NSString*>*)identifiers keysToFetch:(NSArray<NSString*>*)keysToFetch
{
	NSMutableArray* contacts = [NSMutableArray new];
	
	CNContactFetchRequest *request = [[CNContactFetchRequest alloc] initWithKeysToFetch:@[CNContactIdentifierKey]];
	request.unifyResults = YES;
	request.sortOrder = CNContactSortOrderUserDefault;
	request.predicate = [CNContact predicateForContactsWithIdentifiers:identifiers];
	request.keysToFetch = keysToFetch;
	
	NSError* error = nil;
	if([_store enumerateContactsWithFetchRequest:request error:&error usingBlock:^(CNContact * _Nonnull contact, BOOL * _Nonnull stop) {
		[contacts addObject:contact];
	}] == NO)
	{
		NSLog(@"Contact manager failed to obtain contact from identifiers with error: %@", error);
	}
	
	return contacts;
}

- (CNContact*)contactWithIdentifier:(NSString*)identifier keysToFetch:(nullable NSArray<NSString*>*)keysToFetch
{
	return [_store unifiedContactWithIdentifier:identifier keysToFetch:keysToFetch error:NULL];
}

- (NSUInteger)contactsCount
{
	return self._identifiers.count;
}

@end
