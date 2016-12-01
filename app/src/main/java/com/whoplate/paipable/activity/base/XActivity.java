package com.whoplate.paipable.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.ActivityHome;
import com.whoplate.paipable.activity.ActivityLogIn;
import com.whoplate.paipable.session.XSession;
import com.whoplate.paipable.stack.XStack;

public abstract class XActivity extends AppCompatActivity {
    public boolean needSession = true;

    public IconTextView goBack;
    public TextView title;
    public TextView rigthBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        XStack.Push(this);

        setContentView(GetContentView());

        if (!(this instanceof ActivityHome)) {

            goBack = (IconTextView) findViewById(R.id.goBack);
            title = (TextView) findViewById(R.id.title);
            rigthBtn = (TextView) findViewById(R.id.right_button);

            goBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    public abstract int GetContentView();

    @Override
    protected void onStart() {
        super.onStart();

        XStack.Push(this);

        //check the session
        if (!XSession.IsValid() && needSession) {
            Log.d("tan", "onClick: go to sign in");
            startActivity(new Intent(this, ActivityLogIn.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        XStack.Push(this);

        Log.d("tan", "onResume: xstack size:" + XStack.Size());
    }
}
