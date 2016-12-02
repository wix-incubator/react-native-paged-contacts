package com.wix.pagedcontacts.contacts.Items;

import android.database.Cursor;
import android.support.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

abstract class ContactItem {
    Cursor cursor;

    ContactItem(Cursor cursor) {
        this.cursor = cursor;
    }

    ContactItem() {
    }

    WritableMap toMap() {
        WritableMap map = Arguments.createMap();
        fillMap(map);
        return map;
    }

    protected abstract void fillMap(WritableMap map);

    @Nullable
    Integer getInt(String key) {
        final int columnIndex = cursor.getColumnIndex(key);
        return columnIndex != -1 ? cursor.getInt(columnIndex) : null;
    }

    @Nullable
    String getString(String key) {
        final int columnIndex = cursor.getColumnIndex(key);
        return columnIndex != -1 ? cursor.getString(columnIndex) : null;
    }

    @Nullable
    byte[] getBlob(String key) {
        final int columnIndex = cursor.getColumnIndex(key);
        return columnIndex != -1 ? cursor.getBlob(columnIndex) : null;
    }

    void addToMap(WritableMap map, String key, String value) {
        if (value != null) {
            map.putString(key, value);
        }
    }

    protected void putString(WritableMap map, String key, String value) {
        if (value != null) {
            map.putString(key, value);
        }
    }
}
