package com.whoplate.paipable.toast;

import android.support.annotation.StringRes;
import android.widget.Toast;

import com.whoplate.paipable.App;
import com.whoplate.paipable.stack.XStack;
import com.whoplate.paipable.thread.XThread;

public class XToast {

    static public void Show(final String str) {
        if (XThread.IsUIThread()){
            Toast.makeText(XStack.GetLastAliveActivity(), str, Toast.LENGTH_SHORT).show();
        }else {
            App.Uihandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(XStack.GetLastAliveActivity(), str, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    static public void Show(@StringRes final int resId) {
        if (XThread.IsUIThread()){
            Toast.makeText(XStack.GetLastAliveActivity(), resId, Toast.LENGTH_SHORT).show();
        }else {
            App.Uihandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(XStack.GetLastAliveActivity(), resId, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    static public void ShowLong(final String str) {
        if (XThread.IsUIThread()){
            Toast.makeText(XStack.GetLastAliveActivity(), str, Toast.LENGTH_LONG).show();
        }else {
            App.Uihandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(XStack.GetLastAliveActivity(), str, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    static public void ShowLong(@StringRes final int resId) {
        if (XThread.IsUIThread()){
            Toast.makeText(XStack.GetLastAliveActivity(), resId, Toast.LENGTH_LONG).show();
        }else {
            App.Uihandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(XStack.GetLastAliveActivity(), resId, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
