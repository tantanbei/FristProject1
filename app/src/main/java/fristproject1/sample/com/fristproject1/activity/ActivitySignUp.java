package fristproject1.sample.com.fristproject1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bluelinelabs.logansquare.LoganSquare;

import java.io.IOException;
import java.util.Random;

import fristproject1.sample.com.fristproject1.Const;
import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.activity.base.XActivity;
import fristproject1.sample.com.fristproject1.db.Pref;
import fristproject1.sample.com.fristproject1.http.Http;
import fristproject1.sample.com.fristproject1.networkpacket.GetCode;
import fristproject1.sample.com.fristproject1.networkpacket.OkPacket;
import fristproject1.sample.com.fristproject1.networkpacket.SignUpInPacket;
import fristproject1.sample.com.fristproject1.networkpacket.User;
import fristproject1.sample.com.fristproject1.string.XString;
import fristproject1.sample.com.fristproject1.thread.XThread;
import fristproject1.sample.com.fristproject1.toast.XToast;
import okhttp3.Response;

public class ActivitySignUp extends XActivity {

    EditText phone;
    EditText code;
    EditText password;
    EditText confirmPassword;
    Button getCode;
    Button signUp;

    String verificationCode;
    String phoneNumber;

    @Override
    public int GetContentView() {
        return R.layout.activity_signup;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        needSession = false;

        goBack.setVisibility(View.GONE);
        rigthBtn.setVisibility(View.VISIBLE);
        rigthBtn.setText(R.string.sign_in);

        phone = (EditText) findViewById(R.id.phone);
        code = (EditText) findViewById(R.id.code);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
        getCode = (Button) findViewById(R.id.get_verification_code);
        signUp = (Button) findViewById(R.id.sign_up);

        title.setText(R.string.register);

        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = phone.getText().toString();

                if (XString.IsEmpty(phoneNumber) || phoneNumber.length() != 11) {
                    XToast.Show(R.string.warning_phone_number);
                    return;
                }

                Random random = new Random();
                verificationCode = "";
                for (int i = 0; i < 6; i++) {
                    int n = random.nextInt(10);
                    verificationCode += n;
                }

                XThread.RunBackground(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("tan", "get code phone:" + phoneNumber + " code:" + verificationCode);
                            final Response response = Http.Post(Const.SERVER_IP + Const.URL_GET_CODE, new GetCode(phoneNumber, verificationCode));

                            OkPacket packet = LoganSquare.parse(response.body().byteStream(), OkPacket.class);

                            if (!packet.Ok) {
                                if (packet.Data.equals("0")) {
                                    XToast.Show(R.string.warning_registered);
                                } else {
                                    XToast.Show(R.string.request_fails);
                                }
                            } else {
                                XToast.Show(R.string.send_success);
                            }
                        } catch (IOException e) {
                            XToast.Show(R.string.request_fails);
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (XString.IsEmpty(code.getText().toString())) {
                    XToast.Show(R.string.input_verification_code);
                    return;
                }

                if (!code.getText().toString().equals(verificationCode)) {
                    XToast.Show(R.string.verification_error);
                    return;
                }

                final String passwordStr = password.getText().toString();
                final String confirmStr = confirmPassword.getText().toString();

                if (confirmStr.length() < 6 || confirmStr.length() > 20 || passwordStr.length() < 6 || passwordStr.length() > 20 || !passwordStr.equals(confirmStr)) {
                    XToast.Show(R.string.password_invalid);
                    return;
                }


                XThread.RunBackground(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response = Http.Post(Const.SERVER_IP + Const.URL_SING_UP, new SignUpInPacket(phoneNumber, passwordStr));
                            OkPacket packet = LoganSquare.parse(response.body().byteStream(), OkPacket.class);

                            if (!packet.Ok) {
                                if (packet.Data.equals("0")) {
                                    XToast.Show(R.string.warning_registered);
                                } else {
                                    XToast.Show(R.string.request_fails);
                                }
                            } else {
                                XToast.Show(R.string.register_success);

                                User user = LoganSquare.parse(packet.Data, User.class);
                                Pref.Set(Pref.USERID, user.UserId);
                                Pref.Set(Pref.USERNAME, user.UserName);
                                Pref.Set(Pref.USERPHONE, user.UserPhone);
                                Pref.Save();
                                Log.d("tan", "sign up user:" + user.UserId);

                                startActivity(new Intent(ActivitySignUp.this, ActivityInitUserInfo.class));
                                finish();
                            }
                        } catch (IOException e) {
                            XToast.Show(R.string.request_fails);
                            e.printStackTrace();
                        }

                    }
                });
            }
        });

        rigthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ActivitySignUp.this, ActivitySignIn.class));
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
