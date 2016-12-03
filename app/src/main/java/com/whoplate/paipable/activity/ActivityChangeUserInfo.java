package com.whoplate.paipable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.whoplate.paipable.Const;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.db.Pref;
import com.whoplate.paipable.http.Http;
import com.whoplate.paipable.networkpacket.ChangeUsername;
import com.whoplate.paipable.thread.XThread;
import com.whoplate.paipable.toast.XToast;
import com.whoplate.paipable.ui.XView;

import okhttp3.Response;

public class ActivityChangeUserInfo extends XActivity {

    EditText username;

    @Override
    public int GetContentView() {
        return R.layout.activity_init_user_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText("设置用户信息");
        rigthBtn.setText("下一步");
        XView.Show(rigthBtn);

        username = (EditText) findViewById(R.id.new_username);

        rigthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String newUsername = username.getText().toString().trim();
                if (newUsername.equals(Pref.Get(Pref.USERNAME, ""))) {
                    XToast.Show("用户名无改变");
                    return;
                }

                XThread.RunBackground(new Runnable() {
                    @Override
                    public void run() {

                        if (!newUsername.equals("")) {
                            Response response = Http.Post(Const.URL_API + Const.URL_CHANGE_USERNAME, new ChangeUsername(newUsername));
                            if (response == null) {
                                return;
                            }

                            Pref.Set(Pref.USERNAME, newUsername);
                            Pref.Save();
                        }

                        startActivity(new Intent(ActivityChangeUserInfo.this, ActivityPurchaseIntent.class));
                        finish();
                    }
                });
            }
        });

    }
}
