package com.wix.pagedcontacts.contacts.Items;

import android.database.Cursor;
import android.provider.ContactsContract;

import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.QueryParams;

public class Identity extends ContactItem {
    String namespace;
    String identity;

    public Identity(Cursor cursor) {
        super(cursor);
        fillFromCursor();
    }

    private void fillFromCursor() {
        namespace = getString(ContactsContract.CommonDataKinds.Identity.NAMESPACE);
        identity = getString(ContactsContract.CommonDataKinds.Identity.IDENTITY);
    }

    @Override
    protected void fillMap(WritableMap map, QueryParams params) {
        addToMap(map, "nameSpace", namespace);
        addToMap(map, "identity", identity);
    }
}
