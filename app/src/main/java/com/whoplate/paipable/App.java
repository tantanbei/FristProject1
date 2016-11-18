package com.whoplate.paipable;

import android.app.Application;
import android.os.Looper;
import android.os.Handler;

import com.crashlytics.android.Crashlytics;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.EntypoModule;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.IoniconsModule;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.joanzapata.iconify.fonts.TypiconsModule;
import com.whoplate.paipable.http.SSLSocketFactoryEx;
import com.whoplate.paipable.http.XRandom;

import javax.net.ssl.SSLSocketFactory;

import io.fabric.sdk.android.Fabric;

public class App extends Application{

    static public Handler Uihandler = new Handler(Looper.getMainLooper());

    public static App INSTANCE ;

    public static String AppDbDirectory;

    public static SSLSocketFactory SetSslSocketFactory;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

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
}
