package com.whoplate.paipable.util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.whoplate.paipable.App;
import com.whoplate.paipable.fragment.base.XFragment;
import com.whoplate.paipable.stack.XStack;

import io.fabric.sdk.android.Fabric;

public class XFabric {
    static public final String KEY_ACTIVITY = "ACTIVITY";
    static public final String KEY_FRAGMENT = "FRAGMENT";

    static public final String KEY_LAST_ACTIVITY = "LAST_ACTIVITY";
    static public final String KEY_LAST_DB_OPEN = "LAST_DB_OPEN";
    static public final String KEY_STACK_SIZE = "STACK_SIZE";
    static public final String KEY_ACTIVE_REALMS = "ACTIVE_REALMS";
    static public final String KEY_COUNT_ACTIVITY = "COUNT_ACTIVITY_CREATE";
    static public final String KEY_COUNT_FRAGMENT = "COUNT_FRAGMENT_CREATE";

    static public final String KEY_LAST_API_IP = "LAST_API_IP";

    //this is very slow...so load in background thread..
    public static synchronized void Load() {
        //crash report
        if (!Fabric.isInitialized()) {
            Log.d("xing", "fabric");
            Fabric.with(App.INSTANCE, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().build()).build());
            Log.d("xing", "fabric end");
        }
    }

    //log last db open to fabric...
    public static void SetLastDb(final String db) {
        XFabric.Load();
        Crashlytics.setString(KEY_LAST_DB_OPEN, db);
    }

    public static void SetLastActivity() {
        XFabric.Load();

        final Activity a = XStack.GetLastAliveActivity();
        if (a != null) {
            Crashlytics.setString(KEY_LAST_ACTIVITY, a.getClass().getName());
        }
    }

    public static void SetActivity(@NonNull final Activity a) {
        XFabric.Load();
        Crashlytics.setString(KEY_ACTIVITY, a.getClass().getName());
    }

    public static void SetFragment(@NonNull final XFragment f) {
        XFabric.Load();
        Crashlytics.setString(KEY_FRAGMENT, f.getClass().getName());
    }

    public static void Remove(@Nullable final String k) {
        //crashanalytics crash with null values...
        if (k == null) {
            return;
        }

        XFabric.Load();
        Crashlytics.setString(k, "");
    }

    public static void Set(@Nullable final String k, @Nullable final String v) {
        //crashanalytics crash with null values...
        if (k == null || v == null) {
            return;
        }

        XFabric.Load();
        Crashlytics.setString(k, v);
    }

    public static void Set(final String k, final int v) {
        XFabric.Load();
        Crashlytics.setInt(k, v);
    }

    public static void Set(final String k, final long v) {
        XFabric.Load();
        Crashlytics.setLong(k, v);
    }

    public static void SetEmail(@Nullable final String email) {
        //crashanalytics crash with null values...
        if (email == null) {
            return;
        }

        XFabric.Load();
        Crashlytics.setUserEmail(email);
    }

    public static void SetId(@Nullable final String id) {
        //crashanalytics crash with null values...
        if (id == null) {
            return;
        }

        XFabric.Load();
        Crashlytics.setUserIdentifier(id);
    }

    public static void SetName(@Nullable final String name) {
        //crashanalytics crash with null values...
        if (name == null) {
            return;
        }

        XFabric.Load();
        Crashlytics.setUserName(name);
    }
}
