var ReactNative = require('react-native')

function guid() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        let r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
        return v.toString(16);
    });
}

export class PagedContacts {
    constructor(nameMatch) {
        this._uuid = guid();
        this._nameMatch = nameMatch;
    }
    
    async requestAccess() {
        return ReactNative.NativeModules.PagedContactsModule.requestAccess(this._uuid);
    }

    setNameMatch(str) {
        this._nameMatch = str;
        ReactNative.NativeModules.PagedContactsModule.setNameMatch(this._uuid, str);
    }

    async getContactsCount() {
        return ReactNative.NativeModules.PagedContactsModule.contactsCount(this._uuid);
    }

    async getContactsWithRange(offset, batchSize, keysToFetch) {
        return ReactNative.NativeModules.PagedContactsModule.getContactsWithRange(this._uuid, offset, batchSize, keysToFetch);
    }

    async getContactsWithIdentifiers(identifiers, keysToFetch) {
        return ReactNative.NativeModules.PagedContactsModule.getContactsWithIdentifiers(this._uuid, identifiers, keysToFetch);
    }

    dispose() {
        ReactNative.NativeModules.PagedContactsModule.dispose(this._uuid);
    }
}

// export {PagedContacts};