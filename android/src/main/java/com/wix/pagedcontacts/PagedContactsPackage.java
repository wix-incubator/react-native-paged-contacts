package com.wix.pagedcontacts;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.wix.pagedcontacts.contacts.permission.RequestPermissionsResultCallback;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PagedContactsPackage implements ReactPackage {

    private final RequestPermissionsResultCallback permissionsResultCallback;

    public PagedContactsPackage() {
        permissionsResultCallback = new RequestPermissionsResultCallback();
    }

    public PagedContactsPackage(RequestPermissionsResultCallback permissionsResultCallback) {
        this.permissionsResultCallback = permissionsResultCallback;
    }

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        PagedContactsModule pagedContactsModule = new PagedContactsModule(reactContext);
        permissionsResultCallback.setModule(pagedContactsModule);
        return Arrays.<NativeModule>asList(pagedContactsModule);
    }

    public List<Class<? extends JavaScriptModule>> createJSModules() {
      return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
      return Collections.emptyList();
    }
}