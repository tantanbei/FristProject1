package fristproject1.sample.com.fristproject1.activity;

import android.app.Activity;
import android.os.Bundle;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.IoniconsModule;
import com.joanzapata.iconify.fonts.TypiconsModule;

import fristproject1.sample.com.fristproject1.R;

public class ActivityAuction extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify.with(new IoniconsModule());

        setContentView(R.layout.activity_auction);

    }
}
