# React Native Paged Contacts

Paged contacts manager for React Native.

**Currently, only fetching contacts is supported.**

##API

- `new PagedContacts()` — Create a paged contacts manager for all device contacts.
- `new PagedContacts(nameMatch)` — Create a paged contacts manager for contacts matching the provided name.
- `requestAccess()` — Request contacts access from the operating system. This must be called before calling other APIs.
- `setNameMatch(matchName)` — Change the result set to filter contacts by matching name. Set to `null` to receive all contacts.
- `getContactsCount()` — Get the count of the current contacts set.
- `getContactsWithRange(offset, batchSize, keysToFetch)` — Get contacts within the requested `batchSize`, starting from `offset`. Only the keys requested in `keysToFetch` will be provided (contact identifiers are always provided).
- `getContactsWithIdentifiers(identifiers, keysToFetch)` — Get contacts with the provided `identifiers`. Only the keys requested in `keysToFetch` will be provided (contact identifiers are always provided).
- `dispose()` — Disposes the native components. Call this method when the manager object is no longer required. Must not call any other methods of the contacts manager after calling `dispose`.

####Available Keys to Fetch

- `PagedContacts.namePrefix`
- `PagedContacts.givenName`
- `PagedContacts.middleName`
- `PagedContacts.familyName`
- `PagedContacts.previousFamilyName`
- `PagedContacts.nameSuffix`
- `PagedContacts.nickname`
- `PagedContacts.organizationName`
- `PagedContacts.departmentName`
- `PagedContacts.jobTitle`
- `PagedContacts.phoneticGivenName`
- `PagedContacts.phoneticMiddleName`
- `PagedContacts.phoneticFamilyName`
- `PagedContacts.phoneticOrganizationName`
- `PagedContacts.birthday`
- `PagedContacts.nonGregorianBirthday`
- `PagedContacts.note`
- `PagedContacts.imageData`
- `PagedContacts.thumbnailImageData`
- `PagedContacts.phoneNumbers`
- `PagedContacts.emailAddresses`
- `PagedContacts.postalAddresses`
- `PagedContacts.dates`
- `PagedContacts.urlAddresses`
- `PagedContacts.relations`
- `PagedContacts.socialProfiles`
- `PagedContacts.instantMessageAddresses`

##Usage

Import the library and create a new `PagedContacts` instance.

```
import {PagedContacts} from 'react-native-paged-contacts';
let pg = new PagedContacts();
```

First request authorization, and, if granted, request the contacts.

```
pg.requestAccess().then((res) => {
  if(res !== true)
  {
    return; 
  }

  pg.getContactsCount().then( (count) => {
    pg.getContactsWithRange(0, count, [PagedContacts.givenName, PagedContacts.familyName, PagedContacts.thumbnailImageData, PagedContacts.phoneNumbers, PagedContacts.emailAddresses]).then((r) => {
      r.forEach((e) => {
        console.log(JSON.stringify(e));
      })
    });
  });
});
```

This is a very intensive way of obtaining specific keys of all contacts. Instead, use the paging mechanism to obtain contacts within a range, and only request keys you need.

###Example of a Contact Result

```
{
  "socialProfiles": [
    {
      "facebook": {
        "urlString": "http:\/\/www.facebook.com\/LeoNatan",
        "username": "LeoNatan",
        "service": "Facebook"
      }
    },
    {
      "twitter": {
        "urlString": "http:\/\/twitter.com\/LeoNatan",
        "username": "LeoNatan",
        "service": "Twitter"
      }
    }
  ],
  "namePrefix": "Dr.",
  "jobTitle": "Portfolio Manager",
  "note": "This is a sample note.",
  "postalAddresses": [
    {
      "work": {
        "ISOCountryCode": "us",
        "state": "CA",
        "street": "1741 Kearny Street",
        "city": "San Rafael",
        "country": "",
        "postalCode": "94901"
      }
    },
    {
      "home": {
        "ISOCountryCode": "il",
        "state": "",
        "street": "Sderot Yerushalayim 151",
        "city": "Tel Aviv - Jaffa",
        "country": "Israel",
        "postalCode": "68151"
      }
    }
  ],
  "emailAddresses": [
    {
      "work": "hank-zakroff@mac.com"
    },
    {
      "iCloud": "hank@icloud.com"
    }
  ],
  "phoneNumbers": [
    {
      "work": "(555) 766-4823"
    },
    {
      "other": "(707) 555-1854"
    }
  ],
  "givenName": "Hank",
  "middleName": "M.",
  "identifier": "1965A24F-361A-4FCB-804D-3A01E024D2FE",
  "birthday": "1980-10-25T00:00:00.000Z",
  "organizationName": "Financial Services Inc.",
  "instantMessageAddresses": [
    {
      "ICQ": {
        "service": "ICQ",
        "username": "12345678"
      }
    },
    {
      "Skype": {
        "service": "Skype",
        "username": "HankMMMM"
      }
    }
  ],
  "urlAddresses": [
    {
      "homepage": "https:\/\/www.google.com"
    }
  ],
  "contactRelations": [
    {
      "partner": "Kate Bell"
    }
  ],
  "nickname": "Hanky Panky",
  "familyName": "Zakroff",
  "dates": [
    {
      "anniversary": "0001-11-28T00:00:00.000Z"
    },
    {
      "Work Start": "2001-07-18T00:00:00.000Z"
    }
  ],
  "departmentName": "RND"
}
```