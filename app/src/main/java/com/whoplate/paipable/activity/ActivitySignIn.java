package com.whoplate.paipable.activity;

import android.os.Bundle;

import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;

public class ActivitySignIn extends XActivity {
    @Override
    public int GetContentView() {
        return R.layout.activity_signin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText(R.string.sign_in_everyday);
    }
}
