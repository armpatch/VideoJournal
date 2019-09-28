package com.armpatch.android.videojournal.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ThumbnailFactory {

    public static final String TAG = "TAG_LOG";

    public static String createThumbnail(Context context, Recording recording) {
        String ImageName = recording.getImageFilename();
        String videoPath = recording.getVideoPath();

        Bitmap bitmap;

        File file = new File(videoPath);
        if (!file.exists()) {
            Log.e(TAG, "Thumbnail Factory: file does not exist");
        }

        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MINI_KIND);

        File bitmapFile = new File(context.getFilesDir(), ImageName);

        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(bitmapFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmapFile.getAbsolutePath();
    }
}
