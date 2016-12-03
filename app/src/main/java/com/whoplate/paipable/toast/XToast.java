package com.whoplate.paipable.toast;

import android.support.annotation.StringRes;
import android.widget.Toast;

import com.whoplate.paipable.App;
import com.whoplate.paipable.stack.XStack;
import com.whoplate.paipable.thread.XThread;

public class XToast {

    static private Toast currToast;

    static public void Show(final String str) {
        if (currToast != null) {
            currToast.cancel();
        }

        currToast = Toast.makeText(XStack.GetLastAliveActivity(), str, Toast.LENGTH_SHORT);

        if (XThread.IsUIThread()) {
            currToast.show();
        } else {
            App.Uihandler.post(new Runnable() {
                @Override
                public void run() {
                    currToast.show();
                }
            });
        }
    }

    static public void Show(@StringRes final int resId) {
        if (currToast != null) {
            currToast.cancel();
        }

        currToast = Toast.makeText(XStack.GetLastAliveActivity(), resId, Toast.LENGTH_SHORT);

        if (XThread.IsUIThread()) {
            currToast.show();
        } else {
            App.Uihandler.post(new Runnable() {
                @Override
                public void run() {
                    currToast.show();
                }
            });
        }
    }

    static public void ShowLong(final String str) {
        if (currToast != null) {
            currToast.cancel();
        }

        currToast = Toast.makeText(XStack.GetLastAliveActivity(), str, Toast.LENGTH_LONG);

        if (XThread.IsUIThread()) {
            currToast.show();
        } else {
            App.Uihandler.post(new Runnable() {
                @Override
                public void run() {
                    currToast.show();
                }
            });
        }
    }

    static public void ShowLong(@StringRes final int resId) {
        if (currToast != null) {
            currToast.cancel();
        }

        currToast = Toast.makeText(XStack.GetLastAliveActivity(), resId, Toast.LENGTH_LONG);

        if (XThread.IsUIThread()) {
            currToast.show();
        } else {
            App.Uihandler.post(new Runnable() {
                @Override
                public void run() {
                    currToast.show();
                }
            });
        }
    }
}
