package com.wix.pagedcontacts.contacts.Items;

import android.database.Cursor;
import android.provider.ContactsContract;

import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.Field;
import com.wix.pagedcontacts.contacts.query.QueryParams;

class Note extends ContactItem {
    private String note;
    Note(Cursor cursor) {
        super(cursor);
        fillFromCursor();
    }

    Note() {

    }

    private void fillFromCursor() {
        note = getString(ContactsContract.CommonDataKinds.Note.NOTE);
    }

    @Override
    protected void fillMap(WritableMap map, QueryParams params) {
        addField(map, params, Field.note, note);
    }
}
