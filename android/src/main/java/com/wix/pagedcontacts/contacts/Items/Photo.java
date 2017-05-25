package com.wix.pagedcontacts.contacts.Items;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Contactables;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.facebook.react.bridge.WritableMap;
import com.wix.pagedcontacts.contacts.query.QueryParams;
import com.wix.pagedcontacts.utils.ImageUtils;

import static android.provider.ContactsContract.CommonDataKinds.Photo.PHOTO;

class Photo extends ContactItem {
    private Context applicationContext;
    @Nullable private String imageUri;
    @Nullable private String thumbnailImageUri;
    private byte[] thumbnailBytes;

    Photo() {

    }

    Photo(Context context, Cursor cursor) {
        super(cursor);
        this.applicationContext = context;
        fillFromCursor();
    }

    public byte[] getImageBytes() {
        return imageUri != null ? getBytesPhoto(imageUri) : null;
    }

    public byte[] getThumbnailBytes() {
        if (hasThumbnailBlob()) {
            return thumbnailBytes;
        }
        return thumbnailImageUri != null ? getBytesPhoto(thumbnailImageUri) : null;
    }

    private void fillFromCursor() {
        imageUri = getString(Contactables.PHOTO_URI);
        thumbnailImageUri = getString(Contactables.PHOTO_THUMBNAIL_URI);
        thumbnailBytes = getBlob(PHOTO);
    }

    String getImageBase64() {
        return imageUri != null ? getBase64Photo(imageUri) : null;
    }

    String getThumbnailImageBase64() {
        if (hasThumbnailBlob()) {
            return ImageUtils.toBase64(thumbnailBytes);
        }
        return thumbnailImageUri != null ? getBase64Photo(thumbnailImageUri) : null;
    }

    private boolean hasThumbnailBlob() {
        return thumbnailBytes != null && thumbnailBytes.length > 0;
    }

    private String getBase64Photo(@NonNull String uri) {
        byte[] bytes = getBytesPhoto(uri);
        return bytes == null ? null : ImageUtils.toBase64(bytes);
    }

    private byte[] getBytesPhoto(@NonNull String uri) {
        return ImageUtils.getBytes(applicationContext, uri);
    }

    @Override
    protected void fillMap(WritableMap map, QueryParams params) {

    }
}
