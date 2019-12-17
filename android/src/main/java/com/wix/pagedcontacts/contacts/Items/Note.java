package com.wix.pagedcontacts.contacts.Items;

import android.content.ContentProviderOperation;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.Field;
import com.wix.pagedcontacts.contacts.query.QueryParams;

import java.util.ArrayList;

class Note extends ContactItem implements withCreationOp {
    private String note;
    Note(Cursor cursor) {
        super(cursor);
        fillFromCursor();
    }

    Note(ReadableMap contactMap) {
        super(contactMap);
        fillFromContactMap();
    }

    Note() {

    }

    private void fillFromContactMap() {
        note = getMapString(Field.note);
    }

    private void fillFromCursor() {
        note = getString(ContactsContract.CommonDataKinds.Note.NOTE);
    }

    @Override
    protected void fillMap(WritableMap map, QueryParams params) {
        addField(map, params, Field.note, note);
    }

    @Override
    public void addCreationOp(ArrayList<ContentProviderOperation> ops) {
        ContentProviderOperation.Builder op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Note.NOTE, note);
        ops.add(op.build());
    }
}
