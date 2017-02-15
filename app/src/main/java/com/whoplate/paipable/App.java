package com.whoplate.paipable;

import android.app.Application;
import android.content.res.Resources;
import android.os.Looper;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.EntypoModule;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.IoniconsModule;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.joanzapata.iconify.fonts.TypiconsModule;
import com.whoplate.paipable.http.SSLSocketFactoryEx;
import com.whoplate.paipable.http.XRandom;
import com.whoplate.paipable.thread.XThread;
import com.whoplate.paipable.util.XFabric;
import com.whoplate.paipable.util.XFile;

import javax.net.ssl.SSLSocketFactory;

import io.fabric.sdk.android.Fabric;

public class App extends Application{

    //app resources
    @Nullable
    private static Resources RESOURCES;

    static public Handler Uihandler = new Handler(Looper.getMainLooper());

    public static App INSTANCE ;

    public static String AppDbDirectory;
    private static String FileCacheDir;

    public static SSLSocketFactory SetSslSocketFactory;

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;

        try {
//            SetSslSocketFactory = new SSLSocketFactoryEx(null, XRandom.Get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        initIconify();

    }

    private void initIconify(){
        Iconify.with(new TypiconsModule());
        Iconify.with(new FontAwesomeModule());
        Iconify.with(new EntypoModule());
        Iconify.with(new MaterialModule());
        Iconify.with(new IoniconsModule());
    }

    public static Resources GetResources() {
        if (RESOURCES == null) {
            RESOURCES = App.INSTANCE.getResources();
        }

        return RESOURCES;
    }

    public static synchronized String GetFileCacheDir() {
        if (FileCacheDir == null) {
            FileCacheDir = XFile.GetExternalCachePath("/cache_files/");
        }

        XFile.EnsureDirExists(FileCacheDir);

        return FileCacheDir;
    }
}
