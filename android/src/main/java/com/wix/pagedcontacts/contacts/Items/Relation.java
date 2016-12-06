package com.wix.pagedcontacts.contacts.Items;

import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds;

import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.query.QueryParams;

public class Relation extends ContactItem {
    public String label;
    public String name;

    public Relation(Cursor cursor) {
        super(cursor);
        fillFromCursor();
    }

    private void fillFromCursor() {
        name = getString(CommonDataKinds.Relation.NAME);
        final String label = getString(CommonDataKinds.Relation.LABEL);
        final Integer type = getInt(CommonDataKinds.Relation.TYPE);
        this.label = getLabelFromType(type, label);
    }

    @Override
    protected void fillMap(WritableMap map, QueryParams params) {
        addToMap(map, "label", label);
        addToMap(map, "value", name);
    }


    private String getLabelFromType(Integer type, String name) {
        switch (type) {
            case CommonDataKinds.Relation.TYPE_CUSTOM:
                return name;
            case CommonDataKinds.Relation.TYPE_ASSISTANT:
                return "assistant";
            case CommonDataKinds.Relation.TYPE_BROTHER:
                return "brother";
            case CommonDataKinds.Relation.TYPE_CHILD:
                return "child";
            case CommonDataKinds.Relation.TYPE_DOMESTIC_PARTNER:
                return "domestic partner";
            case CommonDataKinds.Relation.TYPE_FATHER:
                return "father";
            case CommonDataKinds.Relation.TYPE_FRIEND:
                return "friend";
            case CommonDataKinds.Relation.TYPE_MANAGER:
                return "manager";
            case CommonDataKinds.Relation.TYPE_MOTHER:
                return "mother";
            case CommonDataKinds.Relation.TYPE_PARENT:
                return "parent";
            case CommonDataKinds.Relation.TYPE_PARTNER:
                return "partner";
            case CommonDataKinds.Relation.TYPE_REFERRED_BY:
                return "referred by";
            case CommonDataKinds.Relation.TYPE_RELATIVE:
                return "relative";
            case CommonDataKinds.Relation.TYPE_SISTER:
                return "sister";
            case CommonDataKinds.Relation.TYPE_SPOUSE:
                return "spouse";
            default:
                return "other";
        }
    }
}
