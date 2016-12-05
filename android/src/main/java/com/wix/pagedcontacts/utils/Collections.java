package com.wix.pagedcontacts.utils;

import android.support.annotation.Nullable;

import com.facebook.react.bridge.ReadableArray;

import java.util.ArrayList;
import java.util.List;

public class Collections {
    public static boolean isEmpty(ReadableArray array) {
        return array == null || array.size() == 0;
    }

    public static List<String> toStringList(@Nullable ReadableArray array) {
        if (array == null) {
            return null;
        }
        List<String> result = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            result.add(array.getString(i));
        }
        return result;
    }
}
