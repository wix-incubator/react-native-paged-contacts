package com.wix.pagedcontacts.contacts.Items;

import android.content.ContentProviderOperation;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.Field;
import com.wix.pagedcontacts.contacts.query.QueryParams;

import java.util.ArrayList;

class Email extends ContactItem implements withCreationOp {
    private String label;
    private String address;

    Email(Cursor cursor) {
        super(cursor);
        fillFromCursor();
    }

    Email(ReadableMap emailMap) {
        super();
        fillFromEmailMap(emailMap);
    }

    private void fillFromEmailMap(ReadableMap emailMap) {
        address = emailMap.getString("value");
        label = emailMap.getString("label");
    }

    private void fillFromCursor() {
        address = getString(ContactsContract.CommonDataKinds.Email.ADDRESS);
        final String label = getString(ContactsContract.CommonDataKinds.Email.LABEL);
        final Integer typeId = getInt(ContactsContract.CommonDataKinds.Email.TYPE);
        this.label = getEmailTypeString(typeId, label);
    }

    private String getEmailTypeString(Integer type, String label) {
        if (type == null) {
            throw new InvalidCursorTypeException();
        }
        switch (type) {
            case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                return "home";
            case ContactsContract.CommonDataKinds.Email.TYPE_MOBILE:
                return "mobile";
            case ContactsContract.CommonDataKinds.Email.TYPE_WORK:
                return "work";
            case ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM:
                return label;
            default:
                return "other";
        }
    }

    private int getEmailTypeInteger(String label) {
        switch (label) {
            case "home":
                return ContactsContract.CommonDataKinds.Email.TYPE_HOME;
            case "work":
                return ContactsContract.CommonDataKinds.Email.TYPE_WORK;
            case "mobile":
                return ContactsContract.CommonDataKinds.Email.TYPE_MOBILE;
            default:
                return ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM;
        }
    }


    @Override
    protected void fillMap(WritableMap map, QueryParams params) {
        if (params.fetchField(Field.emailAddresses)) {
            addToMap(map, "label", label);
            addToMap(map, "value", address);
        }
    }

    @Override
    public void addCreationOp(ArrayList<ContentProviderOperation> ops) {
        ContentProviderOperation.Builder op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, address)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, getEmailTypeInteger(label));
        ops.add(op.build());
    }
}
