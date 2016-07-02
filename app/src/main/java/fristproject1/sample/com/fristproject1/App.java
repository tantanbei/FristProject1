package fristproject1.sample.com.fristproject1;

import android.app.Application;
import android.os.Looper;
import android.os.Handler;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.IoniconsModule;
import com.joanzapata.iconify.fonts.TypiconsModule;

public class App extends Application{

    static public Handler Uihandler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();

        initIconify();

    }

    private void initIconify(){
        Iconify.with(new TypiconsModule());
        Iconify.with(new FontAwesomeModule());
        Iconify.with(new IoniconsModule());
    }
}
