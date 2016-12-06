package com.wix.pagedcontacts.contacts.Items;

import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.query.QueryParams;

class PostalAddress extends ContactItem {
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

    private void fillFromCursor() {
        Integer type = getInt(StructuredPostal.TYPE);
        label = getType(type, getString(StructuredPostal.LABEL));
        formattedAddress = getString(StructuredPostal.FORMATTED_ADDRESS);
        poBox = getString(StructuredPostal.POBOX);
        street = getString(StructuredPostal.STREET);
        neighborhood = getString(StructuredPostal.NEIGHBORHOOD);
        city = getString(StructuredPostal.CITY);
        region = getString(StructuredPostal.REGION);
        postcode = getString(StructuredPostal.POSTCODE);
        country = getString(StructuredPostal.COUNTRY);
    }

    private String getType(Integer type, String label) {
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
}
