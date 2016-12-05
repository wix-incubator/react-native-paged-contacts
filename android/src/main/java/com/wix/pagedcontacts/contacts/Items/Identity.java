package com.wix.pagedcontacts.contacts.Items;

import android.database.Cursor;
import android.provider.ContactsContract;

import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.Field;
import com.wix.pagedcontacts.contacts.QueryParams;

public class Identity extends ContactItem {
    private String namespace;
    private String identity;

    public Identity() {

    }

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
        addField(map, params, Field.identity, namespace);
        addField(map, params, Field.identity, identity);
    }
}
