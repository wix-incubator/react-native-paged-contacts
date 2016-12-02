package com.wix.pagedcontacts.contacts.Items;

import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;

import com.facebook.react.bridge.WritableMap;

public class PostalAddress extends ContactItem {
    public String type;
    private String formattedAddress;
    private String poBox;
    private String street;
    private String neighborhood;
    private String city;
    private String region;
    private String postcode;
    private String country;

    public PostalAddress(Cursor cursor) {
        super(cursor);
        fillFromCursor();
    }

    private void fillFromCursor() {
        type = getType(getString(StructuredPostal.LABEL));
        formattedAddress = getString(StructuredPostal.FORMATTED_ADDRESS);
        poBox = getString(StructuredPostal.POBOX);
        street = getString(StructuredPostal.STREET);
        neighborhood = getString(StructuredPostal.NEIGHBORHOOD);
        city = getString(StructuredPostal.CITY);
        region = getString(StructuredPostal.REGION);
        postcode = getString(StructuredPostal.POSTCODE);
        country = getString(StructuredPostal.COUNTRY);
    }

    private String getType(String label) {
        Integer type = getInt(StructuredPostal.TYPE);
        if (type == null) {
            return label;
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
    protected void fillMap(WritableMap map) {
        addToMap(map, "country", country);
        addToMap(map, "formattedAddress", formattedAddress);
        addToMap(map, "postalCode", postcode);
        addToMap(map, "poBox", poBox);
        addToMap(map, "street", street);
        addToMap(map, "neighborhood", neighborhood);
        addToMap(map, "city", city);
        addToMap(map, "region", region);
        addToMap(map, "country", country);
    }
}
