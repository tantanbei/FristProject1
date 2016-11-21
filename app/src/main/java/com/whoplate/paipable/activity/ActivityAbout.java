package com.whoplate.paipable.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.whoplate.paipable.BuildConfig;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;

public class ActivityAbout extends XActivity {
    @Override
    public int GetContentView() {
        return R.layout.activity_about;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText("关于拍牌宝");

        TextView tv = (TextView) findViewById(R.id.version);
        tv.setText("版本信息 \nv" + BuildConfig.VERSION_NAME);
    }
}
