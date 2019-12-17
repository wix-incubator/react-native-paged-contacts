package com.wix.pagedcontacts.contacts.Items;

import android.content.ContentProviderOperation;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.Field;
import com.wix.pagedcontacts.contacts.query.QueryParams;

import java.util.ArrayList;




//FIXME: no public
public class Name extends ContactItem implements withCreationOp{
    private String namePrefix;
    private String givenName;
    private String middleName;
    private String familyName;
    private String nameSuffix;
    private String phoneticGivenName;
    private String phoneticMiddleName;
    private String phoneticFamilyName;

    Name() {

    }

    public Name(ReadableMap contactMap) {
        super(contactMap);
        fillFromContactMap();
    }

    Name(Cursor cursor) {
        super(cursor);
        fillFromCursor();
    }

    private void fillFromCursor() {
        namePrefix = getString(StructuredName.PREFIX);
        givenName = getString(StructuredName.GIVEN_NAME);
        middleName = getString(StructuredName.MIDDLE_NAME);
        familyName = getString(StructuredName.FAMILY_NAME);
        nameSuffix = getString(StructuredName.SUFFIX);
        phoneticGivenName = getString(StructuredName.PHONETIC_GIVEN_NAME);
        phoneticMiddleName = getString(StructuredName.PHONETIC_MIDDLE_NAME);
        phoneticFamilyName = getString(StructuredName.PHONETIC_FAMILY_NAME);
    }

    protected void fillFromContactMap() {
         namePrefix = getMapString(Field.namePrefix);
         givenName = getMapString(Field.givenName);
         middleName = getMapString(Field.middleName);
         familyName = getMapString(Field.familyName);
         nameSuffix = getMapString(Field.nameSuffix);
         phoneticGivenName = getMapString(Field.phoneticGivenName);
         phoneticMiddleName = getMapString(Field.phoneticMiddleName);
         phoneticFamilyName = getMapString(Field.phoneticFamilyName);
    }

    @Override
    public void fillMap(WritableMap map, QueryParams params) {
        addField(map, params, Field.givenName, givenName);
        addField(map, params, Field.namePrefix, namePrefix);
        addField(map, params, Field.middleName, middleName);
        addField(map, params, Field.familyName, familyName);
        addField(map, params, Field.nameSuffix, nameSuffix);
        addField(map, params, Field.phoneticGivenName, phoneticGivenName);
        addField(map, params, Field.phoneticMiddleName, phoneticMiddleName);
        addField(map, params, Field.phoneticFamilyName, phoneticFamilyName);
    }

    public void addCreationOp(ArrayList<ContentProviderOperation> ops) {
        ContentProviderOperation.Builder op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                .withValue(StructuredName.GIVEN_NAME, givenName)
                .withValue(StructuredName.MIDDLE_NAME, middleName)
                .withValue(StructuredName.FAMILY_NAME, familyName)
                .withValue(StructuredName.PREFIX, namePrefix)
                .withValue(StructuredName.SUFFIX, nameSuffix)
                .withValue(StructuredName.PHONETIC_GIVEN_NAME, phoneticGivenName)
                .withValue(StructuredName.PHONETIC_FAMILY_NAME, phoneticFamilyName)
                .withValue(StructuredName.PHONETIC_MIDDLE_NAME, phoneticMiddleName);

        ops.add(op.build());
    }
}
