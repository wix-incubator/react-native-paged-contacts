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

    public ContactsProvider(Context context) {
        this.context = context;
        contactIds = new ArrayList<>();
    }

    public void sync() {
        contactIds = getContacts();
        Log.d(TAG, "sync: " + contactIds.size());
    }

    public int getContactsCount() {
        return contactIds.size();
    }

    private List<String> getContacts() {
        List<String> contacts = new ArrayList<>();
        Set<String> dedupSet = new HashSet<>();
        Cursor cursor = queryContacts();
        ContactCursorReader reader = new ContactCursorReader(context);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Contact contact = reader.read(cursor);
                if (dedupSet.add(contact.contactId)) {
                    contacts.add(contact.contactId);
                }
            }
            cursor.close();
        }
        return contacts;
    }

    private Cursor queryContacts() {
        return context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Data.CONTACT_ID, ContactsContract.RawContacts.SOURCE_ID},
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        );
    }

    public WritableArray getContactsWithRange(QueryParams params) {
        Cursor cursor = queryContactsWithRange(params);
        return new ContactCursorReader(context).readWithIds(cursor, getContactsToFetch(params.offset, params.size), params);
    }

    private Cursor queryContactsWithRange(QueryParams params) {
        return context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                params.getProjection(),
                params.getSelection(),
                params.getSelectionArgs(),
                null
        );
    }

    private List<String> getContactsToFetch(int offset, int size) {
        List<String> ids = new ArrayList<>();
        for (String contactId : contactIds.subList(offset, size)) {
            ids.add(contactId);
        }
        return ids;
    }
}
