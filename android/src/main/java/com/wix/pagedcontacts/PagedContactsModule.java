package com.wix.pagedcontacts;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableArray;
import com.wix.pagedcontacts.contacts.ContactsProviderFactory;
import com.wix.pagedcontacts.contacts.Field;
import com.wix.pagedcontacts.contacts.query.QueryParams;
import com.wix.pagedcontacts.utils.Collections;

import java.util.HashMap;
import java.util.Map;

public class PagedContactsModule extends ReactContextBaseJavaModule {
    private final ContactsProviderFactory contactProvider;

    public PagedContactsModule(ReactApplicationContext context) {
        super(context);
        contactProvider = new ContactsProviderFactory(context);
    }

    @Override
    public String getName() {
        return "ReactNativePagedContacts";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        Field.exportToJs(constants);
        return constants;
    }

    @ReactMethod
    public void setNameMatch(String uuid, String nameMatch) {
        contactProvider.get(uuid).setMatchName(nameMatch);
    }

    @ReactMethod
    public void contactsCount(String uuid, Promise promise) {
        final int count = contactProvider.get(uuid).getContactsCount();
        promise.resolve(count);
    }

    @ReactMethod
    public void getContactsWithRange(String uuid, int offset, int size, ReadableArray keysToFetch, Promise promise) {
        QueryParams params = new QueryParams(Collections.toStringList(keysToFetch), offset, size);
        WritableArray contacts = contactProvider.get(uuid).getContactsWithRange(params);
        promise.resolve(contacts);
    }

    @ReactMethod
    public void getContactsWithIdentifiers(String uuid, ReadableArray identifiers, ReadableArray keysToFetch, Promise promise) {
        QueryParams params = new QueryParams(Collections.toStringList(keysToFetch), Collections.toStringList(identifiers));
        WritableArray contacts = contactProvider.get(uuid).getContactsWithIdentifiers(params);
        promise.resolve(contacts);
    }
}