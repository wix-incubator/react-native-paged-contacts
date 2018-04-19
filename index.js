import {
  NativeModules,
  Platform
} from 'react-native';

const PagedContactsModule = NativeModules.ReactNativePagedContacts;

/**
 * Generates a globally unique identifier.
 * 
 * @returns {String} A globally unique identifier.
 */
function guid() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
    let r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
    return v.toString(16);
  });
}


/**
 * The `PagedContacts` class is a class that can fetch native contacts in pages.
 * 
 * @export
 * @class PagedContacts
 */
export class PagedContacts {
  /**
   * Creates an instance of PagedContacts.
   * 
   * @param {String} nameMatch The contact name to be matched
   * 
   * @memberOf PagedContacts
   */
  constructor(nameMatch) {
    this._uuid = guid();
    this.setNameMatch(nameMatch);
  }

  /**
   * Return the authorization status.
   * 
   * @returns {String} The authorization status
   * 
   * @memberOf PagedContacts
   */
  async getAuthorizationStatus() {
    return PagedContactsModule.getAuthorizationStatus();
  }

  /**
     * Requests contact access from the operating system. 
     * 
     * @returns {Boolean} `true` if access was granted or `false` otherwise
     * 
     * @memberOf PagedContacts
     */
  async requestAccess() {
    return PagedContactsModule.requestAccess(this._uuid);
  }

  /**
   * Set the contact name to be matched.
   * 
   * @param {String} str The contact name to be matched
   * 
   * @memberOf PagedContacts
   */
  setNameMatch(str) {
    this._nameMatch = str;
    PagedContactsModule.setNameMatch(this._uuid, str);
  }

  /**
   * Return the total number of contacts.
   * 
   * @returns {Number} The total number of contacts
   * 
   * @memberOf PagedContacts
   */
  async getContactsCount() {
    return PagedContactsModule.contactsCount(this._uuid);
  }

  /**
   * Fetches `batchSize` contacts, starting from `offset`, returning the provided keys.
   * 
   * @param {Number} offset The fetch offset
   * @param {Number} batchSize The fetch size
   * @param {String[]} keysToFetch The keys to fetch
   * @returns {Object[]} The fetched contacts  
   * 
   * @memberOf PagedContacts
   */
  async getContactsWithRange(offset, batchSize, keysToFetch) {
    return PagedContactsModule.getContactsWithRange(this._uuid, offset, batchSize, keysToFetch);
  }

  /**
   * Fetches contacts matching the provided identifiers, returning the provided keys.
   * 
   * @param {String[]} identifiers The contact identifiers to match
   * @param {String[]} keysToFetch The keys to fetch
   * @returns {Object[]} The fetched contacts
   * 
   * @memberOf PagedContacts
   */
  async getContactsWithIdentifiers(identifiers, keysToFetch) {
    return PagedContactsModule.getContactsWithIdentifiers(this._uuid, identifiers, keysToFetch);
  }

  /**
   * Disposes the underlying native component, freeing resources.
   * You must call this when the `PagedContacts` object is no longer needed.
   *
   * @memberOf PagedContacts
   */
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
  PagedContacts.lookupKey = PagedContactsModule.lookupKey;
}

PagedContacts.denied = PagedContactsModule.denied;
PagedContacts.authorized = PagedContactsModule.authorized;
if (Platform.OS === 'ios') {
  PagedContacts.notDetermined = PagedContactsModule.notDetermined;
  PagedContacts.restricted = PagedContactsModule.restricted;
}