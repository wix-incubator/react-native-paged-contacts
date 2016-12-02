package com.wix.pagedcontacts.contacts.Items;

import android.database.Cursor;
import android.provider.ContactsContract;

import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.Field;

public class Organization extends ContactItem {
    private String organizationName;
    private String departmentName;
    private String jobTitle;
    private String phoneticOrganizationName;

    Organization() {
        super();
    }

    public Organization(Cursor cursor) {
        super(cursor);
        fillFromCursor();
    }

    private void fillFromCursor() {
        organizationName = getString(ContactsContract.CommonDataKinds.Organization.COMPANY);
        departmentName = getString(ContactsContract.CommonDataKinds.Organization.DEPARTMENT);
        jobTitle = getString(ContactsContract.CommonDataKinds.Organization.TITLE);
        phoneticOrganizationName = getString(ContactsContract.CommonDataKinds.Organization.PHONETIC_NAME);
    }

    @Override
    protected void fillMap(WritableMap map) {
        addToMap(map, Field.phoneticOrganizationName, phoneticOrganizationName);
        addToMap(map, Field.organizationName, organizationName);
        addToMap(map, Field.jobTitle, jobTitle);
        addToMap(map, Field.departmentName, departmentName);
    }
}
