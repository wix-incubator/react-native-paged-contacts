package com.wix.pagedcontacts.contacts.readers;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.wix.pagedcontacts.contacts.Items.Contact;
import com.wix.pagedcontacts.contacts.Items.ContactItemReader;
import com.wix.pagedcontacts.contacts.Items.DisplayName;
import com.wix.pagedcontacts.contacts.Items.Identity;
import com.wix.pagedcontacts.contacts.Items.InvalidCursorTypeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ContactCursorReader {
    private Map<String, Contact> contacts;
    private Context context;

    public ContactCursorReader(Context context) {
        this.context = context;
        contacts = new HashMap<>();
    }

    public Contact read(Cursor cursor) {
        Contact contact = new Contact(getId(cursor));
        contact.identity = new Identity(cursor);
        contact.displayName = new DisplayName(cursor);
        return contact;
    }

    public List<Contact> readWithIds(Cursor cursor) {
        Set<String> fetchedContacts = new HashSet<>();
        List<Contact> contacts = new ArrayList<>();
        while (cursor.moveToNext()) {
            Contact contact = read(cursor, getId(cursor));
            if (!fetchedContacts.contains(contact.getContactId())) {
                fetchedContacts.add(contact.getContactId());
                contacts.add(contact);
            }
        }
        return contacts;
    }

    private Contact read(Cursor cursor, String contactId) {
        Contact contact = getContact(contactId);
        contact.displayName = new DisplayName(cursor);
        readField(cursor, contact);
        return contact;
    }

    private void readField(Cursor cursor, Contact contact) {
        try {
            new ContactItemReader(contact, context).read(cursor);
        } catch (InvalidCursorTypeException e) {
            // Nothing
        }
    }

    private Contact getContact(String contactId) {
        Contact contact = contacts.get(contactId);
        if (contact == null) {
            contact = new Contact(contactId);
            contacts.put(contactId, contact);
        }
        return contact;
    }

    private String getId(Cursor cursor) {
        return String.valueOf(cursor.getInt(cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID)));
    }
}
