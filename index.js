import {
  NativeModules,
  Platform
} from 'react-native';

const PagedContactsModule = NativeModules.ReactNativePagedContacts;

function guid() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    let r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
    return v.toString(16);
  });
}

export class PagedContacts {
    constructor(nameMatch) {
        this._uuid = guid();
        this.setNameMatch(nameMatch);
    }
    
    async requestAccess() {
        return ReactNative.NativeModules.PagedContactsModule.requestAccess(this._uuid);
    }

  async requestAccess() {
    return PagedContactsModule.requestAccess(this._uuid);
  }

  setNameMatch(str) {
    this._nameMatch = str;
    PagedContactsModule.setNameMatch(this._uuid, str);
  }

  async getContactsCount() {
    return PagedContactsModule.contactsCount(this._uuid);
  }

  async getContactsWithRange(offset, batchSize, keysToFetch) {
    return PagedContactsModule.getContactsWithRange(this._uuid, offset, batchSize, keysToFetch);
  }

  async getContactsWithIdentifiers(identifiers, keysToFetch) {
    return PagedContactsModule.getContactsWithIdentifiers(this._uuid, identifiers, keysToFetch);
  }

  dispose() {
    if (Platform.OS === 'ios') {
      PagedContactsModule.dispose(this._uuid);
    }
  }
}

PagedContacts.identifier = PagedContactsModule.identifier;
PagedContacts.displayName = PagedContactsModule.displayName;
PagedContacts.namePrefix = PagedContactsModule.namePrefix;
PagedContacts.givenName = PagedContactsModule.givenName;
PagedContacts.middleName = PagedContactsModule.middleName;
PagedContacts.familyName = PagedContactsModule.familyName;
PagedContacts.nameSuffix = PagedContactsModule.nameSuffix;
PagedContacts.nickname = PagedContactsModule.nickname;
PagedContacts.organizationName = PagedContactsModule.organizationName;
PagedContacts.departmentName = PagedContactsModule.departmentName;
PagedContacts.jobTitle = PagedContactsModule.jobTitle;
PagedContacts.phoneticGivenName = PagedContactsModule.phoneticGivenName;
PagedContacts.phoneticMiddleName = PagedContactsModule.phoneticMiddleName;
PagedContacts.phoneticFamilyName = PagedContactsModule.phoneticFamilyName;
PagedContacts.phoneticOrganizationName = PagedContactsModule.phoneticOrganizationName;
PagedContacts.birthday = PagedContactsModule.birthday;
PagedContacts.note = PagedContactsModule.note;
PagedContacts.imageData = PagedContactsModule.imageData;
PagedContacts.thumbnailImageData = PagedContactsModule.thumbnailImageData;
PagedContacts.phoneNumbers = PagedContactsModule.phoneNumbers;
PagedContacts.emailAddresses = PagedContactsModule.emailAddresses;
PagedContacts.postalAddresses = PagedContactsModule.postalAddresses;
PagedContacts.dates = PagedContactsModule.dates;
PagedContacts.urlAddresses = PagedContactsModule.urlAddresses;
PagedContacts.relations = PagedContactsModule.relations;
PagedContacts.instantMessageAddresses = PagedContactsModule.instantMessageAddresses;

if (Platform.OS === 'ios') {
  PagedContacts.previousFamilyName = PagedContactsModule.previousFamilyName;
  PagedContacts.nonGregorianBirthday = PagedContactsModule.nonGregorianBirthday;
  PagedContacts.socialProfiles = PagedContactsModule.socialProfiles;
}
if (Platform.OS === 'android') {
  PagedContacts.identity = PagedContactsModule.identity;
}
