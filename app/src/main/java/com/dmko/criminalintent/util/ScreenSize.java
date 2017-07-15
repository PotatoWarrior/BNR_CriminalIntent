package com.dmko.criminalintent.util;

import android.app.Activity;
import android.util.DisplayMetrics;

public class ScreenSize {
    private static boolean isBig;
    private static boolean isCalculated = false;

    public static boolean isScreenBig(Activity activity) {
        if (!isCalculated) {
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            float yInches = metrics.heightPixels / metrics.ydpi;
            float xInches = metrics.widthPixels / metrics.xdpi;
            double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
            if (diagonalInches >= 6.5) {
                isBig = true;
            } else {
                isBig = false;
            }
            isCalculated = true;
        }
        return isBig;
    }
}
