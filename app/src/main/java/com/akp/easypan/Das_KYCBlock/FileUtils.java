package com.akp.easypan.Das_KYCBlock;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    public static String getPathFromURI(Context context, Uri uri) {
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    filePath = cursor.getString(columnIndex);
                }
            } catch (Exception e) {
                Log.e("FileUtils", "Error getting file path from URI", e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        return filePath;
    }

    public static String convertContentUriToBase64(Context context, Uri contentUri) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(contentUri);
            if (inputStream != null) {
                byte[] bytes = readBytesFromInputStream(inputStream);
                if (bytes != null) {
                    return Base64.encodeToString(bytes, Base64.DEFAULT);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Failed to convert content URI to Base64
    }

    private static byte[] readBytesFromInputStream(InputStream inputStream) {
        try (BufferedInputStream bis = new BufferedInputStream(inputStream);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Error occurred while reading input stream
    }
}