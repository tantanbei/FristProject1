package fristproject1.sample.com.fristproject1.activity;

import android.os.Bundle;
import android.view.View;

import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.activity.base.XActivity;

public class ActivitySignIn extends XActivity{
    @Override
    public int GetContentView() {
        return R.layout.activity_signin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText(R.string.sign_in);
        rigthBtn.setVisibility(View.VISIBLE);
        rigthBtn.setText(R.string.sign_up);

    }
}
