package com.wix.pagedcontacts.contacts.Items;

import android.content.ContentProviderOperation;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.query.QueryParams;

import java.util.ArrayList;

class PhoneNumber extends ContactItem implements withCreationOp {
    public String type;
    private String number;

    PhoneNumber(Cursor cursor) {
        super(cursor);
        fillFromCursor();
    }

    PhoneNumber(ReadableMap phoneNumberMap) {
        super();
        fillFromNumberMap(phoneNumberMap);
    }

    private void fillFromNumberMap(ReadableMap phoneNumberMap) {
        number = phoneNumberMap.getString("value");
        type = phoneNumberMap.getString("label");
    }


    private void fillFromCursor() {
        String number = getString(ContactsContract.CommonDataKinds.Phone.NUMBER);
        String normalizedNumber = getString(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
        this.number = number != null ? number : normalizedNumber;

        Integer type = getInt(ContactsContract.CommonDataKinds.Phone.TYPE);
        this.type = getTypeString(type);
    }

    private String getTypeString(Integer type) {
        if (type == null) {
            throw new InvalidCursorTypeException();
        }
        switch (type) {
            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                return "home";
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                return "work";
            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                return "mobile";
            default:
                return "other";
        }
    }

    private int getTypeInt(String label) {
        switch (label) {
            case "home":
                return ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
            case "work":
                return ContactsContract.CommonDataKinds.Phone.TYPE_WORK;
            case "mobile":
                return ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
            default:
                return ContactsContract.CommonDataKinds.Phone.TYPE_OTHER;
        }
    }

    @Override
    protected void fillMap(WritableMap map, QueryParams params) {
        addToMap(map, "label", type);
        addToMap(map, "value", number);
    }

    public void addCreationOp(ArrayList<ContentProviderOperation> ops) {
        ContentProviderOperation.Builder op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,  getTypeInt(type));

        ops.add(op.build());
    }
}
