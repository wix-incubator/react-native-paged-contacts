#import <AddressBook/AddressBook.h>
#import <UIKit/UIKit.h>
#import "RCTPagedContactsModule.h"

#import "WXContactsManager.h"
@import ObjectiveC;

@implementation RCTPagedContactsModule
{
	dispatch_queue_t _managerDispatchQueue;
	NSMutableDictionary<NSString*, WXContactsManager*>* _managerMapping;
	NSDateFormatter* _jsonDateFormatter;
	CNContactFormatter* _displayNameFormatter;
}

- (instancetype)init
{
	self = [super init];
	
	if(self)
	{
		_managerDispatchQueue = dispatch_queue_create("_managerDispatchQueue", NULL);
		_managerMapping = [NSMutableDictionary new];
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

RCT_EXPORT_METHOD(getAuthorizationStatus:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
	resolve(@([WXContactsManager authorizationStatus]));
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

RCT_EXPORT_METHOD(addContact:(NSDictionary*)contactData identifier:(NSString*)identifier) {
    WXContactsManager* manager = [self _managerForIdentifier:identifier];
    
    CNMutableContact * contact = [[CNMutableContact alloc] init];
    [self _updateCNContactWithContactData:contact withData:contactData];
    
    [manager saveContact:contact];
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

-(void) _updateCNContactWithContactData:(CNMutableContact *)contact withData:(NSDictionary *)contactData
{
    NSString *givenName = [contactData valueForKey:@"givenName"];
    NSString *familyName = [contactData valueForKey:@"familyName"];
    NSString *middleName = [contactData valueForKey:@"middleName"];
    NSString *nickname = [contactData valueForKey:@"nickname"];
    NSString *namePrefix = [contactData valueForKey:@"namePrefix"];
    NSString *nameSuffix = [contactData valueForKey:@"nameSuffix"];
    NSString *departmentName = [contactData valueForKey:@"departmentName"];
    NSString *organizationName = [contactData valueForKey:@"organizationName"];
    NSString *jobTitle = [contactData valueForKey:@"jobTitle"];
    NSString *note = [contactData valueForKey:@"note"];

    contact.givenName = givenName;
    contact.familyName = familyName;
    contact.middleName = middleName;
    contact.namePrefix = namePrefix;
    contact.nameSuffix = nameSuffix;
    contact.nickname = nickname;
    contact.organizationName = organizationName;
    contact.departmentName = departmentName;
    contact.jobTitle = jobTitle;
    contact.note = note;


    NSMutableArray *phoneNumbers = [[NSMutableArray alloc]init];

    for (id phoneData in [contactData valueForKey:@"phoneNumbers"]) {
        NSString *label = [phoneData valueForKey:@"label"];
        NSString *number = [phoneData valueForKey:@"value"];

        CNLabeledValue *phone;
        if ([label isEqual: @"main"]){
            phone = [[CNLabeledValue alloc] initWithLabel:CNLabelPhoneNumberMain value:[[CNPhoneNumber alloc] initWithStringValue:number]];
        }
        else if ([label isEqual: @"mobile"]){
            phone = [[CNLabeledValue alloc] initWithLabel:CNLabelPhoneNumberMobile value:[[CNPhoneNumber alloc] initWithStringValue:number]];
        }
        else if ([label isEqual: @"iPhone"]){
            phone = [[CNLabeledValue alloc] initWithLabel:CNLabelPhoneNumberiPhone value:[[CNPhoneNumber alloc] initWithStringValue:number]];
        }
        else{
            phone = [[CNLabeledValue alloc] initWithLabel:label value:[[CNPhoneNumber alloc] initWithStringValue:number]];
        }

        [phoneNumbers addObject:phone];
    }
    contact.phoneNumbers = phoneNumbers;


    NSMutableArray *urls = [[NSMutableArray alloc]init];

    for (id urlData in [contactData valueForKey:@"urlAddresses"]) {
        NSString *label = [urlData valueForKey:@"label"];
        NSString *url = [urlData valueForKey:@"value"];

        if(label && url) {
            [urls addObject:[[CNLabeledValue alloc] initWithLabel:label value:url]];
        }
    }

    contact.urlAddresses = urls;


    NSMutableArray *emails = [[NSMutableArray alloc]init];

    for (id emailData in [contactData valueForKey:@"emailAddresses"]) {
        NSString *label = [emailData valueForKey:@"label"];
        NSString *email = [emailData valueForKey:@"value"];

        if(label && email) {
            [emails addObject:[[CNLabeledValue alloc] initWithLabel:label value:email]];
        }
    }

    contact.emailAddresses = emails;

    NSMutableArray *postalAddresses = [[NSMutableArray alloc]init];

    for (id addressData in [contactData valueForKey:@"postalAddresses"]) {
        NSString *label = [addressData valueForKey:@"label"];

        NSString* postalAddressData = [addressData valueForKey:@"value"];

        NSString *street = [postalAddressData valueForKey:@"street"];
        NSString *postalCode = [postalAddressData valueForKey:@"postCode"];
        NSString *city = [postalAddressData valueForKey:@"city"];
        NSString *country = [postalAddressData valueForKey:@"country"];
        NSString *state = [postalAddressData valueForKey:@"state"];
        
        if(label && street) {
            CNMutablePostalAddress *postalAddr = [[CNMutablePostalAddress alloc] init];
            postalAddr.street = street;
            postalAddr.postalCode = postalCode;
            postalAddr.city = city;
            postalAddr.country = country;
            postalAddr.state = state;
            [postalAddresses addObject:[[CNLabeledValue alloc] initWithLabel:label value: postalAddr]];
        }
    }

    contact.postalAddresses = postalAddresses;

    NSString *imageUrl = [contactData valueForKey:@"imageUrl"];

    if(imageUrl) {
        contact.imageData = [RCTPagedContactsModule imageDataFromUrl:imageUrl];
    }
}

+ (NSData*) imageDataFromUrl:(NSString*)sourceUrl
{
    NSURL* url = [[NSURL alloc] initWithString:sourceUrl];
    return [NSData dataWithContentsOfURL:url];
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
	
	WXContactsManager* manager = [self _managerForIdentifier:identifier];
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
	
	WXContactsManager* manager = [self _managerForIdentifier:identifier];
	NSArray* contacts = [manager contactsWithIdentifiers:identifiers keysToFetch:realKeysToFetch];
	
	resolve([self _transformCNContactsToContactDatas:contacts keysToFetch:keysToFetch managerForObscureContacts:manager]);
}


+ (BOOL)requiresMainQueueSetup
{
    return YES;
}

@end
