package com.wix.pagedcontacts.contacts.Items;

import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;

import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.Field;
import com.wix.pagedcontacts.contacts.QueryParams;

public class Name extends ContactItem {
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

    public Name(Cursor cursor) {
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
}
