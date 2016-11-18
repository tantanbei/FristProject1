package com.whoplate.paipable.util;

import com.crashlytics.android.Crashlytics;
import com.whoplate.paipable.BuildConfig;

public class XDebug {
    public static void Handle(Throwable e) {
        Crashlytics.logException(e);

        if (BuildConfig.DEBUG) {
            e.printStackTrace();

            //force crash on debug builds...
            throw new RuntimeException(e);
        }
    }
}