package com.armpatch.android.videojournal.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.armpatch.android.videojournal.model.Recording;

import java.io.File;
import java.util.List;

public class VideoCamera {

    public static void takeVideoAndSaveTo(Recording recording, Context activityContext) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        File outputFile = new File(activityContext.getFilesDir(), recording.getVideoFilename());
        recording.setVideoPath(outputFile.getAbsolutePath());

        Uri outputUri = FileProvider.getUriForFile(activityContext, "com.armpatch.android.videojournal.fileprovider", outputFile);

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

        // grant uri permissions
        List<ResolveInfo> cameraActivities = activityContext.getPackageManager().
                queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo activity : cameraActivities) {
            activityContext.grantUriPermission(activity.activityInfo.packageName, outputUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        // if a camera is available, start it
        if (cameraIntent.resolveActivity(activityContext.getPackageManager()) != null) {
            activityContext.startActivity(cameraIntent);
        }
    }
}
