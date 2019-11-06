package com.wix.pagedcontacts.contacts.Items;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Contactables;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.query.QueryParams;
import com.wix.pagedcontacts.utils.ImageUtils;

import static android.provider.ContactsContract.CommonDataKinds.Photo.PHOTO;

class Photo extends ContactItem {
    private Context applicationContext;
    @Nullable private String imageUri;
    @Nullable private String thumbnailImageUri;
    private byte[] imageData;

    Photo() {

    }

    Photo(Context context, Cursor cursor) {
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
        return imageUri != null ? getBase64Photo(imageUri) : null;
    }

    String getThumbnailImageDate() {
        if (hasThumbnailBlob()) {
            return ImageUtils.toBase64(imageData);
        }
        return thumbnailImageUri != null ? getBase64Photo(thumbnailImageUri) : null;
    }

    private boolean hasThumbnailBlob() {
        return imageData != null && imageData.length > 0;
    }

    private String getBase64Photo(@NonNull String uri) {
        return ImageUtils.toBase64(applicationContext, uri);
    }

    @Override
    protected void fillMap(WritableMap map, QueryParams params) {

    }
}
