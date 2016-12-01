package com.whoplate.paipable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bluelinelabs.logansquare.LoganSquare;
import com.whoplate.paipable.Const;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.db.Pref;
import com.whoplate.paipable.http.Http;
import com.whoplate.paipable.networkpacket.OkPacket;
import com.whoplate.paipable.networkpacket.SignUpInPacket;
import com.whoplate.paipable.networkpacket.User;
import com.whoplate.paipable.thread.XThread;
import com.whoplate.paipable.toast.XToast;
import com.whoplate.paipable.util.XDebug;

import okhttp3.Response;

public class ActivityLogIn extends XActivity {
    EditText phone;
    EditText password;
    Button login;
    TextView forgetPassword;

    @Override
    public int GetContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        needSession = false;

        goBack.setVisibility(View.GONE);
        title.setText(R.string.login);
        rigthBtn.setVisibility(View.VISIBLE);
        rigthBtn.setText(R.string.sign_up);

        phone = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.log_in);
        forgetPassword = (TextView) findViewById(R.id.forget_password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phoneNum = phone.getText().toString();
                if (phoneNum.length() != 11) {
                    XToast.Show(R.string.warning_phone_number);
                    return;
                }

                XThread.RunBackground(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response = Http.Post(Const.URL_APN + Const.URL_LOG_IN, new SignUpInPacket(phoneNum, password.getText().toString()));

                            OkPacket packet = LoganSquare.parse(response.body().byteStream(), OkPacket.class);
                            if (!packet.Ok) {
                                if (packet.Data.equals("0")) {
                                    XToast.Show(R.string.wrong_phone_password);
                                } else {
                                    XToast.Show(R.string.request_fails);
                                }
                            } else {
                                XToast.Show(R.string.login_success);

                                User user = LoganSquare.parse(packet.Data, User.class);
                                Log.d("tan", "user info name:" + user.UserName + " id:" + user.UserId + " phone:" + user.UserPhone + " tokenid:" + user.TokenId);

                                Pref.Set(Pref.USERID, user.UserId);
                                Pref.Set(Pref.USERNAME, user.UserName);
                                Pref.Set(Pref.USERPHONE, user.UserPhone);
                                Pref.Set(Pref.TOKENID, user.TokenId);
                                Pref.Save();

                                finish();
                            }

                        } catch (Exception e) {
                            XDebug.Handle(e);

                            XToast.Show(R.string.request_fails);
                        }
                    }
                });
            }
        });

        rigthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ActivityLogIn.this, ActivitySignUp.class);
                intent.putExtra(ActivitySignUp.ACTIVITY_TYPE, ActivitySignUp.SIGN_UP);
                startActivity(intent);
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogIn.this, ActivitySignUp.class);
                intent.putExtra(ActivitySignUp.ACTIVITY_TYPE, ActivitySignUp.RESET_PASSWORD);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }
}
