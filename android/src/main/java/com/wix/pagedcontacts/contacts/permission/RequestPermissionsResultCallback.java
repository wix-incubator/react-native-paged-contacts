package com.wix.pagedcontacts.contacts.permission;

import com.wix.pagedcontacts.PagedContactsModule;

public class RequestPermissionsResultCallback {
    private PagedContactsModule pagedContactsModule;

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        pagedContactsModule.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void setModule(PagedContactsModule pagedContactsModule) {
        this.pagedContactsModule = pagedContactsModule;
    }
}
