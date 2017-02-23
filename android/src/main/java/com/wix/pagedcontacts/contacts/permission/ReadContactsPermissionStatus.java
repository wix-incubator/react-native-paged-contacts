package com.wix.pagedcontacts.contacts.permission;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;

import com.wix.pagedcontacts.PagedContactsModule;

import java.util.Map;

public enum ReadContactsPermissionStatus {
    authorized(PermissionChecker.PERMISSION_GRANTED, "authorized"),
    notDetermined(-2, "notDetermined"),
    denied(PermissionChecker.PERMISSION_DENIED, "denied");

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

    public static boolean isAuthorized(Activity context) {
        return getAuthorizationStatus(context).equals(authorized.value);
    }

    public static String getAuthorizationStatus(Activity context) {
        final int statusCode = PermissionChecker.checkCallingOrSelfPermission(context, Manifest.permission.READ_CONTACTS);
        if (statusCode == authorized.code) {
            return authorized.value;
        }
        if (shouldShowRequestPermission(context)) {
            return notDetermined.value;
        }
        return denied.value;
    }

    private static boolean shouldShowRequestPermission(Activity context) {
        return ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.READ_CONTACTS);
    }

    public static void requestAccess(Activity context) {
        ActivityCompat.requestPermissions(context,
                new String[]{Manifest.permission.READ_CONTACTS},
                PagedContactsModule.READ_CONTACTS_PERMISSION_REQUEST_CODE);
    }
}
