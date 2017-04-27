# React Native Paged Contacts

Paged contacts manager for React Native.

**Currently, only fetching contacts is supported.**

## Installation

#### iOS

* Add `RCTPagedContacts.xcodeproj` to your project.
* In you project's target, under `Build Phases` — `Target Dependencies`, add `RCTPagedContacts`.
* In you project's target, under `Build Phases` — `Link Libraries With Libraries`, add `RCTPagedContacts`.

#### Android
* Add the following to `settings.gradle`:

	```groovy
	include ':react-native-paged-contacts'
	project(':react-native-paged-contacts').projectDir = new File(
	        rootProject.projectDir, '../node_modules/react-native-paged-contacts/android/')
	```
* Update dependencies in `build.gradle`. 

	```groovy
	dependencies {
	    compile fileTree(dir: 'libs', include: ['*.jar'])
	    compile project(':react-native-paged-contacts')  // <— Add this
	    ...
	}
	```

* Add `react-native-paged-contacts` package, `new PagedContactsPackage()`, to `MainApplication.java`

	```java
	    import com.wix.pagedcontacts.PagedContactsPackage; // <- Add this

	    @Override
	    protected List<ReactPackage> getPackages() {
		return Arrays.<ReactPackage>asList(
		    new MainReactPackage(),
		    new PagedContactsPackage() // <- Add this
		);
	    }
	```
* Add `READ_CONTACTS` permission to `AndroidManifest.xml`

	```xml
	<uses-permission android:name="android.permission.READ_CONTACTS" />
	```

##API

- `new PagedContacts()` — Create a paged contacts manager for all device contacts.
- `new PagedContacts(nameMatch)` — Create a paged contacts manager for contacts matching the provided name.
- `getAuthorizationStatus()` — Returns the current authorization status to access the contact data.
- `requestAccess()` — Request contacts access from the operating system. This must be called before calling other APIs.
- `setNameMatch(matchName)` — Change the result set to filter contacts by matching name. Set to `null` to receive all contacts.
- `getContactsCount()` — Get the count of the current contacts set.
- `getContactsWithRange(offset, batchSize, keysToFetch)` — Get contacts within the requested `batchSize`, starting from `offset`. Only the keys requested in `keysToFetch` will be provided (contact identifiers are always provided).
- `getContactsWithIdentifiers(identifiers, keysToFetch)` — Get contacts with the provided `identifiers`. Only the keys requested in `keysToFetch` will be provided (contact identifiers are always provided).
- `dispose()` — Disposes the native components. Call this method when the manager object is no longer required. Must not call any other methods of the contacts manager after calling `dispose`.

####Authorization Status

- `PagedContacts.notDetermined` — The user has not yet made a choice regarding whether the application may access contact data.
- `PagedContacts.authorized` — The application is authorized to access contact data.
- `PagedContacts.denied` — The user explicitly denied access to contact data for the application.
- `PagedContacts.restricted` — The application is not authorized to access contact data. The user cannot change this application’s status, possibly due to active restrictions such as parental controls being in place.

####Available Keys to Fetch

- `PagedContacts.identifier` — The contact’s unique identifier.
- `PagedContacts.displayName`
- `PagedContacts.namePrefix` — Name prefix.
- `PagedContacts.givenName` — Given name.
- `PagedContacts.middleName` — Middle name.
- `PagedContacts.familyName` — Family prefix.
- `PagedContacts.previousFamilyName` — Previous family name. (**iOS only**)
- `PagedContacts.nameSuffix` — Name suffix.
- `PagedContacts.nickname` — Nickname.
- `PagedContacts.organizationName` — Organization name.
- `PagedContacts.departmentName` — Department name.
- `PagedContacts.jobTitle` — Job title.
- `PagedContacts.phoneticGivenName` — Phonetic given name.
- `PagedContacts.phoneticMiddleName` — Phonetic middle name.
- `PagedContacts.phoneticFamilyName` — Phonetic family name.
- `PagedContacts.phoneticOrganizationName` — Phonetic organization name.
- `PagedContacts.birthday` — Birthday.
- `PagedContacts.nonGregorianBirthday` — Non-Gregorian birthday. (**iOS only**)
- `PagedContacts.note` — Note.
- `PagedContacts.imageData` — Image data.
- `PagedContacts.thumbnailImageData` — Thumbnail data.
- `PagedContacts.phoneNumbers` — Phone numbers.
- `PagedContacts.emailAddresses` — Email addresses.
- `PagedContacts.postalAddresses` — Postal addresses.
- `PagedContacts.dates` — Contact dates.
- `PagedContacts.urlAddresses` — URL addresses.
- `PagedContacts.relations` — Contact relations.
- `PagedContacts.socialProfiles` — Social profiles. (**iOS only**)
- `PagedContacts.instantMessageAddresses` — Instant message addresses.

##Usage

Import the library and create a new `PagedContacts` instance.

```javascript
import {PagedContacts} from 'react-native-paged-contacts';
let pg = new PagedContacts();
```

First request authorization, and, if granted, request the contacts. (**iOS only**)

```javascript
pg.requestAccess().then((granted) => {
  if(granted !== true)
  {
    return; 
  }

  pg.getContactsCount().then( (count) => {
    pg.getContactsWithRange(0, count, [PagedContacts.displayName, PagedContacts.thumbnailImageData, PagedContacts.phoneNumbers, PagedContacts.emailAddresses]).then((contacts) => {
      //Use contacts here
    });
  });
});
```

This is a very intensive way of obtaining specific keys of all contacts. Instead, use the paging mechanism to obtain contacts within a range, and only request keys you need.

####Example of a Contact Result

```json
{
  "familyName": "Zakroff",
  "nonGregorianBirthday": "1961-12-25T22:00:00.000Z",
  "birthday": "1961-12-26T00:00:00.000Z",
  "contactRelations": [
    {
      "label": "sister",
      "value": "Kate Bell"
    }
  ],
  "nickname": "Hanky Panky",
  "displayName": "Prof. Hank M. Zakroff Esq.",
  "organizationName": "Financial Services Inc.",
  "departmentName": "Legal",
  "namePrefix": "Prof.",
  "nameSuffix": "Esq.",
  "socialProfiles": [
    {
      "label": "twitter",
      "value": {
        "urlString": "http:\/\/twitter.com\/HankyPanky",
        "username": "HankyPanky",
        "service": "Twitter"
      }
    },
    {
      "label": "facebook",
      "value": {
        "urlString": "http:\/\/www.facebook.com\/HankZakoff",
        "username": "HankZakoff",
        "service": "Facebook"
      }
    }
  ],
  "dates": [
    {
      "label": "anniversary",
      "value": "0001-12-01T00:00:00.000Z"
    },
    {
      "label": "other",
      "value": "2014-09-25T00:00:00.000Z"
    }
  ],
  "phoneNumbers": [
    {
      "label": "work",
      "value": "(555) 766-4823"
    },
    {
      "label": "other",
      "value": "(707) 555-1854"
    }
  ],
  "identifier": "60CB0169-0747-4494-9F10-22F387226676",
  "urlAddresses": [
    {
      "label": "homepage",
      "value": "https:\/\/google.com"
    }
  ],
  "postalAddresses": [
    {
      "label": "work",
      "value": {
        "ISOCountryCode": "us",
        "state": "CA",
        "street": "1741 Kearny Street",
        "city": "San Rafael",
        "country": "",
        "postalCode": "94901"
      }
    },
    {
      "label": "home",
      "value": {
        "ISOCountryCode": "il",
        "state": "",
        "street": "151 Jerusalem Avenue",
        "city": "Tel Aviv - Jaffa",
        "country": "Israel",
        "postalCode": "68152"
      }
    }
  ],
  "middleName": "M.",
  "jobTitle": "Partner",
  "note": "Best lawyer ever!",
  "emailAddresses": [
    {
      "label": "work",
      "value": "hank-zakroff@mac.com"
    }
  ],
  "givenName": "Hank",
  "instantMessageAddresses": [
    {
      "label": "Facebook",
      "value": {
        "service": "Facebook",
        "username": "HankZakoff"
      }
    },
    {
      "label": "Skype",
      "value": {
        "service": "Skype",
        "username": "HZakoff"
      }
    }
  ]
}
```
