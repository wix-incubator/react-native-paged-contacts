package com.wix.pagedcontacts.contacts.query;

import android.provider.ContactsContract;
import android.text.TextUtils;

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
        return combineMimeTypeAndContactsSelection(getMimeTypeSelection(), getContactSelection());
    }

    private String combineMimeTypeAndContactsSelection(String mimeTypeSelection, String contactSelection) {
        String result = "";
        if (!TextUtils.isEmpty(mimeTypeSelection)) {
            result = mimeTypeSelection;
        }
        if (!TextUtils.isEmpty(contactSelection)) {
            if (!TextUtils.isEmpty(mimeTypeSelection)) {
                result = "(" + result + ") AND (" + contactSelection + ")";
            } else {
                result = contactSelection;
            }
        }
        return result.isEmpty() ? null : result;
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
        if (count == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(columnName);
        sb.append(" in (");
        for (int i = 0; i < count; i++) {
            if (i == count - 1) {
                sb.append("?");
            } else {
                sb.append("?,");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    private String getMatchNameSelection() {
        return ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";
    }
}
