
package com.example.util;

import java.text.DecimalFormat;
import java.util.Locale;

public class TimeUtils {
    private static final String TAG = "TimeUtils";

    public static String formatDateTime1(long milliseconds) {
        StringBuilder sb = new StringBuilder();
        long mss = milliseconds / 1000;
        long hour = mss / (60 * 60);
        long minute = mss / 60 % 60;
        long second = mss % 60;
        DecimalFormat format = new DecimalFormat("00");
        if (hour > 0) {
            sb.append(format.format(hour)).append(":").append(format.format(minute)).append(":").append(format.format(second));
        } else {
            sb.append(format.format(minute)).append(":").append(format.format(second));
        }
        return sb.toString();
    }

    public static String formatDateTime2(long milliseconds) {
        long mss = milliseconds / 1000;
        long second = mss % 60;
        long minute = mss / 60 % 60;
        long hour = mss / 60 / 60;
        if (hour > 0) {
            return String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minute, second);
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d", minute, second);
        }
    }
}
