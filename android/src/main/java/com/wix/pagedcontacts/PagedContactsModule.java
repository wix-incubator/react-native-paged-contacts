package com.wix.pagedcontacts;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.ContactsProvider;
import com.wix.pagedcontacts.contacts.Field;
import com.wix.pagedcontacts.contacts.QueryParams;

import java.util.HashMap;
import java.util.Map;

public class PagedContactsModule extends ReactContextBaseJavaModule {
    private final ContactsProvider contactsProvider;

    public PagedContactsModule(ReactApplicationContext context) {
        super(context);
        contactsProvider = new ContactsProvider(context);
        context.addLifecycleEventListener(new LifecycleEventListener() {
            @Override
            public void onHostResume() {
                syncContactsOnAppResumed();
            }

            @Override
            public void onHostPause() {

            }

            @Override
            public void onHostDestroy() {

            }

            private void syncContactsOnAppResumed() {
                contactsProvider.sync();
            }
        });
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
    public void contactsCount(String uuid, Promise promise) {
        final int count = contactsProvider.getContactsCount();
        WritableMap args = Arguments.createMap();
        args.putInt("count", count);
        promise.resolve(args);
    }

    @ReactMethod
    public void getContactsWithRange(String uuid, int offset, int size, ReadableArray keysToFetch, Promise promise) {
        QueryParams params = new QueryParams(keysToFetch, contactsProvider.getContactsCount(), offset, size);
        WritableArray contacts = contactsProvider.getContactsWithRange(params);
        setContactsAndResolve(promise, contacts);
    }

    @ReactMethod
    public void getContactsWithIdentifiers(String uuid, ReadableArray identifiers, ReadableArray keysToFetch, Promise promise) {
        WritableArray contacts = contactsProvider.getContactsWithIdentifiers(new QueryParams(keysToFetch, identifiers));
        setContactsAndResolve(promise, contacts);
    }

    private void setContactsAndResolve(Promise promise, WritableArray contacts) {
        WritableMap args = Arguments.createMap();
        args.putArray("contacts", contacts);
        promise.resolve(args);
    }
}