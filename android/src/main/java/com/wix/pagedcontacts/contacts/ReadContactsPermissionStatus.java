package com.wix.pagedcontacts.contacts;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import java.util.Map;

public enum ReadContactsPermissionStatus {
    authorized(PackageManager.PERMISSION_GRANTED, "authorized"),
    denied(PackageManager.PERMISSION_DENIED, "denied");

    private String value;
    private int code;

    ReadContactsPermissionStatus(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static void exportToJs(Map<String, Object> constants) {
        for (ReadContactsPermissionStatus field : values()) {
            constants.put(field.value, field.value);
        }
    }


    public static String check(Context context) {
        final int statusCode = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS);
        for (ReadContactsPermissionStatus status : values()) {
            if (status.code == statusCode) {
                return status.value;
            }
        }
        throw new RuntimeException("Unexpected permission status " + statusCode);
    }
}
