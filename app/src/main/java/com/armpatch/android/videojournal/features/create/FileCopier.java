package com.armpatch.android.videojournal.features.create;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

class FileCopier {


    /**
     * copies a file from one location to another
     * @param sourceFilepath string
     * @param destinationFilepath string
     */
    static void copy(String sourceFilepath, String destinationFilepath) {
        try {
            FileInputStream oldFile = new FileInputStream (sourceFilepath);
            FileOutputStream newFile = new FileOutputStream (destinationFilepath);

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
