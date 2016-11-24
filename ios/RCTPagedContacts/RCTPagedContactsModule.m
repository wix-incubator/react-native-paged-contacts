#import <AddressBook/AddressBook.h>
#import <UIKit/UIKit.h>
#import "RCTPagedContactsModule.h"

#import "WXContactsManager.h"

@implementation RCTPagedContactsModule
{
	dispatch_queue_t _managerDispatchQueue;
	NSMutableDictionary<NSString*, WXContactsManager*>* _managerMapping;
}

- (instancetype)init
{
	self = [super init];
	
	if(self)
	{
		_managerDispatchQueue = dispatch_queue_create("_managerDispatchQueue", NULL);
		_managerMapping = [NSMutableDictionary new];
	}
	
	return self;
}

RCT_EXPORT_MODULE();

- (NSDictionary *)constantsToExport
{
	return @{
			 @"identifier": CNContactIdentifierKey,
			 @"givenName": CNContactGivenNameKey,
			 @"middleName": CNContactMiddleNameKey,
			 @"familyName": CNContactFamilyNameKey,
			 @"phoneNumbers": CNContactPhoneNumbersKey,
			 @"emailAddresses": CNContactEmailAddressesKey,
			 @"thumbnailImageData": CNContactThumbnailImageDataKey,
			 };
}

- (WXContactsManager*)_managerForIdentifier:(NSString*)identifier
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

- (dispatch_queue_t)methodQueue
{
	return dispatch_queue_create("RCTPagedContactsModule", DISPATCH_QUEUE_SERIAL);
}

RCT_EXPORT_METHOD(requestAccess:(NSString*)identifier resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
	WXContactsManager* manager = [self _managerForIdentifier:identifier];
	
	[manager requestAccessWithCompletionHandler:^(BOOL granted, NSError* error) {
		if(error)
		{
			return reject(@(error.code).stringValue, error.localizedDescription, error);
		}
		
		return resolve(@(granted));
	}];
}

RCT_EXPORT_METHOD(dispose:(NSString*)identifier)
{
	dispatch_sync(_managerDispatchQueue, ^{
		[_managerMapping removeObjectForKey:identifier];
	});
}

RCT_EXPORT_METHOD(setNameMatch:(NSString*)identifier nameMatch:(NSString*)nameMatch)
{
	WXContactsManager* manager = [self _managerForIdentifier:identifier];
	manager.nameMatch = nameMatch;
}

RCT_EXPORT_METHOD(contactsCount:(NSString*)identifier  resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
	WXContactsManager* manager = [self _managerForIdentifier:identifier];
	resolve(@(manager.contactsCount));
}

- (id)_transformValueToJSValue:(id)value
{
	if([value isKindOfClass:[NSArray class]])
	{
		NSMutableArray* transformed = [NSMutableArray new];
		[(NSArray*)value enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
			[transformed addObject:[self _transformValueToJSValue:obj]];
		}];
		return transformed;
	}
	else if([value isKindOfClass:[CNLabeledValue class]])
	{
		return [self _transformValueToJSValue:[(CNLabeledValue*)value value]];
	}
	else if([value isKindOfClass:[CNPhoneNumber class]])
	{
		return [(CNPhoneNumber*)value stringValue];
	}
	else if([value isKindOfClass:[NSData class]])
	{
		return [(NSData*)value base64EncodedStringWithOptions:0];
	}
	else if([value respondsToSelector:@selector(stringValue)])
	{
		return [value performSelector:@selector(stringValue)];
	}
	
	return value;
}

- (NSArray<NSDictionary*>*)_transformCNContactsToContactDatas:(NSArray<CNContact*>*) contacts keysToFetch:(NSArray<NSString*>*)keysToFetch
{
	NSMutableArray* rv = [NSMutableArray new];
	
	[contacts enumerateObjectsUsingBlock:^(CNContact* _Nonnull contact, NSUInteger idx, BOOL * _Nonnull stop) {
		NSMutableDictionary<NSString*, id>* rvC = [NSMutableDictionary new];
		
		rvC[@"identifier"] = contact.identifier;
		[keysToFetch enumerateObjectsUsingBlock:^(NSString * _Nonnull key, NSUInteger idx, BOOL * _Nonnull stop) {
			rvC[key] = [self _transformValueToJSValue:[contact valueForKey:key]];
		}];
		
		[rv addObject:rvC];
	}];

	return rv;
}

RCT_EXPORT_METHOD(getContactsWithRange:(NSString*)identifier offset:(NSUInteger)offset batchSize:(NSUInteger)batchSize keysToFetch:(NSArray<NSString*>*)keysToFetch resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
	WXContactsManager* manager = [self _managerForIdentifier:identifier];
	NSArray<CNContact*>* contacts = [manager contactsWithRange:NSMakeRange(offset, batchSize) keysToFetch:keysToFetch];
	
	resolve([self _transformCNContactsToContactDatas:contacts keysToFetch:keysToFetch]);
}

RCT_EXPORT_METHOD(getContactsWithIdentifiers:(NSString*)identifier identifiers:(NSArray<NSString*>*)identifiers keysToFetch:(NSArray<NSString*>*)keysToFetch resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
	WXContactsManager* manager = [self _managerForIdentifier:identifier];
	NSArray* contacts = [manager contactsWithIdentifiers:identifiers keysToFetch:keysToFetch];
	
	resolve([self _transformCNContactsToContactDatas:contacts keysToFetch:keysToFetch]);
}    

@end
