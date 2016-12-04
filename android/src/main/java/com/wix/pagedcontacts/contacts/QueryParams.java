package com.wix.pagedcontacts.contacts;

import android.provider.ContactsContract;

import com.facebook.react.bridge.ReadableArray;
import com.wix.pagedcontacts.utils.RnCollections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QueryParams {
    int offset;
    int size;
    private Set<String> selectionArgs;
    private ReadableArray keysToFetch;
    private List<String> identifiers = new ArrayList<>();
    private Set<Field> fields;

    List<String> getIdentifiers() {
        return identifiers;
    }

    public QueryParams(ReadableArray keysToFetch, ReadableArray identifiers) {
        this.keysToFetch = keysToFetch;
        if (!RnCollections.isEmpty(identifiers)) {
            this.identifiers = RnCollections.toStringList(identifiers);
        }
        init();
    }

    public QueryParams(ReadableArray keysToFetch, int contactCount, int offset, int size) {
        this.keysToFetch = keysToFetch;
        this.offset = offset < contactCount - 1 ? offset : contactCount - 1;
        this.size = this.offset + size < contactCount ? size : contactCount - this.offset;
        init();
    }

    private void init() {
        fields = new HashSet<>();
        selectionArgs = new HashSet<>();
    }

    String[] getProjection() {
        Set<String> projection = new HashSet<>();
        for (int i = 0; i < keysToFetch.size(); i++) {
            Field field = Field.fromKey(keysToFetch.getString(i));
            Collections.addAll(projection, field.getProjection());
            field.addContentItemType(selectionArgs);
            fields.add(field);
        }
        projection.add(ContactsContract.Contacts.Data.MIMETYPE);
        projection.add(ContactsContract.Data.CONTACT_ID);
        projection.add(ContactsContract.RawContacts.SOURCE_ID);
        return projection.toArray(new String[projection.size()]);
    }

    String getSelection() {
        return getMimeTypeSelection();
    }

    private String getMimeTypeSelection() {
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
        return getMimeTypeSelectionArgs();
    }

    private String[] getMimeTypeSelectionArgs() {
        return selectionArgs.toArray(new String[selectionArgs.size()]);
    }
}
