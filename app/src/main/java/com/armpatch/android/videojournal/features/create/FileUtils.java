package com.armpatch.android.videojournal.features.create;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    public static String getPath(Context context, Uri uri){
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            Log.d("TAG", "uri scheme is content");

            String[] projection = { "_data" };
            Cursor cursor;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);

                int column_index = cursor.getColumnIndexOrThrow("_data");

                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // do nothing
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            Log.d("TAG", "uri scheme is file");
            return uri.getPath();
        }

        return null;
    }

    public static void copyFile(String sourceFile, String destinationFile) {
        try {
            FileInputStream oldFile = new FileInputStream (sourceFile);
            FileOutputStream newFile = new FileOutputStream (destinationFile);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;

            while ((len = oldFile.read(buf)) > 0) {
                newFile.write(buf, 0, len);
            }

            newFile.close();
            oldFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
