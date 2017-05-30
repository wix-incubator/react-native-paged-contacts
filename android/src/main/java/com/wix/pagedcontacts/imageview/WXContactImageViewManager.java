package com.wix.pagedcontacts.imageview;

import android.support.annotation.Nullable;
import android.view.View;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.wix.pagedcontacts.contacts.ContactsProviderFactory;

/**
 * Created by sergeyi on 30/05/2017.
 */

public final class WXContactImageViewManager extends SimpleViewManager {
    public static final String REACT_CLASS = "WXContactImageView";

    private ContactsProviderFactory _contactsProviderFactory;

    public WXContactImageViewManager(ContactsProviderFactory contactsProviderFactory) {
        super();
        _contactsProviderFactory = contactsProviderFactory;
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected View createViewInstance(ThemedReactContext reactContext) {
        return new WXContactImageView(reactContext, _contactsProviderFactory);
    }

    @ReactProp(name = "pagedContactsId")
    public void setPagedContactsId(WXContactImageView view, @Nullable String src) {
        view.setPagedContactsId(src);
    }

    @ReactProp(name = "contactId")
    public void setContactId(WXContactImageView view, @Nullable String src) {
        view.setContactId(src);
    }

    @ReactProp(name = "imageType")
    public void setImageType(WXContactImageView view, @Nullable String src) {
        view.setImageType(src);
    }
}
