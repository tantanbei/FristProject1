package fristproject1.sample.com.fristproject1.toast;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

import fristproject1.sample.com.fristproject1.App;
import fristproject1.sample.com.fristproject1.stack.XStack;

public class XToast {

    static public void Show(final String str) {
        App.Uihandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(XStack.GetLastAliveActivity(), str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    static public void Show(@StringRes final int resId) {
        App.Uihandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(XStack.GetLastAliveActivity(), resId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    static public void ShowLong(final String str) {
        App.Uihandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(XStack.GetLastAliveActivity(), str, Toast.LENGTH_LONG).show();
            }
        });
    }

    static public void ShowLong(@StringRes final int resId) {
        App.Uihandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(XStack.GetLastAliveActivity(), resId, Toast.LENGTH_LONG).show();
            }
        });
    }
}
