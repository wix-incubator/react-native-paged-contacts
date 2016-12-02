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
    displayName("displayName", StructuredName.CONTENT_ITEM_TYPE, new String[]{ContactsContract.Contacts.DISPLAY_NAME}),
    namePrefix("namePrefix", StructuredName.CONTENT_ITEM_TYPE, new String[]{StructuredName.PREFIX}),
    givenName("givenName", StructuredName.CONTENT_ITEM_TYPE,new String[]{StructuredName.GIVEN_NAME}),
    middleName("middleName", StructuredName.CONTENT_ITEM_TYPE, new String[]{StructuredName.MIDDLE_NAME}),
    familyName("familyName", StructuredName.CONTENT_ITEM_TYPE, new String[]{StructuredName.FAMILY_NAME}),
    phoneticGivenName("phoneticGivenName", StructuredName.CONTENT_ITEM_TYPE, new String[]{StructuredName.PHONETIC_GIVEN_NAME}),
    phoneticMiddleName("phoneticMiddleName", StructuredName.CONTENT_ITEM_TYPE, new String[]{StructuredName.PHONETIC_MIDDLE_NAME}),
    phoneticFamilyName("phoneticFamilyName", StructuredName.CONTENT_ITEM_TYPE, new String[]{StructuredName.PHONETIC_FAMILY_NAME}),
    nameSuffix("nameSuffix", StructuredName.CONTENT_ITEM_TYPE, new String[]{StructuredName.SUFFIX}),
    nickname("nickname", Nickname.CONTENT_ITEM_TYPE, new String[]{Nickname.NAME}),
    phoneNumbers("phoneNumbers", Phone.CONTENT_ITEM_TYPE, new String[]{Phone.NUMBER, Phone.TYPE, Phone.LABEL}),
    organizationName("organizationName", Organization.CONTENT_ITEM_TYPE, new String[]{Organization.COMPANY}),
    phoneticOrganizationName("phoneticOrganizationName", Organization.CONTENT_ITEM_TYPE, new String[]{Organization.PHONETIC_NAME}),
    departmentName("departmentName", Organization.CONTENT_ITEM_TYPE, new String[]{Organization.DEPARTMENT}),
    jobTitle("jobTitle", Organization.CONTENT_ITEM_TYPE, new String[]{Organization.TITLE}),
    note("note", Note.CONTENT_ITEM_TYPE, new String[]{Note.NOTE}),
    birthday("birthday", Event.CONTENT_ITEM_TYPE, new String[]{Event.TYPE, Event.START_DATE, Event.LABEL}),
    dates("dates", Event.CONTENT_ITEM_TYPE, new String[]{Event.TYPE, Event.START_DATE, Event.LABEL}),
    relation("relation", Relation.CONTENT_ITEM_TYPE, new String[]{Relation.NAME, Relation.TYPE, Relation.LABEL}),
    emailAddresses("emailAddresses", Email.CONTENT_ITEM_TYPE,new String[]{Email.DATA, Email.ADDRESS, Email.TYPE, Email.LABEL}),
    postalAddresses("postalAddresses", StructuredPostal.CONTENT_ITEM_TYPE, new String[]{StructuredPostal.TYPE, StructuredPostal.FORMATTED_ADDRESS, StructuredPostal.LABEL, StructuredPostal.STREET, StructuredPostal.POBOX, StructuredPostal.NEIGHBORHOOD, StructuredPostal.CITY, StructuredPostal.REGION, StructuredPostal.POSTCODE, StructuredPostal.COUNTRY}),
    instantMessageAddresses("instantMessageAddresses", Im.CONTENT_ITEM_TYPE, new String[]{Im.DATA, Im.TYPE, Im.LABEL, Im.PROTOCOL}),
    urlAddresses("urlAddresses", Website.CONTENT_ITEM_TYPE, new String[]{Website.URL, Website.TYPE, Website.LABEL}),
    imageData("imageData", Photo.CONTENT_ITEM_TYPE, new String[]{Contactables.PHOTO_URI}),
    thumbnailImageData("thumbnailImageData", Photo.CONTENT_ITEM_TYPE, new String[]{Contactables.PHOTO_URI, Photo.PHOTO});

    private String key;
    private String contentItemType;
    private String[] projection;

    Field(String key, String contentItemType, String[] projection) {
        this.key = key;
        this.contentItemType = contentItemType;
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

    public String getContentItemType() {
        return contentItemType;
    }
}
