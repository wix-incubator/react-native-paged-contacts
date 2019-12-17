package com.wix.pagedcontacts.contacts.Items;

import android.content.ContentProviderOperation;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.Field;
import com.wix.pagedcontacts.contacts.query.QueryParams;

import java.util.ArrayList;

class Organization extends ContactItem implements withCreationOp {
    private String organizationName;
    private String departmentName;
    private String jobTitle;
    private String phoneticOrganizationName;

    Organization() {
        super();
    }

    Organization(ReadableMap contactMap) {
        super(contactMap);
        fillFromContactMap();
    }

    Organization(Cursor cursor) {
        super(cursor);
        fillFromCursor();
    }

    protected void fillFromContactMap() {
        organizationName = getMapString(Field.organizationName);
        departmentName = getMapString(Field.departmentName);
        jobTitle = getMapString(Field.jobTitle);
        phoneticOrganizationName = getMapString(Field.phoneticOrganizationName);
    }

    private void fillFromCursor() {
        organizationName = getString(ContactsContract.CommonDataKinds.Organization.COMPANY);
        departmentName = getString(ContactsContract.CommonDataKinds.Organization.DEPARTMENT);
        jobTitle = getString(ContactsContract.CommonDataKinds.Organization.TITLE);
        phoneticOrganizationName = getString(ContactsContract.CommonDataKinds.Organization.PHONETIC_NAME);
    }

    @Override
    protected void fillMap(WritableMap map, QueryParams params) {
        addField(map, params, Field.phoneticOrganizationName, phoneticOrganizationName);
        addField(map, params, Field.organizationName, organizationName);
        addField(map, params, Field.jobTitle, jobTitle);
        addField(map, params, Field.departmentName, departmentName);
    }

    @Override
    public void addCreationOp(ArrayList<ContentProviderOperation> ops) {
        ContentProviderOperation.Builder op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, organizationName)
                .withValue(ContactsContract.CommonDataKinds.Organization.DEPARTMENT, departmentName)
                .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
                .withValue(ContactsContract.CommonDataKinds.Organization.PHONETIC_NAME, phoneticOrganizationName);

        ops.add(op.build());
    }
}
