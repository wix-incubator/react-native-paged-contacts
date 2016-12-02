package com.wix.pagedcontacts.contacts;

import android.provider.ContactsContract;

import com.facebook.react.bridge.ReadableArray;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class QueryParams {
    int offset;
    int size;
    private Set<String> selectionArgs;
    private ReadableArray keysToFetch;
    private Set<Field> fields;

    public QueryParams(ReadableArray keysToFetch, int offset, int size) {
        fields = new HashSet<>();
        selectionArgs = new HashSet<>();
        this.keysToFetch = keysToFetch;
        this.offset = offset;
        this.size = size;
    }

    String[] getProjection() {
        Set<String> projection = new HashSet<>();
        for (int i = 0; i < keysToFetch.size(); i++) {
            Field field = Field.fromKey(keysToFetch.getString(i));
            Collections.addAll(projection, field.getProjection());
            selectionArgs.add(field.getContentItemType());
        }
        projection.add(ContactsContract.Contacts.Data.MIMETYPE);
        projection.add(ContactsContract.Data.CONTACT_ID);
        projection.add(ContactsContract.RawContacts.SOURCE_ID);
        return projection.toArray(new String[projection.size()]);
    }

    String getSelection() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < selectionArgs.size(); i++) {
            sb.append(ContactsContract.Data.MIMETYPE);
            if (i == selectionArgs.size() - 1) {
                sb.append("=?");
            } else {
                sb.append("=? OR ");
            }
        }
        return sb.toString();
    }

    public boolean fetchField(Field field) {
        return fields.contains(field);
    }

    String[] getSelectionArgs() {
        return selectionArgs.toArray(new String[selectionArgs.size()]);
    }
}
