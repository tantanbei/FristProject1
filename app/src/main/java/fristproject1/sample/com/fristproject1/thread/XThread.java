package fristproject1.sample.com.fristproject1.thread;

import android.os.Build;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class XThread {
    //background threads...value of 17 is a best-estimate...6 max http2 requests = 12 max thread used, 5 left over for other thtings..
    private static final ScheduledThreadPoolExecutor backgroundPool = new ScheduledThreadPoolExecutor(17, ThreadFactory("Background Tasks"));

    static {
        //do not use keepalive for our pool...
        backgroundPool.setKeepAliveTime(0L, TimeUnit.SECONDS);
        if (Build.VERSION.SDK_INT >= 21) {
            backgroundPool.setRemoveOnCancelPolicy(true);
        }
    }

    public static void EnforceUIThread() {
        //gets static/app instance of realm...must be on UI thread...
        if (!IsUIThread()) {
            throw new RuntimeException("You are trying to a static Realm on a non-UI thread!!!!");
        }
    }

    public static boolean IsUIThread() {
        //gets static/app instance of realm...must be on UI thread...
        return Looper.getMainLooper().equals(Looper.myLooper());
    }

    public static void MoveToBackground() {
        Log.e("xing", "xthread switched to background");

        //CRITICAL some android devices might give only 1 core to background threads with extremely low priority if set to background priority
        //add more favorable -1 flag to background we are not locked to a background cgroup which may have a 10% cpu cap for all threads
        MoveToPriority(Process.THREAD_PRIORITY_BACKGROUND - 1);
    }

    public static void MoveToPriority(final int priority) {
        if (priority != android.os.Process.getThreadPriority(android.os.Process.myTid())) {
            android.os.Process.setThreadPriority(priority);
        }
    }

    public static Future<?> RunBackground(final Runnable r) {
        return backgroundPool.submit(r);
    }

    public static void RunBackgroundIfNotBackground(final Runnable r) {
        //already background thread...run now...
        if (!IsUIThread()) {
            r.run();
            return;
        }

        //use background pool
        backgroundPool.submit(r);
    }

    public static Future<?> RunBackground(final Runnable r, final long delay) {
        return backgroundPool.schedule(r, delay, TimeUnit.MILLISECONDS);
    }

    public static void Cancel(final Runnable r) {
        backgroundPool.remove(r);
    }

    public static Future<?> RunBackgroundFixedDelay(final Runnable r, final long delay) {
        return backgroundPool.scheduleWithFixedDelay(r, delay, delay, TimeUnit.MILLISECONDS);
    }

    public static ThreadFactory ThreadFactory(final String name) {
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread result = new Thread(runnable, name);
                //CRITICAL some android devices might give only 1 core to background threads with extremely low priority if set to background priority
                //XThread.MoveToBackground();

                return result;
            }
        };
    }
}

