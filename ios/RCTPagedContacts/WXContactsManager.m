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
	NSArray* _chachedIdentifiers;
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
	_chachedIdentifiers = nil;
}

- (NSArray*)_chachedIdentifiers
{
	if(_chachedIdentifiers == nil)
	{
		NSMutableArray* identifiers = [NSMutableArray new];
		
		CNContactFetchRequest *request = [[CNContactFetchRequest alloc] initWithKeysToFetch:@[CNContactIdentifierKey]];
		request.unifyResults = YES;
		request.sortOrder = CNContactSortOrderUserDefault;
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
		
		_chachedIdentifiers = identifiers;
	}
	
	return _chachedIdentifiers;
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
	keysToFetch = [NSSet setWithArray:[keysToFetch arrayByAddingObject:CNContactIdentifierKey]].allObjects;
	
	NSMutableArray* contacts = [NSMutableArray new];
	
	NSArray* identifiers = [self._chachedIdentifiers subarrayWithRange:range];
	
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
	keysToFetch = [NSSet setWithArray:[keysToFetch arrayByAddingObject:CNContactIdentifierKey]].allObjects;
	
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

- (NSUInteger)contactsCount
{
	return self._chachedIdentifiers.count;
}

@end
