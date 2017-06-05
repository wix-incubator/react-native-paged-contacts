#import <AddressBook/AddressBook.h>
#import <UIKit/UIKit.h>
#import "RCTPagedContactsModule.h"
#import <React/RCTUIManager.h>

#import "WXContactsManager.h"
#import "WXContactsManagersStore.h"
@import ObjectiveC;

@implementation RCTPagedContactsModule
{
	NSDateFormatter* _jsonDateFormatter;
	CNContactFormatter* _displayNameFormatter;
}

- (instancetype)init
{
	self = [super init];
	
	if(self)
	{
		_jsonDateFormatter = [NSDateFormatter new];
		_jsonDateFormatter.dateFormat = @"yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'";
		_displayNameFormatter = [CNContactFormatter new];
		_displayNameFormatter.style = CNContactFormatterStyleFullName;
	}
	
	return self;
}

RCT_EXPORT_MODULE(ReactNativePagedContacts);

- (NSDictionary *)constantsToExport
{
	NSMutableDictionary *constants = [@{
										@"identifier": CNContactIdentifierKey,
										@"displayName": @"displayName",
										
										@"namePrefix": CNContactNamePrefixKey,
										@"givenName": CNContactGivenNameKey,
										@"middleName": CNContactMiddleNameKey,
										@"familyName": CNContactFamilyNameKey,
										@"previousFamilyName": CNContactPreviousFamilyNameKey,
										@"nameSuffix": CNContactNameSuffixKey,
										@"nickname": CNContactNicknameKey,
										@"organizationName": CNContactOrganizationNameKey,
										@"departmentName": CNContactDepartmentNameKey,
										@"jobTitle": CNContactJobTitleKey,
										@"phoneticGivenName": CNContactPhoneticGivenNameKey,
										@"phoneticMiddleName": CNContactPhoneticMiddleNameKey,
										@"phoneticFamilyName": CNContactPhoneticFamilyNameKey,
										@"birthday": CNContactBirthdayKey,
										@"nonGregorianBirthday": CNContactNonGregorianBirthdayKey,
										@"note": CNContactNoteKey,
										@"imageData": CNContactImageDataKey,
										@"thumbnailImageData": CNContactThumbnailImageDataKey,
										@"phoneNumbers": CNContactPhoneNumbersKey,
										@"emailAddresses": CNContactEmailAddressesKey,
										@"postalAddresses": CNContactPostalAddressesKey,
										@"dates": CNContactDatesKey,
										@"urlAddresses": CNContactUrlAddressesKey,
										@"socialProfiles": CNContactSocialProfilesKey,
										@"instantMessageAddresses": CNContactInstantMessageAddressesKey,
										@"relations": CNContactRelationsKey,
										} mutableCopy];
	
	// CNContactPhoneticOrganizationNameKey is only available in iOS10
	if (&CNContactPhoneticOrganizationNameKey != nil) {
		[constants setValue:CNContactPhoneticOrganizationNameKey forKey:@"phoneticOrganizationName"];
	}
	
	NSDictionary* authorizationStatusConstants = @{
												   @"denied": @(CNAuthorizationStatusDenied),
												   @"notDetermined": @(CNAuthorizationStatusNotDetermined),
												   @"authorized": @(CNAuthorizationStatusAuthorized),
												   @"restricted": @(CNAuthorizationStatusRestricted),
												   };
	
	[constants addEntriesFromDictionary:authorizationStatusConstants];
	
	return constants;
}

- (dispatch_queue_t)methodQueue
{
	return dispatch_queue_create("RCTPagedContactsModule", DISPATCH_QUEUE_SERIAL);
}

RCT_EXPORT_METHOD(getAuthorizationStatus:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
	resolve(@([WXContactsManager authorizationStatus]));
}

RCT_EXPORT_METHOD(requestAccess:(NSString*)identifier resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
	WXContactsManager* manager = [[WXContactsManagersStore sharedInstance] managerForIdentifier:identifier];
	
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
    [[WXContactsManagersStore sharedInstance] removeManagerWithIdentifier:identifier];
}

RCT_EXPORT_METHOD(setNameMatch:(NSString*)identifier nameMatch:(NSString*)nameMatch)
{
	WXContactsManager* manager = [[WXContactsManagersStore sharedInstance] managerForIdentifier:identifier];
	manager.nameMatch = nameMatch;
}

RCT_EXPORT_METHOD(contactsCount:(NSString*)identifier  resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
	WXContactsManager* manager = [[WXContactsManagersStore sharedInstance] managerForIdentifier:identifier];
	resolve(@(manager.contactsCount));
}

- (NSDictionary<NSString*, id>*)_flattenObject:(id)obj
{
	NSMutableDictionary<NSString*, id>* rv = [NSMutableDictionary new];
	
	unsigned int count = 0;
	objc_property_t *list = class_copyPropertyList(object_getClass(obj), &count);
	
	for(unsigned int i = 0; i < count; i++)
	{
		objc_property_t prop = list[i];
		NSString* propName = @(property_getName(prop));
		rv[propName] = [obj valueForKey:propName];
	}
	
	free(list);
	
	return rv;
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
		id label = [CNLabeledValue localizedStringForLabel:[(CNLabeledValue*)value label]];
		if (label == nil) label = @"";
		return @{@"label": label, @"value": [self _transformValueToJSValue:[(CNLabeledValue*)value value]]};
	}
	else if([value isKindOfClass:[CNPhoneNumber class]])
	{
		return [(CNPhoneNumber*)value stringValue];
	}
	else if([value isKindOfClass:[CNPostalAddress class]] || [value isKindOfClass:[CNSocialProfile class]] || [value isKindOfClass:[CNInstantMessageAddress class]])
	{
		return [value valueForKey:@"dictionaryRepresentation"];
	}
	else if([value isKindOfClass:[CNContactRelation class]])
	{
		return [(CNContactRelation*)value name];
	}
	else if([value isKindOfClass:[NSData class]])
	{
		return [(NSData*)value base64EncodedStringWithOptions:0];
	}
	else if([value isKindOfClass:[NSDateComponents class]])
	{
		return [[(NSDateComponents*)value calendar] dateFromComponents:value];
	}
	else if([value respondsToSelector:@selector(stringValue)])
	{
		return [value performSelector:@selector(stringValue)];
	}
	
	return value;
}

- (NSArray<NSDictionary*>*)_transformCNContactsToContactDatas:(NSArray<CNContact*>*) contacts keysToFetch:(NSArray<NSString*>*)keysToFetch managerForObscureContacts:(WXContactsManager*)manager
{
	NSMutableArray* rv = [NSMutableArray new];
	
	[contacts enumerateObjectsUsingBlock:^(CNContact* _Nonnull contact, NSUInteger idx, BOOL * _Nonnull stop) {
		NSMutableDictionary<NSString*, id>* rvC = [NSMutableDictionary new];
		
		rvC[@"identifier"] = contact.identifier;
		
		if([keysToFetch containsObject:@"displayName"])
		{
			NSString* displayName = [_displayNameFormatter stringFromContact:contact];
			if(displayName.length == 0)
			{
				if([contact isKeyAvailable:CNContactEmailAddressesKey] && contact.emailAddresses.count > 0)
				{
					displayName = contact.emailAddresses.firstObject.value;
				}
				else if([contact isKeyAvailable:CNContactPhoneNumbersKey] && contact.phoneNumbers.count > 0)
				{
					displayName = contact.phoneNumbers.firstObject.value.stringValue;
				}
				else
				{
					CNContact* fulfilledContact = [manager contactWithIdentifier:contact.identifier keysToFetch:@[CNContactEmailAddressesKey, CNContactPhoneNumbersKey]];
					displayName = fulfilledContact.emailAddresses.count > 0 ? fulfilledContact.emailAddresses.firstObject.value : fulfilledContact.phoneNumbers.firstObject.value.stringValue;
				}
			}
			rvC[@"displayName"] = displayName;
		}
		[keysToFetch enumerateObjectsUsingBlock:^(NSString * _Nonnull key, NSUInteger idx, BOOL * _Nonnull stop) {
			if([key isEqualToString:@"displayName"])
			{
				return;
			}
			
			id value = [self _transformValueToJSValue:[contact valueForKey:key]];
			if(value != nil
			   && ([value respondsToSelector:@selector(length)] == NO || [(NSString*)value length] > 0)
			   && ([value respondsToSelector:@selector(count)]  == NO || [(NSArray*)value count]   > 0))
			{
				rvC[key] = value;
			}
		}];
		
		[rv addObject:rvC];
	}];
	
	return rv;
}

- (NSArray*)_keysToFetchIncludingManadatoryKeys:(NSArray*)keysToFetch
{
	NSMutableSet* rvSet = [NSMutableSet setWithArray:keysToFetch];
	[rvSet addObject:CNContactIdentifierKey];
	if([keysToFetch containsObject:@"displayName"])
	{
		[rvSet removeObject:@"displayName"];
		[rvSet addObject:[CNContactFormatter descriptorForRequiredKeysForStyle:CNContactFormatterStyleFullName]];
	}
	
	return rvSet.allObjects;
}

RCT_EXPORT_METHOD(getContactsWithRange:(NSString*)identifier offset:(NSUInteger)offset batchSize:(NSUInteger)batchSize keysToFetch:(NSArray<NSString*>*)keysToFetch resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
	if(batchSize == 0)
	{
		return resolve(@[]);
	}
	
	NSArray* realKeysToFetch = [self _keysToFetchIncludingManadatoryKeys:keysToFetch];
	
	WXContactsManager* manager = [[WXContactsManagersStore sharedInstance] managerForIdentifier:identifier];
	NSArray<CNContact*>* contacts = [manager contactsWithRange:NSMakeRange(offset, batchSize) keysToFetch:realKeysToFetch];
	
	resolve([self _transformCNContactsToContactDatas:contacts keysToFetch:keysToFetch managerForObscureContacts:manager]);
}

RCT_EXPORT_METHOD(getContactsWithIdentifiers:(NSString*)identifier identifiers:(NSArray<NSString*>*)identifiers keysToFetch:(NSArray<NSString*>*)keysToFetch resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
	if(identifiers.count == 0)
	{
		return resolve(@[]);
	}
	
	NSArray* realKeysToFetch = [self _keysToFetchIncludingManadatoryKeys:keysToFetch];
	
	WXContactsManager* manager = [[WXContactsManagersStore sharedInstance] managerForIdentifier:identifier];
	NSArray* contacts = [manager contactsWithIdentifiers:identifiers keysToFetch:realKeysToFetch];
	
	resolve([self _transformCNContactsToContactDatas:contacts keysToFetch:keysToFetch managerForObscureContacts:manager]);
}

RCT_EXPORT_METHOD(setImageViewWithHandle:(nonnull NSNumber *)viewHandle contactId:(NSString *)contactId imageType:(NSString*)imageType managerId:(NSString *)managerId)
{
    WXContactsManager* manager = [[WXContactsManagersStore sharedInstance] managerForIdentifier:managerId];
    NSString *key = [imageType isEqual:@"image"] ? CNContactImageDataKey : CNContactThumbnailImageDataKey;
    NSArray<CNContact *> *contacts = [manager contactsWithIdentifiers:@[contactId] keysToFetch:@[key]];
    CNContact *contact = contacts.firstObject;
    NSData *imageData = [contact valueForKey:key];
    UIImage *image = imageData ? [UIImage imageWithData:imageData] : nil;
    
    RCTUIManager *uiManager = ((RCTBridge *)[RCTBridge valueForKey:@"currentBridge"]).uiManager;
    dispatch_async(RCTGetUIManagerQueue(), ^{
        [uiManager addUIBlock:^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, UIView *> *viewRegistry)
         {
             UIView *view = viewRegistry[viewHandle];
             if ([view isKindOfClass:[UIImageView class]])
             {
                 [(UIImageView *)view setImage:image];
             }
         }];
        [uiManager batchDidComplete];
    });
}

@end
