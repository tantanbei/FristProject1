package fristproject1.sample.com.fristproject1.stack;

import android.os.Build;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import fristproject1.sample.com.fristproject1.activity.base.XActivity;

public class XStack {

    //max history size...
    public static final int MAX_HISTORY_SIZE;

    private static ArrayList<WeakReference<XActivity>> history = new ArrayList<>();

    static {
        //reduce stacksize for < 4.4 devices which are extremely buggy...
        if (Build.VERSION.SDK_INT < 19) {
            //reduce memory usage for 4.3 and below...
            MAX_HISTORY_SIZE = 4;
        } else if (Build.VERSION.SDK_INT == 19) {
            MAX_HISTORY_SIZE = 5;
        } else if (Build.VERSION.SDK_INT < 24) {
            MAX_HISTORY_SIZE = 6;
        } else {
            //android N
            MAX_HISTORY_SIZE = 12;
        }
    }

}
