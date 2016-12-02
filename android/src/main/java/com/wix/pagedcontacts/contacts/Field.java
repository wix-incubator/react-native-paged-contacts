package com.wix.pagedcontacts.contacts;

import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Contactables;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;

import java.util.Map;

public enum Field {
    displayName("displayName", new String[]{ContactsContract.Contacts.DISPLAY_NAME}),
    namePrefix("namePrefix", new String[]{StructuredName.PREFIX}),
    givenName("givenName", new String[]{StructuredName.GIVEN_NAME}),
    middleName("middleName", new String[]{StructuredName.MIDDLE_NAME}),
    familyName("familyName", new String[]{StructuredName.FAMILY_NAME}),
    nickname("nickname", new String[]{Nickname.NAME}),
    phoneticGivenName("phoneticGivenName", new String[]{StructuredName.PHONETIC_GIVEN_NAME}),
    phoneticMiddleName("phoneticMiddleName", new String[]{StructuredName.PHONETIC_MIDDLE_NAME}),
    phoneticFamilyName("phoneticFamilyName", new String[]{StructuredName.PHONETIC_FAMILY_NAME}),
    nameSuffix("nameSuffix", new String[]{StructuredName.SUFFIX}),
    phoneNumber("phoneNumber", new String[]{Phone.NUMBER, Phone.TYPE, Phone.LABEL}),
    organizationName("organizationName", new String[]{Organization.COMPANY}),
    phoneticOrganizationName("phoneticOrganizationName", new String[]{Organization.PHONETIC_NAME}),
    departmentName("departmentName", new String[]{Organization.DEPARTMENT}),
    jobTitle("jobTitle", new String[]{Organization.TITLE}),
    note("note", new String[]{Note.NOTE}),
    birthday("birthday", new String[]{Event.TYPE, Event.START_DATE, Event.LABEL}),
    dates("dates", new String[]{Event.TYPE, Event.START_DATE, Event.LABEL}),
    relation("relation", new String[]{Relation.NAME, Relation.TYPE, Relation.LABEL}),
    emailAddresses("emailAddresses", new String[]{Email.DATA, Email.ADDRESS, Email.TYPE, Email.LABEL}),
    postalAddresses("postalAddresses", new String[]{StructuredPostal.TYPE, StructuredPostal.FORMATTED_ADDRESS, StructuredPostal.LABEL, StructuredPostal.STREET, StructuredPostal.POBOX, StructuredPostal.NEIGHBORHOOD, StructuredPostal.CITY, StructuredPostal.REGION, StructuredPostal.POSTCODE, StructuredPostal.COUNTRY}),
    instantMessageAddresses("instantMessageAddresses", new String[]{Im.DATA, Im.TYPE, Im.LABEL, Im.PROTOCOL}),
    urlAddresses("urlAddresses", new String[]{Website.URL, Website.TYPE, Website.LABEL}),
    imageData("imageData", new String[]{Contactables.PHOTO_URI}),
    thumbnailImageData("thumbnailImageData", new String[]{Contactables.PHOTO_URI, Photo.PHOTO});

    private String key;
    private String[] projection;

    Field(String key, String[] projection) {
        this.key = key;
        this.projection = projection;
    }

    public String getKey() {
        return key;
    }

    public String[] getProjection() {
        return projection;
    }

    public static void exportToJs(Map<String, Object> constants ) {
        for (Field field : values()) {
            constants.put(field.key, field.key);
        }
    }

    public static Field fromKey(String key) {
        for (Field field : values()) {
            if (field.key.equals(key)) {
                return field;
            }
        }
        throw new RuntimeException("Unsupported contact key: " + key);
    }
}
