import {
  NativeModules,
  Platform
} from 'react-native';
import * as keys from './src/keys';

const PagedContactsModule = NativeModules.ReactNativePagedContacts;

function guid() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    let r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
    return v.toString(16);
  });
}

export const KeysToFetch = keys;

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
    const result = await PagedContactsModule.contactsCount(this._uuid);
    return result.count;
  }

  async getContactsWithRange(offset, batchSize, keysToFetch) {
    const result = await PagedContactsModule.getContactsWithRange(this._uuid, offset, batchSize, keysToFetch);
    return result.contacts;
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

PagedContacts.identifier = ReactNative.NativeModules.PagedContactsModule.identifier;
PagedContacts.displayName = ReactNative.NativeModules.PagedContactsModule.displayName;
PagedContacts.namePrefix = ReactNative.NativeModules.PagedContactsModule.namePrefix;
PagedContacts.givenName = ReactNative.NativeModules.PagedContactsModule.givenName;
PagedContacts.middleName = ReactNative.NativeModules.PagedContactsModule.middleName;
PagedContacts.familyName = ReactNative.NativeModules.PagedContactsModule.familyName;
PagedContacts.previousFamilyName = ReactNative.NativeModules.PagedContactsModule.previousFamilyName;
PagedContacts.nameSuffix = ReactNative.NativeModules.PagedContactsModule.nameSuffix;
PagedContacts.nickname = ReactNative.NativeModules.PagedContactsModule.nickname;
PagedContacts.organizationName = ReactNative.NativeModules.PagedContactsModule.organizationName;
PagedContacts.departmentName = ReactNative.NativeModules.PagedContactsModule.departmentName;
PagedContacts.jobTitle = ReactNative.NativeModules.PagedContactsModule.jobTitle;
PagedContacts.phoneticGivenName = ReactNative.NativeModules.PagedContactsModule.phoneticGivenName;
PagedContacts.phoneticMiddleName = ReactNative.NativeModules.PagedContactsModule.phoneticMiddleName;
PagedContacts.phoneticFamilyName = ReactNative.NativeModules.PagedContactsModule.phoneticFamilyName;
PagedContacts.phoneticOrganizationName = ReactNative.NativeModules.PagedContactsModule.phoneticOrganizationName;
PagedContacts.birthday = ReactNative.NativeModules.PagedContactsModule.birthday;
PagedContacts.nonGregorianBirthday = ReactNative.NativeModules.PagedContactsModule.nonGregorianBirthday;
PagedContacts.note = ReactNative.NativeModules.PagedContactsModule.note;
PagedContacts.imageData = ReactNative.NativeModules.PagedContactsModule.imageData;
PagedContacts.thumbnailImageData = ReactNative.NativeModules.PagedContactsModule.thumbnailImageData;
PagedContacts.phoneNumbers = ReactNative.NativeModules.PagedContactsModule.phoneNumbers;
PagedContacts.emailAddresses = ReactNative.NativeModules.PagedContactsModule.emailAddresses;
PagedContacts.postalAddresses = ReactNative.NativeModules.PagedContactsModule.postalAddresses;
PagedContacts.dates = ReactNative.NativeModules.PagedContactsModule.dates;
PagedContacts.urlAddresses = ReactNative.NativeModules.PagedContactsModule.urlAddresses;
PagedContacts.relations = ReactNative.NativeModules.PagedContactsModule.relations;
PagedContacts.socialProfiles = ReactNative.NativeModules.PagedContactsModule.socialProfiles;
PagedContacts.instantMessageAddresses = ReactNative.NativeModules.PagedContactsModule.instantMessageAddresses;
