package com.wix.pagedcontacts.contacts.Items;

import android.content.ContentProviderOperation;
import android.database.Cursor;
import android.text.TextUtils;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.Field;
import com.wix.pagedcontacts.contacts.query.QueryParams;

import java.util.ArrayList;

import androidx.annotation.Nullable;

interface withCreationOp {
    void addCreationOp(ArrayList<ContentProviderOperation> ops);
}


abstract class ContactItem {
    Cursor cursor;
    private ReadableMap contactMap;


    ContactItem(Cursor cursor) {
        this.cursor = cursor;
    }

    ContactItem(ReadableMap contactMap) {this.contactMap = contactMap; }

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
    String getMapString(Field key) {
        String keyString = key.getKey();
        return contactMap.hasKey(keyString) ? contactMap.getString(keyString) : null;
    }

    @Nullable
    int getMapInt(Field key) {
        String keyString = key.getKey();
        return contactMap.hasKey(keyString) ? contactMap.getInt(keyString) : null;
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
