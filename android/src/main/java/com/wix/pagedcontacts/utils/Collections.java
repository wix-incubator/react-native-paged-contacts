package com.wix.pagedcontacts.utils;

import androidx.annotation.Nullable;
import com.facebook.react.bridge.ReadableArray;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Collections {
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

    public static String[] concat(Collection<String> arg1, Collection<String> arg2) {
        if (arg1 == null && arg2 == null) {
            return null;
        }
        arg1 = arg1 == null ? new ArrayList<String>() : arg1;
        arg2 = arg2 == null ? new ArrayList<String>() : arg2;
        return concat(arg1.toArray(new String[arg1.size()]), arg2.toArray(new String[arg2.size()]));
    }

    private static String[] concat(String[]... arrays) {
        int length = 0;
        for (String[] array : arrays) {
            length += array.length;
        }
        String[] result = new String[length];
        int pos = 0;
        for (String[] array : arrays) {
            for (String element : array) {
                result[pos] = element;
                pos++;
            }
        }
        return result;
    }
}
