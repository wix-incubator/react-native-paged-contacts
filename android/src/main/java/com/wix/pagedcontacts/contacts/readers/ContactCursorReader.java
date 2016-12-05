package com.wix.pagedcontacts.contacts.readers;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.wix.pagedcontacts.contacts.Items.Contact;
import com.wix.pagedcontacts.contacts.Items.Date;
import com.wix.pagedcontacts.contacts.Items.DisplayName;
import com.wix.pagedcontacts.contacts.Items.Email;
import com.wix.pagedcontacts.contacts.Items.Identity;
import com.wix.pagedcontacts.contacts.Items.InstantMessagingAddress;
import com.wix.pagedcontacts.contacts.Items.Name;
import com.wix.pagedcontacts.contacts.Items.Note;
import com.wix.pagedcontacts.contacts.Items.Organization;
import com.wix.pagedcontacts.contacts.Items.PhoneNumber;
import com.wix.pagedcontacts.contacts.Items.Photo;
import com.wix.pagedcontacts.contacts.Items.PostalAddress;
import com.wix.pagedcontacts.contacts.Items.Relation;
import com.wix.pagedcontacts.contacts.Items.UrlAddress;
import com.wix.pagedcontacts.contacts.QueryParams;

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

    public WritableArray readWithIds(Cursor cursor, List<String> contactsToFetch, QueryParams params) {
        Set<String> fetchedContacts = new HashSet<>();
        List<Contact> contacts = new ArrayList<>();
        while (cursor.moveToNext()) {
            final String id = getId(cursor);
            if (contactsToFetch.contains(id)) {
                Contact contact = read(cursor, id);
                if (!fetchedContacts.contains(contact.contactId)) {
                    fetchedContacts.add(contact.contactId);
                    contacts.add(contact);
                }
            }
        }
        WritableArray result = Arguments.createArray();
        for (Contact contact : contacts) {
            result.pushMap(contact.toMap(params));
        }
        return result;
    }

    private Contact read(Cursor cursor, String contactId) {
        Contact contact = getContact(contactId);
        contact.displayName = new DisplayName(cursor);
        readField(cursor, contact);
        return contact;
    }

    private void readField(Cursor cursor, Contact contact) {
        if (!hasMimeType(cursor)) {
            return;
        }
        switch (getMimeType(cursor)) {
            case StructuredName.CONTENT_ITEM_TYPE:
                contact.name = new Name(cursor);
                break;
            case Nickname.CONTENT_ITEM_TYPE:
                contact.nickname = new com.wix.pagedcontacts.contacts.Items.Nickname(cursor);
                break;
            case Phone.CONTENT_ITEM_TYPE:
                contact.phoneNumbers.add(new PhoneNumber(cursor));
                break;
            case ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE:
                contact.organization = new Organization(cursor);
                break;
            case ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE:
                contact.note = new Note(cursor);
                break;
            case ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE:
                final int type = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE));
                if (ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY == type) {
                    contact.birthday = getString(cursor, cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));
                } else {
                    contact.dates.add(new Date(cursor));
                }
                break;
            case ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE:
                contact.relations.add(new Relation(cursor));
                break;
            case ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE:
                contact.emails.add(new Email(cursor));
                break;
            case StructuredPostal.CONTENT_ITEM_TYPE:
                contact.postalAddresses.add(new PostalAddress(cursor));
                break;
            case ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE:
                contact.instantMessagingAddresses.add(new InstantMessagingAddress(cursor));
                break;
            case ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE:
                contact.urlAddresses.add(new UrlAddress(cursor));
                break;
            case ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE:
                contact.photo = new Photo(context, cursor);
                break;
            case ContactsContract.CommonDataKinds.Identity.CONTENT_ITEM_TYPE:
                contact.identity = new Identity(cursor);
                break;
            default:
                break;
        }
    }

    private String getMimeType(Cursor cursor) {
        return getString(cursor, cursor.getColumnIndex(ContactsContract.Data.MIMETYPE));
    }

    private boolean hasMimeType(Cursor cursor) {
        return cursor.getColumnIndex(ContactsContract.Data.MIMETYPE) != -1;
    }

    private Contact getContact(String contactId) {
        Contact contact = contacts.get(contactId);
        if (contact == null) {
            contact = new Contact(contactId);
            contacts.put(contactId, contact);
        }
        return contact;
    }

    private String getString(Cursor cursor, int index) {
        return index >= 0 ? cursor.getString(index) : null;
    }

    private String getId(Cursor cursor) {
        String contactId = null;
        final int contactIdColumnIdx = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID);
        if (contactIdColumnIdx != -1) {
            contactId = String.valueOf(cursor.getInt(contactIdColumnIdx));
        }
        return contactId;
    }
}
