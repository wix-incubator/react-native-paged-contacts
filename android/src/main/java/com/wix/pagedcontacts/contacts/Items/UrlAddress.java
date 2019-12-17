package com.wix.pagedcontacts.contacts.Items;

import android.content.ContentProviderOperation;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Website;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.query.QueryParams;

import java.util.ArrayList;

class UrlAddress extends ContactItem implements withCreationOp {
    private String url;
    private String type;

    UrlAddress(Cursor cursor) {
        super(cursor);
        fillFromCursor();
    }

    UrlAddress(ReadableMap urlAddressMap) {
        super();
        fillFromUrlAddressMap(urlAddressMap);
    }

    private void fillFromUrlAddressMap(ReadableMap urlAddressMap) {
        url = urlAddressMap.getString(("value"));
    }

    private void fillFromCursor() {
        url = getString(Website.DATA);
        final Integer type = getInt(Website.TYPE);
        final String label = getString(Website.LABEL);
        this.type = getType(type, label);
    }

    private String getType(Integer type, String label) {
        if (type == null) {
            throw new InvalidCursorTypeException();
        }
        switch (type) {
            case Website.TYPE_BLOG:
                return "Blog";
            case Website.TYPE_FTP:
                return "FTP";
            case Website.TYPE_PROFILE:
                return "Profile";
            case Website.TYPE_HOME:
                return "Home";
            case Website.TYPE_HOMEPAGE:
                return "Home Page";
            case Website.TYPE_WORK:
                return "Work";
            case Website.TYPE_OTHER:
                return "Other";
            case Website.TYPE_CUSTOM:
            default:
                return label;
        }
    }

    @Override
    protected void fillMap(WritableMap map, QueryParams params) {
        map.putString(type, url);
    }

    @Override
    public void addCreationOp(ArrayList<ContentProviderOperation> ops) {
        ContentProviderOperation.Builder op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Website.URL, url);
        ops.add(op.build());
    }
}
