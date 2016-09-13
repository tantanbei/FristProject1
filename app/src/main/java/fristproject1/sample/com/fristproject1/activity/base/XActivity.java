package fristproject1.sample.com.fristproject1.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;

import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.activity.ActivitySignIn;
import fristproject1.sample.com.fristproject1.activity.ActivitySignUp;
import fristproject1.sample.com.fristproject1.session.XSession;

public abstract class XActivity extends Activity {
    public boolean needSession = true;

    public IconTextView goBack;
    public TextView title;
    public TextView rigthBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(GetContentView());

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

    public abstract int GetContentView();

    @Override
    protected void onStart() {
        super.onStart();

        //check the session
        if (!XSession.IsValid() && needSession) {
            Log.d("tan", "onClick: go to sign in");
            startActivity(new Intent(this, ActivitySignIn.class));
            finish();
            return;
        }
    }
}
