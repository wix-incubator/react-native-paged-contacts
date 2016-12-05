package com.wix.pagedcontacts.contacts.query;

import android.provider.ContactsContract;

import com.wix.pagedcontacts.utils.Collections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Selection {
    private String matchName;
    private Set<String> selectionArgs;
    private List<String> contactsToFetch;

    Selection(String matchName, Set<String> selectionArgs, List<String> contactsToFetch) {
        this.matchName = matchName;
        this.selectionArgs = selectionArgs;
        this.contactsToFetch = contactsToFetch != null ? contactsToFetch : new ArrayList<String>();
    }

    public String get() {
        if (matchName != null) {
            return getMatchNameSelection();
        }
        String mimeTypeSelection = getMimeTypeSelection();
        if (contactsToFetch.isEmpty()) {
            return mimeTypeSelection;
        } else {
            String selection = "(" + mimeTypeSelection + ") AND (" +  getContactSelection() + ")";
            return selection;
        }
    }

    private String getContactSelection() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < contactsToFetch.size(); i++) {
            sb.append(ContactsContract.Data.CONTACT_ID);
            if (i == contactsToFetch.size() - 1) {
                sb.append("=?");
            } else {
                sb.append("=? OR ");
            }
        }
        return sb.toString();
    }

    public String[] getArgs() {
        if (matchName != null) {
            return getMatchNameSelectionArgs();
        }
        String[] mimeTypeSelectionArgs = getMimeTypeSelectionArgs();
        if (contactsToFetch.isEmpty()) {
            return mimeTypeSelectionArgs;
        } else {
            return Collections.concat(mimeTypeSelectionArgs, contactsToFetch.toArray(new String[contactsToFetch.size()]));
        }
    }

    private String[] getMatchNameSelectionArgs() {
        return new String[]{"%" + matchName + "%"};
    }

    private String[] getMimeTypeSelectionArgs() {
        return selectionArgs.isEmpty() ? null : selectionArgs.toArray(new String[selectionArgs.size()]);
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
}
