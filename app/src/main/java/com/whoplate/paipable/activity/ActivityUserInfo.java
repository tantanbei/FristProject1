package com.whoplate.paipable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.db.Pref;
import com.whoplate.paipable.session.XSession;
import com.whoplate.paipable.string.XString;
import com.whoplate.paipable.util.Goto;


public class ActivityUserInfo extends XActivity {
    TextView userName;
    TextView phoneNum;

    @Override
    public int GetContentView() {
        return R.layout.activity_mine;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userName = (TextView) findViewById(R.id.username);
        phoneNum = (TextView) findViewById(R.id.phone_number);

        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityUserInfo.this, ActivityChangeUserInfo.class);
                intent.putExtra(ActivityChangeUserInfo.TYPE, ActivityChangeUserInfo.CHANGE);
                startActivity(intent);
            }
        });

        TextView logout = (TextView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XSession.Logout();
                Goto.Login();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        userName.setText(Pref.Get(Pref.USERNAME, XString.GetString(R.string.app_name)));
        phoneNum.setText(Pref.Get(Pref.USERPHONE, ""));
    }
}
