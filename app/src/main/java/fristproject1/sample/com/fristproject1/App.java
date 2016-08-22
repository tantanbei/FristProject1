package fristproject1.sample.com.fristproject1;

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
import io.fabric.sdk.android.Fabric;

public class App extends Application{

    static public Handler Uihandler = new Handler(Looper.getMainLooper());

    public static App INSTANCE ;

    public static String AppDbDirectory;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        INSTANCE = this;

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
