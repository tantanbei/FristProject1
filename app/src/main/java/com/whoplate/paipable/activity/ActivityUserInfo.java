package com.whoplate.paipable.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.db.Pref;
import com.whoplate.paipable.session.XSession;
import com.whoplate.paipable.string.XString;


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

        userName.setText(Pref.Get(Pref.USERNAME, XString.GetString(this, R.string.app_name)));
        phoneNum.setText(Pref.Get(Pref.USERPHONE, ""));

        TextView logout = (TextView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XSession.Logout();
                ActivityUserInfo.this.finish();
            }
        });
    }
}
