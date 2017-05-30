package com.wix.pagedcontacts;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.wix.pagedcontacts.contacts.ContactsProviderFactory;
import com.wix.pagedcontacts.contacts.permission.RequestPermissionsResultCallback;
import com.wix.pagedcontacts.imageview.WXContactImageViewManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

public class PagedContactsPackage implements ReactPackage {
    private final RequestPermissionsResultCallback permissionsResultCallback;
    private ContactsProviderFactory contactsProviderFactory;

    public PagedContactsPackage() {
        permissionsResultCallback = new RequestPermissionsResultCallback();
    }

    public PagedContactsPackage(RequestPermissionsResultCallback permissionsResultCallback) {
        this.permissionsResultCallback = permissionsResultCallback;
    }

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        PagedContactsModule pagedContactsModule = new PagedContactsModule(reactContext, providersFactory(reactContext));
        permissionsResultCallback.setModule(pagedContactsModule);
        return Arrays.<NativeModule>asList(pagedContactsModule);
    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
      return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        WXContactImageViewManager imageViewManager = new WXContactImageViewManager(providersFactory(reactContext));
        return Arrays.<ViewManager>asList(imageViewManager);
    }

    private ContactsProviderFactory providersFactory(ReactApplicationContext reactContext) {
        if(contactsProviderFactory == null) {
            contactsProviderFactory = new ContactsProviderFactory(reactContext);
        }
        return contactsProviderFactory;
    }
}