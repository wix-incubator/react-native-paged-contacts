package com.wix.pagedcontacts.contacts.Items;

import android.content.ContentProviderOperation;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.query.QueryParams;

import java.util.ArrayList;

import androidx.annotation.Nullable;

class PostalAddress extends ContactItem implements withCreationOp {
    public String label;
    private String formattedAddress;
    private String poBox;
    private String street;
    private String neighborhood;
    private String city;
    private String region;
    private String postcode;
    private String country;

    PostalAddress(Cursor cursor) {
        super(cursor);
        fillFromCursor();
    }

    PostalAddress(ReadableMap postalAddressMap) {
        super();
        fillFromPostalAddressMap(postalAddressMap);
    }

    private void fillFromCursor() {
        Integer type = getInt(StructuredPostal.TYPE);
        label = getTypeString(type, getString(StructuredPostal.LABEL));
        formattedAddress = getString(StructuredPostal.FORMATTED_ADDRESS);
        poBox = getString(StructuredPostal.POBOX);
        street = getString(StructuredPostal.STREET);
        neighborhood = getString(StructuredPostal.NEIGHBORHOOD);
        city = getString(StructuredPostal.CITY);
        region = getString(StructuredPostal.REGION);
        postcode = getString(StructuredPostal.POSTCODE);
        country = getString(StructuredPostal.COUNTRY);
    }

    private void fillFromPostalAddressMap(ReadableMap postalAddressMap) {
        label =  postalAddressMap.getString("label");

        ReadableMap addressMap = postalAddressMap.getMap("value");

        formattedAddress = getWithDefault(addressMap, "formattedAddress");
        poBox = getWithDefault(addressMap, "poBox");
        street = getWithDefault(addressMap, "street");
        neighborhood = getWithDefault(addressMap, "neighborhood");
        city = getWithDefault(addressMap, "city");
        region = getWithDefault(addressMap, "region");
        postcode = getWithDefault(addressMap, "postalCode");
        country = getWithDefault(addressMap, "country");
    }

    @Nullable
    private String getWithDefault(ReadableMap addressMap, String key) {
        return addressMap.hasKey(key) ? addressMap.getString(key) : null;
    }

    private String getTypeString(Integer type, String label) {
        if (type == null) {
            throw new InvalidCursorTypeException();
        }
        switch (type) {
            case StructuredPostal.TYPE_HOME:
                return "home";
            case StructuredPostal.TYPE_WORK:
                return "work";
            case StructuredPostal.TYPE_OTHER:
                return "other";
            default:
                return label;
        }
    }


    private int getTypeInt(String label) {
        int postalAddressType;
        switch (label) {
            case "home":
                postalAddressType = StructuredPostal.TYPE_HOME;
                break;
            case "work":
                postalAddressType = StructuredPostal.TYPE_WORK;
                break;
            default:
                postalAddressType = StructuredPostal.TYPE_OTHER;
                break;
        }
        return postalAddressType;
    }

    @Override
    protected void fillMap(WritableMap map, QueryParams params) {
        addToMap(map, "label", label);
        WritableMap address = Arguments.createMap();
        addToMap(address, "country", country);
        addToMap(address, "formattedAddress", formattedAddress);
        addToMap(address, "postalCode", postcode);
        addToMap(address, "poBox", poBox);
        addToMap(address, "street", street);
        addToMap(address, "neighborhood", neighborhood);
        addToMap(address, "city", city);
        addToMap(address, "region", region);
        addToMap(address, "country", country);
        map.putMap("value", address);
    }





    @Override
    public void addCreationOp(ArrayList<ContentProviderOperation> ops) {

        ContentProviderOperation.Builder op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(StructuredPostal.LABEL, getTypeInt(label))
                .withValue(StructuredPostal.STREET, street)
                .withValue(StructuredPostal.CITY, city)
                .withValue(StructuredPostal.REGION, region)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE, postcode)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY, country);

        ops.add(op.build());
    }
}
