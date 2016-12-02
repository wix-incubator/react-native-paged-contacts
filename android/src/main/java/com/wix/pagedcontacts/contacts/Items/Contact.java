package com.wix.pagedcontacts.contacts.Items;

import android.text.TextUtils;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.Field;
import com.wix.pagedcontacts.contacts.QueryParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.react.bridge.Arguments.createMap;

public class Contact {
    public String contactId;
    public String displayName;
    public Name name;
    public String nickname;
    public List<PhoneNumber> phoneNumbers;
    public Organization organization;
    public String note;
    public String birthday;
    public List<Date> dates;
    public List<Relation> contactRelations;
    public List<Email> emails;
    public Map<String, PostalAddress> postalAddresses;
    public List<InstantMessagingAddress> instantMessagingAddresses;
    public List<UrlAddress> urlAddresses;
    public Photo photo;

    public Contact() {
        phoneNumbers = new ArrayList<>();
        dates = new ArrayList<>();
        emails = new ArrayList<>();
        postalAddresses = new HashMap<>();
        contactRelations = new ArrayList<>();
        organization = new Organization();
        name = new Name();
        instantMessagingAddresses = new ArrayList<>();
        urlAddresses = new ArrayList<>();
    }

    public WritableMap toMap(QueryParams params) {
        WritableMap map = createMap();
        map.putString("displayName", displayName);

        addStringField(params, map, Field.nickname, nickname);
        name.fillMap(map);
        organization.fillMap(map);
        addStringField(params, map, Field.note, note);
        addStringField(params, map, Field.birthday, birthday);

        if (params.fetchField(Field.phoneNumbers) && phoneNumbers.size() > 0) {
            map.putArray("phoneNumbers", getWritableArray(phoneNumbers));
        }

        if (params.fetchField(Field.dates) && dates.size() > 0) {
            map.putArray("dates", getWritableArray(dates));
        }

        if (params.fetchField(Field.relation) && contactRelations.size() > 0) {
            map.putArray("contactRelations", getWritableArray(contactRelations));
        }

        if (params.fetchField(Field.emailAddresses) && emails.size() > 0) {
            map.putArray("emailAddresses", getWritableArray(emails));
        }

        if (params.fetchField(Field.postalAddresses) && postalAddresses.size() > 0) {
            map.putArray("postalAddresses", getPostalAddresses());
        }

        if (params.fetchField(Field.instantMessageAddresses) && instantMessagingAddresses.size() > 0) {
            map.putArray("instantMessageAddresses", getWritableArray(instantMessagingAddresses));
        }

        if (params.fetchField(Field.urlAddresses) && urlAddresses.size() > 0) {
            map.putArray(Field.urlAddresses.getKey(), getWritableArray(urlAddresses));
        }

        if (params.fetchField(Field.imageData)) {
            addStringField(params, map, Field.imageData, photo.getImageData());
        }
        if (params.fetchField(Field.thumbnailImageData)) {
            addStringField(params, map, Field.thumbnailImageData, photo.getThumbnailImageDate());
        }
        return map;
    }

    private WritableArray getPostalAddresses() {
        WritableArray result = Arguments.createArray();
        for (String addressType : postalAddresses.keySet()) {
            WritableMap address = Arguments.createMap();
            address.putMap(addressType, postalAddresses.get(addressType).toMap());
            result.pushMap(address);
        }
        return result;
    }

    private WritableArray getWritableArray(List<? extends ContactItem> items) {
        WritableArray result = Arguments.createArray();
        for (ContactItem item : items) {
            result.pushMap(item.toMap());
        }
        return result;
    }

    private void addStringField(QueryParams params, WritableMap map, Field field, String value) {
        if (params.fetchField(field) && !TextUtils.isEmpty(value)) {
            map.putString(field.getKey(), value);
        }
    }

    @Override
    public String toString() {
        return displayName;
    }
}
