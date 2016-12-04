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
    public static final String TYPE = "type";
    public static final int INIT = 0;
    public static final int CHANGE = 1;

    private int loadType;

    EditText username;

    @Override
    public int GetContentView() {
        return R.layout.activity_init_user_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        loadType = intent.getIntExtra(TYPE, 0);

        rigthBtn.setText(R.string.done);
        XView.Show(rigthBtn);

        title.setText(R.string.set_user_info);

        username = (EditText) findViewById(R.id.new_username);
        if (loadType == INIT) {
            username.setHint("默认用户名为您的手机号");
        } else {
            username.setText(Pref.Get(Pref.USERNAME, ""));
        }

        rigthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String newUsername = username.getText().toString().trim();
                if (newUsername.equals("")) {
                    if (loadType == INIT) {
                        startActivity(new Intent(ActivityChangeUserInfo.this, ActivityPurchaseIntent.class));
                    } else {
                        XToast.Show(R.string.warning_input_empty);
                    }
                    return;
                }

                if (newUsername.equals(Pref.Get(Pref.USERNAME, ""))) {
                    XToast.Show(R.string.warning_no_change);
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

                        //if sign up the user, it loadType is INIT
                        if (loadType == INIT) {
                            startActivity(new Intent(ActivityChangeUserInfo.this, ActivityPurchaseIntent.class));
                        }
                        finish();
                    }
                });
            }
        });

    }
}
