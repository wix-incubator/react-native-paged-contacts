package com.wix.pagedcontacts.contacts.Items;

import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Event;

import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.query.QueryParams;

class Date extends ContactItem {
    private String startDate;
    private String type;

    Date(Cursor cursor) {
        super(cursor);
        fillFromCursor();
    }

    private void fillFromCursor() {
        final Integer type = getInt(Event.TYPE);
        startDate = getString(Event.START_DATE);
        final String label = getString(Event.LABEL);
        this.type = getEventType(type, label);
    }

    private String getEventType(Integer type, String label) {
        if (type == null) {
            throw new InvalidCursorTypeException();
        }
        switch (type) {
            case Event.TYPE_ANNIVERSARY:
                return "anniversary";
            case Event.TYPE_OTHER:
                return "other";
            case Event.TYPE_CUSTOM:
                return label;
            default:
                return "other";
        }
    }

    @Override
    protected void fillMap(WritableMap map, QueryParams queryParams) {
        map.putString("label", type);
        map.putString("value", startDate);
    }
}
