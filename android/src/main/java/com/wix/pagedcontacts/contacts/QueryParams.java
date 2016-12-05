package com.wix.pagedcontacts.contacts;

import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QueryParams {
    int offset;
    int size;
    private Set<String> selectionArgs;
    private List<String> keysToFetch;
    private List<String> identifiers = new ArrayList<>();
    private Set<Field> fields;
    private String matchName;

    List<String> getIdentifiers() {
        return identifiers;
    }

    public QueryParams(String matchName) {
        this.keysToFetch = new ArrayList<>();
        this.keysToFetch.add("displayName");
        this.matchName = matchName;
        init();
    }

    public QueryParams(List<String> keysToFetch, List<String> identifiers) {
        this.keysToFetch = keysToFetch;
        this.identifiers = identifiers;
        init();
    }

    public QueryParams(List<String> keysToFetch, int offset, int size) {
        this.keysToFetch = keysToFetch;
        this.keysToFetch.add("identity");
        this.offset = offset;
        this.size = size;
        init();
    }

    private void init() {
        fields = new HashSet<>();
        selectionArgs = new HashSet<>();
    }

    String[] getProjection() {
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
        projection.add(ContactsContract.RawContacts.SOURCE_ID);
        return projection.toArray(new String[projection.size()]);
    }

    String getSelection() {
        if (matchName != null) {
            return getMatchNameSelection();
        }
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
        return selectionArgs.size() > 0 ? sb.toString() : null;
    }

    private String getMatchNameSelection() {
        return ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";
    }

    public boolean fetchField(Field field) {
        return fields.contains(field);
    }

    String[] getSelectionArgs() {
        if (matchName != null) {
            return getMatchNameSelectionArgs();
        }
        return getMimeTypeSelectionArgs();
    }

    private String[] getMatchNameSelectionArgs() {
        return new String[]{"%" + matchName + "%"};
    }

    private String[] getMimeTypeSelectionArgs() {
        return selectionArgs.isEmpty() ? null : selectionArgs.toArray(new String[selectionArgs.size()]);
    }
}
