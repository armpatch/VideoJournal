package com.armpatch.android.videojournal;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TextFormatter {

    /*
    y   = year   (yy or yyyy)
    M   = month  (MM)
    d   = day in month (dd)
    h   = hour (0-12)  (hh)
    H   = hour (0-23)  (HH)
    m   = minute in hour (mm)
    s   = seconds (ss)
    S   = milliseconds (SSS)
    z   = time zone  text        (e.g. Pacific Standard Time...)
    Z   = time zone, time offset (e.g. -0800)
    */

    public static String getSimpleDateString(Date date) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd");
        return dateFormatter.format(date);
    }

}
