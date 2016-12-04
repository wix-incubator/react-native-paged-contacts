package com.wix.pagedcontacts.utils;

import com.facebook.react.bridge.ReadableArray;

import java.util.ArrayList;
import java.util.List;

public class RnCollections {
    public static boolean isEmpty(ReadableArray array) {
        return array == null || array.size() == 0;
    }

    public static List<String> toStringList(ReadableArray array) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            result.add(array.getString(i));
        }
        return result;
    }
}
