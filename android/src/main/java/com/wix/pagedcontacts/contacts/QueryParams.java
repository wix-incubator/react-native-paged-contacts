package com.wix.pagedcontacts.contacts;

import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.facebook.react.bridge.ReadableArray;

import java.util.HashSet;
import java.util.Set;

public class QueryParams {
    private ReadableArray keysToFetch;
    int offset;
    int size;
    private Set<String> projection;
    private Set<String> fields;

    public QueryParams(ReadableArray keysToFetch, int offset, int size) {
        fields = new HashSet<>();
        this.keysToFetch = keysToFetch;
        this.offset = offset;
        this.size = size;
    }

    String[] getProjection() {
        projection = new HashSet<>();
        for (int i = 0; i < keysToFetch.size(); i++) {
            switch (keysToFetch.getString(i)) {
                case Field.namePrefix:
                    fields.add(Field.namePrefix);
                    projection.add(ContactsContract.CommonDataKinds.StructuredName.PREFIX);
                    break;
                case Field.givenName:
                    fields.add(Field.givenName);
                    projection.add(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
                    addDisplayName();
                    break;
                case Field.middleName:
                    fields.add(Field.middleName);
                    projection.add(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME);
                    addDisplayName();
                    break;
                case Field.nickname:
                    fields.add(Field.nickname);
                    projection.add(ContactsContract.CommonDataKinds.Nickname.NAME);
                    break;
                case Field.familyName:
                    fields.add(Field.familyName);
                    projection.add(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME);
                    addDisplayName();
                    break;
                case Field.phoneticGivenName:
                    fields.add(Field.phoneticGivenName);
                    projection.add(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_GIVEN_NAME);
                    break;
                case Field.phoneticMiddleName:
                    fields.add(Field.phoneticMiddleName);
                    projection.add(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_MIDDLE_NAME);
                    break;
                case Field.phoneticFamilyName:
                    fields.add(Field.phoneticFamilyName);
                    projection.add(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_FAMILY_NAME);
                    break;
                case Field.nameSuffix:
                    fields.add(Field.nameSuffix);
                    projection.add(ContactsContract.CommonDataKinds.StructuredName.SUFFIX);
                    break;
                case Field.birthday:
                    fields.add(Field.birthday);
                    addEvents();
                    break;
                case Field.dates:
                    fields.add(Field.dates);
                    addEvents();
                    break;
                case Field.phoneNumber:
                    fields.add(Field.phoneNumber);
                    addPhoneNumber();
                    break;
                case Field.emailAddresses:
                    fields.add(Field.emailAddresses);
                    addEmail();
                    break;
                case Field.organizationName:
                    fields.add(Field.organizationName);
                    projection.add(ContactsContract.CommonDataKinds.Organization.COMPANY);
                    break;
                case Field.jobTitle:
                    fields.add(Field.jobTitle);
                    projection.add(ContactsContract.CommonDataKinds.Organization.TITLE);
                    break;
                case Field.departmentName:
                    fields.add(Field.departmentName);
                    projection.add(ContactsContract.CommonDataKinds.Organization.DEPARTMENT);
                    break;
                case Field.phoneticOrganizationName:
                    fields.add(Field.phoneticOrganizationName);
                    projection.add(ContactsContract.CommonDataKinds.Organization.PHONETIC_NAME);
                    break;
                case Field.note:
                    fields.add(Field.note);
                    projection.add(ContactsContract.CommonDataKinds.Note.NOTE);
                    break;
                case Field.relation:
                    fields.add(Field.relation);
                    addRelationship();
                    break;
                case Field.postalAddresses:
                    fields.add(Field.postalAddresses);
                    addPostalAddress();
                    break;
                case Field.instantMessageAddresses:
                    fields.add(Field.instantMessageAddresses);
                    addInstantMessaging();
                    break;
                case Field.urlAddresses:
                    fields.add(Field.urlAddresses);
                    projection.add(ContactsContract.CommonDataKinds.Website.URL);
                    projection.add(ContactsContract.CommonDataKinds.Website.TYPE);
                    projection.add(ContactsContract.CommonDataKinds.Website.LABEL);
                    break;
                case Field.imageData:
                    fields.add(Field.imageData);
                    projection.add(ContactsContract.CommonDataKinds.Contactables.PHOTO_URI);
                    break;
                case Field.thumbnailImageData:
                    fields.add(Field.thumbnailImageData);
                    projection.add(ContactsContract.CommonDataKinds.Contactables.PHOTO_THUMBNAIL_URI);
                    projection.add(ContactsContract.CommonDataKinds.Photo.PHOTO);
                    break;
                default:
                    break;
            }
        }
        projection.add(ContactsContract.Contacts.DISPLAY_NAME);
        projection.add(ContactsContract.Contacts.Data.MIMETYPE);
        projection.add(ContactsContract.Data.CONTACT_ID);
        projection.add(ContactsContract.RawContacts.SOURCE_ID);
        return projection.toArray(new String[projection.size()]);
    }

    private void addInstantMessaging() {
        projection.add(ContactsContract.CommonDataKinds.Im.DATA);
        projection.add(ContactsContract.CommonDataKinds.Im.TYPE);
        projection.add(ContactsContract.CommonDataKinds.Im.LABEL);
        projection.add(ContactsContract.CommonDataKinds.Im.PROTOCOL);
    }

    private void addRelationship() {
        projection.add(ContactsContract.CommonDataKinds.Relation.NAME);
        projection.add(ContactsContract.CommonDataKinds.Relation.TYPE);
        projection.add(ContactsContract.CommonDataKinds.Relation.LABEL);
    }

    private void addPostalAddress() {
        projection.add(ContactsContract.CommonDataKinds.StructuredPostal.TYPE);
        projection.add(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS);
        projection.add(ContactsContract.CommonDataKinds.StructuredPostal.LABEL);
        projection.add(ContactsContract.CommonDataKinds.StructuredPostal.STREET);
        projection.add(ContactsContract.CommonDataKinds.StructuredPostal.POBOX);
        projection.add(ContactsContract.CommonDataKinds.StructuredPostal.NEIGHBORHOOD);
        projection.add(ContactsContract.CommonDataKinds.StructuredPostal.CITY);
        projection.add(ContactsContract.CommonDataKinds.StructuredPostal.REGION);
        projection.add(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE);
        projection.add(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY);
    }

    private void addEvents() {
        projection.add(ContactsContract.CommonDataKinds.Event.TYPE);
        projection.add(ContactsContract.CommonDataKinds.Event.START_DATE);
        projection.add(ContactsContract.CommonDataKinds.Event.LABEL);
    }

    private void addPhoneNumber() {
        projection.add(Phone.NUMBER);
        projection.add(Phone.TYPE);
        projection.add(Phone.LABEL);
    }

    private void addEmail() {
        projection.add(Email.DATA);
        projection.add(Email.ADDRESS);
        projection.add(Email.TYPE);
        projection.add(Email.LABEL);
    }

    private void addDisplayName() {
        if (!projection.contains(ContactsContract.Profile.DISPLAY_NAME)) {
            projection.add(ContactsContract.Profile.DISPLAY_NAME);
        }
    }

    public boolean fetchField(String field) {
        return fields.contains(field);
    }
}
