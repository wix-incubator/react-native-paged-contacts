package com.wix.pagedcontacts.contacts.Items;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Contactables;
import android.support.annotation.Nullable;

import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.utils.ImageUtils;

import static android.provider.ContactsContract.CommonDataKinds.Photo.PHOTO;

public class Photo extends ContactItem {
    private Context applicationContext;
    @Nullable private String imageUri;
    @Nullable private String thumbnailImageUri;
    private byte[] imageData;

    public Photo(Context context, Cursor cursor) {
        super(cursor);
        this.applicationContext = context;
        fillFromCursor();
    }

    private void fillFromCursor() {
        imageUri = getString(Contactables.PHOTO_URI);
        thumbnailImageUri = getString(Contactables.PHOTO_THUMBNAIL_URI);
        imageData = getBlob(PHOTO);
    }

    String getImageData() {
        return getBase64Photo(imageUri);
    }

    String getThumbnailImageDate() {
        if (hasThumbnailBlob()) {
            return ImageUtils.toBase64(imageData);
        }
        return getBase64Photo(thumbnailImageUri);
    }

    private boolean hasThumbnailBlob() {
        return imageData != null && imageData.length > 0;
    }

    @Nullable
    private String getBase64Photo(String uri) {
        return uri != null ? ImageUtils.toBase64(applicationContext, uri) : null;
    }

    @Override
    protected void fillMap(WritableMap map) {

    }
}
