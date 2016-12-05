package com.wix.pagedcontacts.contacts;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.facebook.react.bridge.WritableArray;
import com.wix.pagedcontacts.contacts.Items.Contact;
import com.wix.pagedcontacts.contacts.readers.ContactCursorReader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContactsProvider {
    private static final String TAG = "ContactsProvider";
    private Context context;
    private List<String> contactIds;
    private String matchName;

    public void setMatchName(String matchName) {
        this.matchName = matchName;
        contactIds.clear();
    }

    public ContactsProvider(Context context) {
        this.context = context;
        contactIds = new ArrayList<>();
    }

    private void sync() {
        contactIds = getAllContacts();
        Log.d(TAG, "sync: " + contactIds.size());
    }

    public int getContactsCount() {
        ensureContactIds();
        return contactIds.size();
    }

    private List<String> getAllContacts() {
        List<String> contactIds = new ArrayList<>();
        Set<String> dedupSet = new HashSet<>();
        Cursor cursor = queryAllContacts(new QueryParams(matchName));
        ContactCursorReader reader = new ContactCursorReader(context);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Contact contact = reader.read(cursor);
                Log.i("getAllContacts", "getAllContacts: " + contact.displayName.name);
                if (dedupSet.add(contact.contactId)) {
                    contactIds.add(contact.contactId);
                }
            }
            cursor.close();
        }
        return contactIds;
    }

    public WritableArray getContactsWithRange(QueryParams params) {
        ensureContactIds();
        Cursor cursor = queryAllContacts(params);
        return new ContactCursorReader(context).readWithIds(cursor, getContactsToFetch(params), params);
    }

    public WritableArray getContactsWithIdentifiers(QueryParams params) {
        ensureContactIds();
        Cursor cursor = queryAllContacts(params);
        return new ContactCursorReader(context).readWithIds(cursor, getContactsToFetch(params), params);
    }

    private void ensureContactIds() {
        if (contactIds.isEmpty()) {
            sync();
        }
    }

    private Cursor queryAllContacts(QueryParams params) {
        return context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                params.getProjection(),
                params.getSelection(),
                params.getSelectionArgs(),
                ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC"
        );
    }

    private List<String> getContactsToFetch(QueryParams params) {
        if (!params.getIdentifiers().isEmpty()) {
            return params.getIdentifiers();
        }
        List<String> ids = new ArrayList<>();
        int offset = params.offset < getContactsCount() - 1 ? params.offset : getContactsCount() - 1;
        int size = offset + params.size < getContactsCount() ? params.size : getContactsCount() - offset;
        for (String contactId : contactIds.subList(offset, size)) {
            ids.add(contactId);
        }
        return ids;
    }
}
