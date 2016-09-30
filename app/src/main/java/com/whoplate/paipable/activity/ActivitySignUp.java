package com.whoplate.paipable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bluelinelabs.logansquare.LoganSquare;
import com.whoplate.paipable.Const;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.db.Pref;
import com.whoplate.paipable.http.Http;
import com.whoplate.paipable.networkpacket.GetCode;
import com.whoplate.paipable.networkpacket.OkPacket;
import com.whoplate.paipable.networkpacket.SignUpInPacket;
import com.whoplate.paipable.networkpacket.User;
import com.whoplate.paipable.string.XString;
import com.whoplate.paipable.thread.XThread;
import com.whoplate.paipable.toast.XToast;

import java.io.IOException;
import java.util.Random;
import okhttp3.Response;

public class ActivitySignUp extends XActivity {
    public static final String ACTIVITY_TYPE = "ACTIVITY_TYPE";
    public static final int SIGN_UP = 0;
    public static final int RESET_PASSWORD = 1;
    public static final int MODIFY_PASSWORD = 2;

    EditText phone;
    EditText code;
    EditText password;
    EditText confirmPassword;
    Button getCode;
    Button signUp;

    String verificationCode;
    String phoneNumber;

    int activityType;

    @Override
    public int GetContentView() {
        return R.layout.activity_signup;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        activityType = intent.getIntExtra(ACTIVITY_TYPE, SIGN_UP);

        switch (activityType) {
            case SIGN_UP:
                needSession = false;

                goBack.setVisibility(View.GONE);
                rigthBtn.setVisibility(View.VISIBLE);
                rigthBtn.setText(R.string.login);

                title.setText(R.string.register);
                break;

            case RESET_PASSWORD:
                needSession = false;

                title.setText(R.string.reset_password);
                break;

            case MODIFY_PASSWORD:
                break;

            default:
                throw new RuntimeException("unknow activity type! check code!");
        }

        phone = (EditText) findViewById(R.id.phone);
        code = (EditText) findViewById(R.id.code);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
        getCode = (Button) findViewById(R.id.get_verification_code);
        signUp = (Button) findViewById(R.id.sign_up);

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

                            final Response response;
                            switch (activityType) {
                                case SIGN_UP:
                                    response = Http.Post(Const.SERVER_IP + Const.URL_GET_CODE, new GetCode(phoneNumber, false, verificationCode));
                                    break;

                                case RESET_PASSWORD:
                                case MODIFY_PASSWORD:
                                    response = Http.Post(Const.SERVER_IP + Const.URL_GET_CODE, new GetCode(phoneNumber, true, verificationCode));
                                    break;

                                default:
                                    return;
                            }


                            OkPacket packet = LoganSquare.parse(response.body().byteStream(), OkPacket.class);

                            if (!packet.Ok) {
                                switch (packet.Data) {
                                    case "0":
                                        XToast.Show(R.string.warning_registered);
                                        break;
                                    case "1":
                                        XToast.Show(R.string.warning_get_verification_code);
                                        break;
                                    case "2":
                                        XToast.Show(R.string.has_not_registered);
                                    default:
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
                startActivity(new Intent(ActivitySignUp.this, ActivityLogIn.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (activityType == SIGN_UP) {
            final Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        } else {
            super.onBackPressed();
        }

    }
}