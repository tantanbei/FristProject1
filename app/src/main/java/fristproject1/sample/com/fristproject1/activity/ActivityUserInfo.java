package fristproject1.sample.com.fristproject1.activity;

import android.os.Bundle;
import android.widget.TextView;

import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.activity.base.XActivity;
import fristproject1.sample.com.fristproject1.db.Pref;
import fristproject1.sample.com.fristproject1.string.XString;

public class ActivityUserInfo extends XActivity {

    @Override
    public int GetContentView() {
        return R.layout.activity_mine;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView userName = (TextView) findViewById(R.id.username);
        TextView phoneNum = (TextView) findViewById(R.id.phone_number);


        userName.setText(Pref.Get(Pref.USERNAME, XString.GetString(this, R.string.app_name)));
        phoneNum.setText(Pref.Get(Pref.USERPHONE, "18117541072"));
    }
}
