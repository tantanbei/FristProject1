package com.whoplate.paipable.ui;

import android.util.DisplayMetrics;

import com.whoplate.paipable.App;

public class XUI {

    public static int DpToPx(float dpValue) {
        final float scale = App.GetResources().getDisplayMetrics().density;
        return Math.round(dpValue * scale + 0.5f);
    }

    public static float DpToPxFloat(float dpValue) {
        final float scale = App.GetResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }

    public static int PxToDp(float px) {
        DisplayMetrics metrics = App.GetResources().getDisplayMetrics();

        return Math.round(px / (metrics.densityDpi / 160f));
    }

    public static int SpToPx(float spValue) {
        final float scale = App.GetResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    public static int SpToPxFloat(float spValue) {
        final float scale = App.GetResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }
}
