package com.wix.pagedcontacts.contacts.Items;

import android.content.ContentProviderOperation;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.Field;
import com.wix.pagedcontacts.contacts.query.QueryParams;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.react.bridge.Arguments.createMap;

public class Contact {
    public Identifier identifier;
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
    public List<PostalAddress> postalAddresses = new ArrayList<>();
    public List<InstantMessagingAddress> instantMessagingAddresses = new ArrayList<>();
    public List<UrlAddress> urlAddresses = new ArrayList<>();
    public Photo photo = new Photo();

    public Contact(String contactId) {
        this.identifier = new Identifier(contactId);
    }

    public Contact(ReadableMap contactMap) {
        fillFromContactMap(contactMap);
    }

    public WritableMap toMap(QueryParams params) {
        WritableMap map = createMap();
        identifier.fillMap(map, params);
        identity.fillMap(map, params);
        displayName.fillMap(map, params);
        nickname.fillMap(map, params);
        name.fillMap(map, params);
        organization.fillMap(map, params);
        note.fillMap(map, params);
        addStringField(params, map, Field.birthday, birthday);
        addItemsArray(map, phoneNumbers, Field.phoneNumbers, params);
        addItemsArray(map, dates, Field.dates, params);
        if (relations.size() > 0) {
            addItemsArray(map, relations, Field.relations, params);
            map.putArray("contactRelations", getWritableArray(relations, params));
        }
        addItemsArray(map, emails, Field.emailAddresses, params);
        if (postalAddresses.size() > 0) {
            map.putArray(Field.postalAddresses.getKey(), getPostalAddresses(params));
        }
        addItemsArray(map, instantMessagingAddresses, Field.instantMessageAddresses, params);
        addItemsArray(map, urlAddresses, Field.urlAddresses, params);
        if (params.fetchField(Field.imageData)) {
            addStringField(params, map, Field.imageData, photo.getImageData());
        }
        if (params.fetchField(Field.thumbnailImageData)){
            addStringField(params, map, Field.thumbnailImageData, photo.getThumbnailImageDate());
        }
        addStringField(params, map, Field.imageUri, photo.getImageUri());
        return map;
    }

    private void addItemsArray(WritableMap map, List<? extends ContactItem> items, Field field, QueryParams params) {
        if (items.size() > 0) {
            map.putArray(field.getKey(), getWritableArray(items, params));
        }
    }

    private WritableArray getPostalAddresses(QueryParams params) {
        WritableArray result = Arguments.createArray();
        for (PostalAddress postalAddress : postalAddresses) {
            result.pushMap(postalAddress.toMap(params));
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

    private void fillFromContactMap(ReadableMap contactMap) {
        this.name = new Name(contactMap);
        this.organization = new Organization(contactMap);
        this.note = new Note(contactMap);

        ReadableArray _phoneNumbers = contactMap.hasKey(Field.phoneNumbers.getKey()) ? contactMap.getArray(Field.phoneNumbers.getKey()) : new EmptyReadableArray();

        for (int i = 0; i < _phoneNumbers.size(); i++) {
            phoneNumbers.add(new PhoneNumber(_phoneNumbers.getMap(i)));
        }

        ReadableArray _emails = contactMap.hasKey(Field.emailAddresses.getKey()) ? contactMap.getArray(Field.emailAddresses.getKey()) : new EmptyReadableArray();

        for (int i = 0; i < _emails.size(); i++) {
            emails.add(new Email(_emails.getMap(i)));
        }

        ReadableArray _urlAddresses = contactMap.hasKey(Field.urlAddresses.getKey()) ? contactMap.getArray(Field.urlAddresses.getKey()): new EmptyReadableArray();

        for (int i = 0; i < _urlAddresses .size(); i++) {
            urlAddresses.add(new UrlAddress(_urlAddresses.getMap(i)));
        }

        ReadableArray _postalAddresses = contactMap.hasKey(Field.postalAddresses.getKey()) ? contactMap.getArray(Field.postalAddresses.getKey()): new EmptyReadableArray();

        for (int i = 0; i < _postalAddresses.size(); i++) {
            postalAddresses.add(new PostalAddress(_postalAddresses.getMap(i)));
        }
    }


    public ArrayList<ContentProviderOperation> createOps() {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ContentProviderOperation.Builder op = ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null);
        ops.add(op.build());


        name.addCreationOp(ops);
        organization.addCreationOp(ops);
        note.addCreationOp(ops);


        op.withYieldAllowed(true);


        for (PhoneNumber phoneNumber : phoneNumbers) {
            phoneNumber.addCreationOp(ops);
        }

        for(Email email: emails) {
            email.addCreationOp(ops);
        }

        for(UrlAddress urlAddress: urlAddresses) {
            urlAddress.addCreationOp(ops);
        }

        for (PostalAddress postalAddress: postalAddresses) {
            postalAddress.addCreationOp(ops);
        }

        return ops;

    }

    public String getContactId() {
        return identifier.contactId;
    }
}
