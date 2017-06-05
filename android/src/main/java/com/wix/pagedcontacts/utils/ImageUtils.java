package com.wix.pagedcontacts.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageUtils {
    public static String toBase64(byte[] blob) {
        return Base64.encodeToString(blob, Base64.DEFAULT);
    }

    public static byte[] getBytes(Context context, @NonNull String imageUri) {
        try {
            Uri photo = Uri.parse(imageUri);
            Bitmap bitmap = getBitmap(context, photo);
            return getBytes(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        return os.toByteArray();
    }

    private static Bitmap getBitmap(Context context, Uri photo) throws FileNotFoundException {
        InputStream is = context.getContentResolver().openInputStream(photo);
        BufferedInputStream in = new BufferedInputStream(is);
        return BitmapFactory.decodeStream(in);
    }
}
