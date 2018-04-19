package com.wix.pagedcontacts.contacts.Items;

import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.Field;
import com.wix.pagedcontacts.contacts.query.QueryParams;

class Identifier extends ContactItem {
    String contactId;
    String lookupKey;

    Identifier(String contactId, String lookupKey) {
        this.contactId = contactId;
        this.lookupKey = lookupKey;
    }

    @Override
    protected void fillMap(WritableMap map, QueryParams params) {
        addToMap(map, Field.identifier.getKey(), contactId);
        addToMap(map, Field.lookupKey.getKey(), lookupKey);
    }
}
