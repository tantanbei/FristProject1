package com.whoplate.paipable.util;

import com.crashlytics.android.Crashlytics;
import com.whoplate.paipable.BuildConfig;
import com.whoplate.paipable.stack.XStack;

public class XDebug {
    public static void Handle(Throwable e) {

        XFabric.Load();

        //add some debug info...
        XFabric.SetLastActivity();
        XFabric.Set(XFabric.KEY_STACK_SIZE, XStack.Size());

        Crashlytics.logException(e);

        if (BuildConfig.DEBUG) {
            e.printStackTrace();

            //force crash on debug builds...
            throw new RuntimeException(e);
        }
    }
}