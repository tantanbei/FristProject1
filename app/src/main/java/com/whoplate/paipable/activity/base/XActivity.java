package com.whoplate.paipable.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;
import com.whoplate.paipable.App;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.ActivityHome;
import com.whoplate.paipable.activity.ActivityLogIn;
import com.whoplate.paipable.session.XSession;
import com.whoplate.paipable.stack.XStack;
import com.whoplate.paipable.thread.XThread;
import com.whoplate.paipable.util.Goto;
import com.whoplate.paipable.util.XFabric;

public abstract class XActivity extends AppCompatActivity {

    public IconTextView goBack;
    public TextView title;
    public TextView rigthBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init the fabric
        XThread.RunBackground(new Runnable() {
            @Override
            public void run() {
                XFabric.Load();
            }
        });

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

        XStack.Push(this);

        if (!XSession.IsValid()) {
            Goto.Login();
        }
    }

    public abstract int GetContentView();

    @Override
    protected void onStart() {
        super.onStart();

        XStack.Push(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        XStack.Push(this);

        Log.d("tan", "onResume: xstack size:" + XStack.Size());
    }
}
