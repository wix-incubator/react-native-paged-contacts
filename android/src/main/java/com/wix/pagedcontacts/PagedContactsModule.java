package com.wix.pagedcontacts;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.PermissionChecker;
import android.view.View;
import android.widget.ImageView;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.uimanager.NativeViewHierarchyManager;
import com.facebook.react.uimanager.UIBlock;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.views.image.ReactImageView;
import com.wix.pagedcontacts.contacts.ContactsProviderFactory;
import com.wix.pagedcontacts.contacts.Field;
import com.wix.pagedcontacts.contacts.Items.Contact;
import com.wix.pagedcontacts.contacts.permission.ReadContactsPermissionStatus;
import com.wix.pagedcontacts.contacts.query.QueryParams;
import com.wix.pagedcontacts.utils.Collections;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PagedContactsModule extends ReactContextBaseJavaModule {
    public static final int READ_CONTACTS_PERMISSION_REQUEST_CODE = 30156;
    private final ContactsProviderFactory contactProvider;
    private Promise requestPermissionPromise;
    private ReactApplicationContext reactContext;

    public PagedContactsModule(ReactApplicationContext context) {
        super(context);
        contactProvider = new ContactsProviderFactory(context);
        reactContext = context;
    }

    @Override
    public String getName() {
        return "ReactNativePagedContacts";
    }

    @ReactMethod
    public void getAuthorizationStatus(Promise promise) {
        promise.resolve(ReadContactsPermissionStatus.getAuthorizationStatus(getCurrentActivity()));
    }

    @ReactMethod
    public void requestAccess(String uuid, Promise promise) {
        if (ReadContactsPermissionStatus.isAuthorized(getCurrentActivity())) {
            promise.resolve(true);
            return;
        }
        ReadContactsPermissionStatus.requestAccess(getCurrentActivity());
        this.requestPermissionPromise = promise;
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        Field.exportToJs(constants);
        ReadContactsPermissionStatus.exportToJs(constants);
        return constants;
    }

    @ReactMethod
    public void setNameMatch(String uuid, String nameMatch) {
        contactProvider.get(uuid).setMatchName(nameMatch);
    }

    @ReactMethod
    public void contactsCount(String uuid, Promise promise) {
        final int count = contactProvider.get(uuid).getContactsCount();
        promise.resolve(count);
    }

    @ReactMethod
    public void getContactsWithRange(String uuid, int offset, int size, ReadableArray keysToFetch, Promise promise) {
        QueryParams params = new QueryParams(Collections.toStringList(keysToFetch), offset, size);
        WritableArray contacts = contactProvider.get(uuid).getContacts(params);
        promise.resolve(contacts);
    }

    @ReactMethod
    public void getContactsWithIdentifiers(String uuid, ReadableArray identifiers, ReadableArray keysToFetch, Promise promise) {
        QueryParams params = new QueryParams(Collections.toStringList(keysToFetch), Collections.toStringList(identifiers));
        WritableArray contacts = contactProvider.get(uuid).getContactsWithIdentifiers(params);
        promise.resolve(contacts);
    }

    @ReactMethod
    public void setImageViewWithHandle(final int viewHandle, String contactId, String imageType, String uuid) {
        Field key = imageType != null && imageType.equals("image") ? Field.imageData : Field.thumbnailImageData;
        QueryParams params = new QueryParams(Arrays.asList(key.getKey()), Arrays.asList(contactId));
        List<Contact> contacts = contactProvider.get(uuid).getContactsJavaListWithIdentifiers(params);
        if(contacts.size() == 0) {
            return;
        }

        byte[] imageBytes = contacts.get(0).getContactPhotoBytes(imageType);
        final Bitmap bitmap = imageBytes == null ? null : BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        UIManagerModule uiManager = reactContext.getNativeModule(UIManagerModule.class);
        uiManager.addUIBlock(new UIBlock() {
            @Override
            public void execute(NativeViewHierarchyManager nativeViewHierarchyManager) {
                View view = nativeViewHierarchyManager.resolveView(viewHandle);
                if(view instanceof ReactImageView) {
                    ((ImageView)view).setImageBitmap(bitmap);
                }
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (isReadContactsPermission(requestCode, permissions)) {
            if (requestPermissionPromise != null) requestPermissionPromise.resolve(grantResults[0] == PermissionChecker.PERMISSION_GRANTED);
        }
    }

    private boolean isReadContactsPermission(int requestCode, String[] permissions) {
        return requestCode == READ_CONTACTS_PERMISSION_REQUEST_CODE &&
               Manifest.permission.READ_CONTACTS.equals(permissions[0]);
    }
}