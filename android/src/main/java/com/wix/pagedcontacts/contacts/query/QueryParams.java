package com.wix.pagedcontacts.contacts.query;

import android.provider.ContactsContract;

import com.wix.pagedcontacts.contacts.ContactsProvider;
import com.wix.pagedcontacts.contacts.Field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QueryParams {
    public int offset;
    public int size;
    private Set<String> selectionArgs;
    private List<String> keysToFetch;
    private List<String> identifiers = new ArrayList<>();
    private Set<Field> fields;
    private String matchName;
    private List<String> contactsToFetch;

    public List<String> getIdentifiers() {
        return identifiers;
    }

    public QueryParams(String matchName) {
        this.keysToFetch = new ArrayList<>();
        this.matchName = matchName;
        init();
    }

    public QueryParams(List<String> keysToFetch, List<String> identifiers) {
        this.keysToFetch = keysToFetch;
        this.identifiers = identifiers;
        init();
    }

    public QueryParams(List<String> keysToFetch, int offset, int size) {
        if (size > ContactsProvider.MAX_ARGS) {
            throw new RuntimeException("Size must be smaller then " + ContactsProvider.MAX_ARGS);
        }
        this.keysToFetch = keysToFetch;
        this.offset = offset;
        this.size = size;
        init();
    }

    private void init() {
        fields = new HashSet<>();
        selectionArgs = new HashSet<>();
    }

    public String[] getProjection() {
        Set<String> projection = new HashSet<>();
        for (int i = 0; i < keysToFetch.size(); i++) {
            Field field = Field.fromKey(keysToFetch.get(i));
            Collections.addAll(projection, field.getProjection());
            String contentItemType = field.getContentItemType();
            if (contentItemType != null) {
                selectionArgs.add(contentItemType);
            }
            fields.add(field);
            if (field != Field.displayName) {
                projection.add(ContactsContract.Contacts.Data.MIMETYPE);
            }
        }
        projection.add(ContactsContract.Data.CONTACT_ID);
        return projection.toArray(new String[projection.size()]);
    }

    public String getSelection() {
        return new Selection(matchName, selectionArgs, contactsToFetch).getSelection();
    }

    public String[] getSelectionArgs() {
        String[] selectionArgs = new Selection(matchName, this.selectionArgs, contactsToFetch).getSelectionArgs();
        return selectionArgs != null && selectionArgs.length == 0 ? null : selectionArgs;
    }

    public boolean fetchField(Field field) {
        return fields.contains(field);
    }

    public void setContactsToFetch(List<String> contactsToFetch) {
        this.contactsToFetch = contactsToFetch;
    }
}
