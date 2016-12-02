package com.wix.pagedcontacts.contacts;

import android.provider.ContactsContract;

import com.facebook.react.bridge.ReadableArray;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class QueryParams {
    private ReadableArray keysToFetch;
    int offset;
    int size;
    private Set<Field> fields;

    public QueryParams(ReadableArray keysToFetch, int offset, int size) {
        fields = new HashSet<>();
        this.keysToFetch = keysToFetch;
        this.offset = offset;
        this.size = size;
    }

    String[] getProjection() {
        Set<String> projection = new HashSet<>();
        for (int i = 0; i < keysToFetch.size(); i++) {
            Collections.addAll(projection, Field.fromKey(keysToFetch.getString(i)).getProjection());
        }
        projection.add(ContactsContract.Contacts.Data.MIMETYPE);
        projection.add(ContactsContract.Data.CONTACT_ID);
        projection.add(ContactsContract.RawContacts.SOURCE_ID);
        return projection.toArray(new String[projection.size()]);
    }

    public boolean fetchField(Field field) {
        return fields.contains(field);
    }
}
