package com.wix.pagedcontacts.contacts;

import com.facebook.react.bridge.ReactApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class ContactsProviderFactory {
    private Map<String, ContactsProvider> contactsProviders = new HashMap<>();
    private ReactApplicationContext context;

    public ContactsProviderFactory(ReactApplicationContext context) {
        this.context = context;
    }

    public ContactsProvider get(String uuid) {
        if (contactsProviders.containsKey(uuid)) {
            return contactsProviders.get(uuid);
        }
        ContactsProvider provider = new ContactsProvider(context);
        contactsProviders.put(uuid, provider);
        return provider;
    }
}
