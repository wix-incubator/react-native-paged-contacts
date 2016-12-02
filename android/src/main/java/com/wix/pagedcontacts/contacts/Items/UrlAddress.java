package com.wix.pagedcontacts.contacts.Items;

import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Website;

import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.QueryParams;

public class UrlAddress extends ContactItem {
    private String url;
    private String type;

    public UrlAddress(Cursor cursor) {
        super(cursor);
        fillFromCursor();
    }

    private void fillFromCursor() {
        url = getString(Website.DATA);
        final Integer type = getInt(Website.TYPE);
        final String label = getString(Website.LABEL);
        this.type = getType(type, label);
    }

    private String getType(int type, String label) {
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
}
