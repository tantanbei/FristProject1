package fristproject1.sample.com.fristproject1.stack;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import fristproject1.sample.com.fristproject1.activity.ActivityAuction;
import fristproject1.sample.com.fristproject1.activity.base.XActivity;

public class XStack {

    //max history size...
    public static final int MAX_HISTORY_SIZE;

    private static ArrayList<WeakReference<Activity>> history = new ArrayList<>();

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

    public static int Size(){
        return history.size();
    }

    private static void gc() {
        if (history == null) {
            history = new ArrayList<>();
            return;
        }

        for (int i = 0; i < history.size(); i++) {
            Activity a = history.get(i).get();
            if (a == null || a.isDestroyed()) {
                history.remove(0);
                i--;
            }
        }
    }

    public static void Push(Activity a) {

        gc();

        Activity lastA = GetLastAliveActivity();

        if (a != null && !a.isDestroyed()) {

            //do not push duplicate item
            if (lastA != a) {
                history.add(new WeakReference<Activity>(a));
            }
        }
    }

    public static Activity GetLastAliveActivity() {
        gc();

        if (history == null || history.isEmpty()) {
            Log.e("tan", "no alive avtivity on stack");
            return null;
        }

        return history.get(history.size() - 1).get();
    }

}
