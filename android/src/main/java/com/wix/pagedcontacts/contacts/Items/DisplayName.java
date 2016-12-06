package com.wix.pagedcontacts.contacts.Items;

import android.database.Cursor;
import android.provider.ContactsContract;

import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.Field;
import com.wix.pagedcontacts.contacts.query.QueryParams;

public class DisplayName extends ContactItem {
    public String name;

    public DisplayName(Cursor cursor) {
        super(cursor);
        fillFromCursor();
    }

    private void fillFromCursor() {
        name = getString(ContactsContract.Contacts.DISPLAY_NAME);
    }

    @Override
    protected void fillMap(WritableMap map, QueryParams params) {
        addField(map, params, Field.displayName, name);
    }
}
