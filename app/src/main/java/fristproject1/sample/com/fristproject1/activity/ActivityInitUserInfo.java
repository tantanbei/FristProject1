package fristproject1.sample.com.fristproject1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import fristproject1.sample.com.fristproject1.Const;
import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.activity.base.XActivity;
import fristproject1.sample.com.fristproject1.db.Pref;
import fristproject1.sample.com.fristproject1.http.Http;
import fristproject1.sample.com.fristproject1.networkpacket.ChangeUsername;
import fristproject1.sample.com.fristproject1.thread.XThread;
import fristproject1.sample.com.fristproject1.toast.XToast;
import okhttp3.Response;

public class ActivityInitUserInfo extends XActivity {

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
        rigthBtn.setVisibility(View.VISIBLE);

        username = (EditText) findViewById(R.id.new_username);

        rigthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String newUsername = username.getText().toString();
                if (newUsername.equals(Pref.Get(Pref.USERNAME, ""))) {
                    XToast.Show("用户名无改变");
                    return;
                }

                XThread.RunBackground(new Runnable() {
                    @Override
                    public void run() {
                        Response response = Http.Post(Const.SERVER_IP + Const.URL_CHANGE_USERNAME, new ChangeUsername(newUsername), true);
                        if (response == null) {
                            return;
                        }

                        Pref.Set(Pref.USERNAME, newUsername);
                        Pref.Save();

                        startActivity(new Intent(ActivityInitUserInfo.this, ActivityPurchaseIntent.class));
                        finish();
                    }
                });
            }
        });

    }
}
