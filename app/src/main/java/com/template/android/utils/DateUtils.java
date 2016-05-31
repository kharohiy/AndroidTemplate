package com.template.android.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    private static DateUtils mInstance;

    private SimpleDateFormat mServerDateFormat;
    private SimpleDateFormat mMissionsListTimeFormat;
    private SimpleDateFormat mMissionsListDateFormat;
    private SimpleDateFormat mHeaderDateFormat;

    private DateUtils() {
        mServerDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        mServerDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        mMissionsListTimeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        mMissionsListTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        mMissionsListDateFormat = new SimpleDateFormat("d MMM - EEE", Locale.getDefault());
        mMissionsListDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        mHeaderDateFormat = new SimpleDateFormat("dd MMM yyyy\nHH:mm", Locale.getDefault());
    }

    public static DateUtils getInstance() {
        synchronized (DateUtils.class) {
            if (mInstance == null) {
                mInstance = new DateUtils();
            }

            return mInstance;
        }
    }

    public long toUnixTime(String value) {
        try {
            Date date = mServerDateFormat.parse(value);
            return date.getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    public String toServerTime(long value) {
        return mServerDateFormat.format(new Date(value));
    }

    public String toMissionListDate(long startDate) {
        return mMissionsListDateFormat.format(new Date(startDate));
    }

    public String toTime(long unixTime) {
        return mMissionsListTimeFormat.format(new Date(unixTime));
    }

    public String toMissionListTime(long startDate, long endDate) {
        return mMissionsListTimeFormat.format(new Date(startDate)) + " - " + mMissionsListTimeFormat.format(new Date(endDate));
    }

    public String toHeaderDate(long unixTime) {
        return mHeaderDateFormat.format(new Date(unixTime));
    }
}
