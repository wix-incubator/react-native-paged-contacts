package com.wix.pagedcontacts.contacts.Items;

import android.database.Cursor;
import android.text.TextUtils;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.Field;
import com.wix.pagedcontacts.contacts.query.QueryParams;
import androidx.annotation.Nullable;

abstract class ContactItem {
    Cursor cursor;

    ContactItem(Cursor cursor) {
        this.cursor = cursor;
    }

    ContactItem() {
    }

    WritableMap toMap(QueryParams params) {
        WritableMap map = Arguments.createMap();
        fillMap(map, params);
        return map;
    }

    protected abstract void fillMap(WritableMap map, QueryParams params);

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

    void addField(WritableMap map, QueryParams params, Field field, String value) {
        if (!TextUtils.isEmpty(value) && params.fetchField(field)) {
            map.putString(field.getKey(), value);
        }
    }
}
