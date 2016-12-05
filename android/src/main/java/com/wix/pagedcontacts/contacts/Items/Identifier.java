package com.wix.pagedcontacts.contacts.Items;

import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.Field;
import com.wix.pagedcontacts.contacts.QueryParams;

public class Identifier extends ContactItem {
    public String contactId;

    public Identifier(String contactId) {
        this.contactId = contactId;
    }

    @Override
    protected void fillMap(WritableMap map, QueryParams params) {
        addToMap(map, Field.identifier.getKey(), contactId);
    }
}
