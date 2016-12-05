package com.wix.pagedcontacts.contacts.Items;

import android.database.Cursor;
import android.provider.ContactsContract;

import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.QueryParams;

public class Date extends ContactItem {
    private String startDate;
    private String type;

    public Date(Cursor cursor) {
        super(cursor);
        fillFromCursor();
    }

    private void fillFromCursor() {
        final Integer type = getInt(ContactsContract.CommonDataKinds.Event.TYPE);
        startDate = getString(ContactsContract.CommonDataKinds.Event.START_DATE);
        final String label = getString(ContactsContract.CommonDataKinds.Event.LABEL);
        this.type = getEventType(type, label);
    }

    private String getEventType(Integer type, String label) {
        if (label != null) {
            return label;
        }
        if (type == ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY) {
            return "anniversary";
        }
        return "other";
    }

    @Override
    protected void fillMap(WritableMap map, QueryParams queryParams) {
        map.putString(type, startDate);
    }
}
