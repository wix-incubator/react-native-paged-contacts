package com.wix.pagedcontacts.contacts.query;

import android.provider.ContactsContract;

import com.wix.pagedcontacts.utils.Collections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class Selection {
    private String matchName;
    private Set<String> selectionArgs;
    private List<String> contactsToFetch;

    Selection(String matchName, Set<String> selectionArgs, List<String> contactsToFetch) {
        this.matchName = matchName;
        this.selectionArgs = selectionArgs;
        this.contactsToFetch = contactsToFetch != null ? contactsToFetch : new ArrayList<String>();
    }

    String getSelection() {
        if (matchName != null) {
            return getMatchNameSelection();
        }
        if (contactsToFetch.isEmpty()) {
            return getMimeTypeSelection();
        } else {
            return "(" + getMimeTypeSelection() + ") AND (" + getContactSelection() + ")";
        }
    }

    String[] getSelectionArgs() {
        if (matchName != null) {
            return getMatchNameSelectionArgs();
        }
        return Collections.concat(selectionArgs, contactsToFetch);
    }

    private String[] getMatchNameSelectionArgs() {
        return new String[]{"%" + matchName + "%"};
    }

    private String getMimeTypeSelection() {
        return getSelection(ContactsContract.Data.MIMETYPE, selectionArgs.size());
    }

    private String getContactSelection() {
        return getSelection(ContactsContract.Data.CONTACT_ID, contactsToFetch.size());
    }

    private String getSelection(String columnName, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(columnName);
            if (i == count - 1) {
                sb.append("=?");
            } else {
                sb.append("=? OR ");
            }
        }
        return sb.toString();
    }

    private String getMatchNameSelection() {
        return ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";
    }
}
