package com.wix.pagedcontacts.contacts.Items;

import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EmptyReadableArray implements ReadableArray {

    public EmptyReadableArray() {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isNull(int index) {
        return false;
    }

    @Override
    public boolean getBoolean(int index) {
        return false;
    }

    @Override
    public double getDouble(int index) {
        return 0;
    }

    @Override
    public int getInt(int index) {
        return 0;
    }

    @Nullable
    @Override
    public String getString(int index) {
        return null;
    }

    @Nullable
    @Override
    public ReadableArray getArray(int index) {
        return null;
    }

    @Nullable
    @Override
    public ReadableMap getMap(int index) {
        return null;
    }

    @NonNull
    @Override
    public Dynamic getDynamic(int index) {
        return null;
    }

    @NonNull
    @Override
    public ReadableType getType(int index) {
        return null;
    }

    @NonNull
    @Override
    public ArrayList<Object> toArrayList() {
        return null;
    }
}
