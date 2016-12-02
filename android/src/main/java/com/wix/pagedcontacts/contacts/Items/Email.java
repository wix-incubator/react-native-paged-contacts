package com.wix.pagedcontacts.contacts.Items;

import android.database.Cursor;
import android.provider.ContactsContract;

import com.facebook.react.bridge.WritableMap;

public class Email extends ContactItem {
    private String type;
    private String address;

    public Email(Cursor cursor) {
        super(cursor);
        fillFromCursor();
    }

    private void fillFromCursor() {
        address = getString(ContactsContract.CommonDataKinds.Email.ADDRESS);
        final String label = getString(ContactsContract.CommonDataKinds.Email.LABEL);
        final Integer typeId = getInt(ContactsContract.CommonDataKinds.Email.TYPE);
        type = getEmailType(typeId, label);
    }

    private String getEmailType(Integer type, String label) {
        if (label != null) {
            return label;
        }
        switch (type) {
            case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                return "home";
            case ContactsContract.CommonDataKinds.Email.TYPE_MOBILE:
                return "mobile";
            case ContactsContract.CommonDataKinds.Email.TYPE_WORK:
                return "work";
            default:
                return "other";
        }
    }

    @Override
    protected void fillMap(WritableMap map) {
        map.putString(type, address);
    }
}
