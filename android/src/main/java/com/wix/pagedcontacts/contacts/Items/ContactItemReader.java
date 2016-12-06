package com.wix.pagedcontacts.contacts.Items;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

public class ContactItemReader {
    private Contact contact;
    private Context context;

    public ContactItemReader(Contact contact, Context context) {
        this.contact = contact;
        this.context = context;
    }

    public void read(Cursor cursor) throws InvalidCursorTypeException {
        if (!hasMimeType(cursor)) {
            return;
        }
        switch (getMimeType(cursor)) {
            case ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE:
                readName(cursor);
                break;
            case ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE:
                readNickname(cursor);
                break;
            case ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE:
                readPhoneNumbers(cursor);
                break;
            case ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE:
                readOrganization(cursor);
                break;
            case ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE:
                readNote(cursor);
                break;
            case ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE:
                readEvent(cursor);
                break;
            case ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE:
                readRelations(cursor);
                break;
            case ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE:
                readEmail(cursor);
                break;
            case ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE:
                readPostalAddress(cursor);
                break;
            case ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE:
                readImAddress(cursor);
                break;
            case ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE:
                readUrlAddress(cursor);
                break;
            case ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE:
                readPhoto(cursor);
                break;
            case ContactsContract.CommonDataKinds.Identity.CONTENT_ITEM_TYPE:
                readIdentity(cursor);
                break;
            default:
                break;
        }
    }

    private void readIdentity(Cursor cursor) {
        contact.identity = new Identity(cursor);
    }

    private void readPhoto(Cursor cursor) {
        contact.photo = new Photo(context, cursor);
    }

    private void readUrlAddress(Cursor cursor) {
        contact.urlAddresses.add(new UrlAddress(cursor));
    }

    private void readImAddress(Cursor cursor) {
        contact.instantMessagingAddresses.add(new InstantMessagingAddress(cursor));
    }

    private void readPostalAddress(Cursor cursor) {
        contact.postalAddresses.add(new PostalAddress(cursor));
    }

    private void readEmail(Cursor cursor) {
        contact.emails.add(new Email(cursor));
    }

    private void readRelations(Cursor cursor) {
        contact.relations.add(new Relation(cursor));
    }

    private void readEvent(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE);
        if (columnIndex <= -1) {
            return;
        }
        if (ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY == cursor.getInt(columnIndex)) {
            contact.birthday =
                    getString(cursor, cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));
        } else {
            contact.dates.add(new Date(cursor));
        }
    }

    private void readNote(Cursor cursor) {
        contact.note = new Note(cursor);
    }

    private void readOrganization(Cursor cursor) {
        contact.organization = new Organization(cursor);
    }

    private void readPhoneNumbers(Cursor cursor) {
        contact.phoneNumbers.add(new PhoneNumber(cursor));
    }

    private void readNickname(Cursor cursor) {
        contact.nickname = new Nickname(cursor);
    }

    private void readName(Cursor cursor) {
        contact.name = new Name(cursor);
    }

    private boolean hasMimeType(Cursor cursor) {
        return cursor.getColumnIndex(ContactsContract.Data.MIMETYPE) != -1;
    }

    private String getMimeType(Cursor cursor) {
        return getString(cursor, cursor.getColumnIndex(ContactsContract.Data.MIMETYPE));
    }

    private String getString(Cursor cursor, int index) {
        return index >= 0 ? cursor.getString(index) : null;
    }
}
