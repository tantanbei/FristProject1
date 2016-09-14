package fristproject1.sample.com.fristproject1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;

import java.io.IOException;

import fristproject1.sample.com.fristproject1.App;
import fristproject1.sample.com.fristproject1.Const;
import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.activity.base.XActivity;
import fristproject1.sample.com.fristproject1.db.Pref;
import fristproject1.sample.com.fristproject1.http.Http;
import fristproject1.sample.com.fristproject1.networkpacket.OkPacket;
import fristproject1.sample.com.fristproject1.networkpacket.SignUpInPacket;
import fristproject1.sample.com.fristproject1.networkpacket.User;
import fristproject1.sample.com.fristproject1.string.XString;
import fristproject1.sample.com.fristproject1.thread.XThread;
import okhttp3.Response;

public class ActivitySignIn extends XActivity {
    EditText phone;
    EditText password;
    Button signin;

    @Override
    public int GetContentView() {
        return R.layout.activity_signin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        needSession = false;

        goBack.setVisibility(View.GONE);
        title.setText(R.string.sign_in);
        rigthBtn.setVisibility(View.VISIBLE);
        rigthBtn.setText(R.string.sign_up);

        phone = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.password);
        signin = (Button) findViewById(R.id.sign_in);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phoneNum = phone.getText().toString();
                if (phoneNum.length() != 11) {
                    Toast.makeText(ActivitySignIn.this, R.string.warning_phone_number, Toast.LENGTH_SHORT).show();
                }


                XThread.RunBackground(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response = Http.Post(ActivitySignIn.this, Const.SERVER_IP + Const.URL_SIGN_IN, new SignUpInPacket(phoneNum, password.getText().toString()));

                            OkPacket packet = LoganSquare.parse(response.body().byteStream(), OkPacket.class);
                            if (!packet.Ok) {
                                if (packet.Data.equals("0")) {
                                    App.Uihandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(ActivitySignIn.this, R.string.wrong_phone_password, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    App.Uihandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(ActivitySignIn.this, R.string.request_fails, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                App.Uihandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ActivitySignIn.this, R.string.sign_in_success, Toast.LENGTH_SHORT).show();
                                    }
                                });

                                User user = LoganSquare.parse(packet.Data, User.class);
                                Log.d("tan", "user info name:" + user.UserName + " id:" + user.UserId + " phone:" + user.UserPhone);

                                Pref.Set(Pref.USERID, user.UserId);
                                Pref.Set(Pref.USERNAME, user.UserName);
                                Pref.Set(Pref.USERPHONE, user.UserPhone);
                                Pref.Save();

                                finish();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                            App.Uihandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ActivitySignIn.this, R.string.request_fails, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });

        rigthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ActivitySignIn.this, ActivitySignUp.class));
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
