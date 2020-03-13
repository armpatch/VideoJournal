package com.armpatch.android.videojournal.tools;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TextFormatter {

    /*

        G	Era designator (before christ, after christ)
        y	Year (e.g. 12 or 2012). Use either yy or yyyy.
        M	Month in year. Number of M's determine length of format (e.g. MM, MMM or MMMMM)
        d	Day in month. Number of d's determine length of format (e.g. d or dd)
        h	Hour of day, 1-12 (AM / PM) (normally hh)
        H	Hour of day, 0-23 (normally HH)
        m	Minute in hour, 0-59 (normally mm)
        s	Second in minute, 0-59 (normally ss)
        S	Millisecond in second, 0-999 (normally SSS)
        E	Day in week (e.g Monday, Tuesday etc.)
        D	Day in year (1-366)
        F	Day of week in month (e.g. 1st Thursday of December)
        w	Week in year (1-53)
        W	Week in month (0-5)
        a	AM / PM marker
        k	Hour in day (1-24, unlike HH's 0-23)
        K	Hour in day, AM / PM (0-11)
        z	Time Zone
        '	Escape for text delimiter
        '	Single quote
    */

    public static String getSimpleDateString(Date date) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EE, MMM dd  yyyy");
        return dateFormatter.format(date);
    }

}
