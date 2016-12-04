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
    private String contactId;
    public DisplayName displayName;
    public Identity identity = new Identity();
    public Name name = new Name();
    public Nickname nickname = new Nickname();
    public List<PhoneNumber> phoneNumbers = new ArrayList<>();
    public Organization organization = new Organization();
    public Note note = new Note();
    public String birthday;
    public List<Date> dates = new ArrayList<>();
    public List<Relation> relations = new ArrayList<>();
    public List<Email> emails = new ArrayList<>();
    public Map<String, PostalAddress> postalAddresses = new HashMap<>();
    public List<InstantMessagingAddress> instantMessagingAddresses = new ArrayList<>();
    public List<UrlAddress> urlAddresses = new ArrayList<>();
    public Photo photo = new Photo();

    public String getId() {
        return contactId;
    }

    public Contact(String contactId) {
        this.contactId = contactId;
    }

    public WritableMap toMap(QueryParams params) {
        WritableMap map = createMap();
        map.putString("identifier", contactId);
        identity.fillMap(map, params);
        displayName.fillMap(map, params);
        nickname.fillMap(map, params);
        name.fillMap(map, params);
        organization.fillMap(map, params);
        note.fillMap(map, params);
        addStringField(params, map, Field.birthday, birthday);
        addItemsArray(map, phoneNumbers, Field.phoneNumbers, params);
        addItemsArray(map, dates, Field.dates, params);
        addItemsArray(map, relations, Field.relations, params);
        addItemsArray(map, emails, Field.emailAddresses, params);
        if (postalAddresses.size() > 0) {
            map.putArray(Field.postalAddresses.getKey(), getPostalAddresses(params));
        }
        addItemsArray(map, instantMessagingAddresses, Field.instantMessageAddresses, params);
        addItemsArray(map, urlAddresses, Field.urlAddresses, params);
        addStringField(params, map, Field.imageData, photo.getImageData());
        addStringField(params, map, Field.thumbnailImageData, photo.getThumbnailImageDate());
        return map;
    }

    private void addItemsArray(WritableMap map, List<? extends ContactItem> items, Field field, QueryParams params) {
        if (items.size() > 0) {
            map.putArray(field.getKey(), getWritableArray(items, params));
        }
    }

    private WritableArray getPostalAddresses(QueryParams params) {
        WritableArray result = Arguments.createArray();
        for (String addressType : postalAddresses.keySet()) {
            WritableMap address = Arguments.createMap();
            address.putMap(addressType, postalAddresses.get(addressType).toMap(params));
            result.pushMap(address);
        }
        return result;
    }

    private WritableArray getWritableArray(List<? extends ContactItem> items, QueryParams params) {
        WritableArray result = Arguments.createArray();
        for (ContactItem item : items) {
            result.pushMap(item.toMap(params));
        }
        return result;
    }

    private void addStringField(QueryParams params, WritableMap map, Field field, String value) {
        if (params.fetchField(field) && !TextUtils.isEmpty(value)) {
            map.putString(field.getKey(), value);
        }
    }
}
