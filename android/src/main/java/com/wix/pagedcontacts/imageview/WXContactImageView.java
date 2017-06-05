package com.wix.pagedcontacts.imageview;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.facebook.react.uimanager.ThemedReactContext;
import com.wix.pagedcontacts.contacts.ContactsProviderFactory;
import com.wix.pagedcontacts.contacts.Field;
import com.wix.pagedcontacts.contacts.Items.Contact;
import com.wix.pagedcontacts.contacts.query.QueryParams;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sergeyi on 30/05/2017.
 */

public final class WXContactImageView extends ImageView {
    private String _pagedContactsId;
    private String _contactId;
    private String _imageType;
    private ContactsProviderFactory _contactsProviderFactory;

    WXContactImageView(ThemedReactContext context, ContactsProviderFactory contactsProviderFactory) {
        super(context);
        _contactsProviderFactory = contactsProviderFactory;
    }

    public void setPagedContactsId(@Nullable String pagedContactsId) {
        _pagedContactsId = pagedContactsId;
    }

    public void setContactId(@Nullable String contactId) {
        _contactId = contactId;
    }

    public void setImageType(@Nullable String imageType) {
        _imageType = imageType;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        Field key = _imageType != null && _imageType.equals("image") ? Field.imageData : Field.thumbnailImageData;
        QueryParams params = new QueryParams(Arrays.asList(key.getKey()), Arrays.asList(_contactId));
        List<Contact> contacts = _contactsProviderFactory.get(_pagedContactsId).getContactsJavaListWithIdentifiers(params);
        if(contacts.size() == 0) {
            return;
        }

        byte[] imageBytes = contacts.get(0).getContactPhotoBytes(_imageType);
        final Bitmap bitmap = imageBytes == null ? null : BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        setImageBitmap(bitmap);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setImageBitmap(null);
    }
}
