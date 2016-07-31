package fristproject1.sample.com.fristproject1.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;

import java.io.IOException;
import java.util.Random;

import fristproject1.sample.com.fristproject1.App;
import fristproject1.sample.com.fristproject1.Const;
import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.activity.base.XActivity;
import fristproject1.sample.com.fristproject1.http.Http;
import fristproject1.sample.com.fristproject1.networkpacket.GetCode;
import fristproject1.sample.com.fristproject1.networkpacket.OkPacket;
import fristproject1.sample.com.fristproject1.networkpacket.SignUpInPacket;
import fristproject1.sample.com.fristproject1.string.XString;
import fristproject1.sample.com.fristproject1.thread.XThread;
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
                    Toast.makeText(ActivitySignUp.this, XString.GetString(ActivitySignUp.this, R.string.warning_phone_number), Toast.LENGTH_SHORT).show();
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
                                    App.Uihandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(ActivitySignUp.this, XString.GetString(ActivitySignUp.this, R.string.warning_registered), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    App.Uihandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(ActivitySignUp.this, XString.GetString(ActivitySignUp.this, R.string.request_fails), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                App.Uihandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ActivitySignUp.this, XString.GetString(ActivitySignUp.this, R.string.send_success), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (IOException e) {
                            App.Uihandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ActivitySignUp.this, XString.GetString(ActivitySignUp.this, R.string.request_fails), Toast.LENGTH_SHORT).show();
                                }
                            });
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
                    Toast.makeText(ActivitySignUp.this, XString.GetString(ActivitySignUp.this, R.string.input_verification_code), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!code.getText().toString().equals(verificationCode)) {
                    Toast.makeText(ActivitySignUp.this, R.string.verification_error, Toast.LENGTH_SHORT).show();
                    return;
                }

                final String passwordStr = password.getText().toString();
                final String confirmStr = confirmPassword.getText().toString();

                if (confirmStr.length() < 6 || confirmStr.length() > 20 || passwordStr.length() < 6 || passwordStr.length() > 20 || !passwordStr.equals(confirmStr)) {
                    Toast.makeText(ActivitySignUp.this, R.string.password_invalid, Toast.LENGTH_SHORT).show();
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
                                    App.Uihandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(ActivitySignUp.this, XString.GetString(ActivitySignUp.this, R.string.warning_registered), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    App.Uihandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(ActivitySignUp.this, XString.GetString(ActivitySignUp.this, R.string.request_fails), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                App.Uihandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ActivitySignUp.this, XString.GetString(ActivitySignUp.this, R.string.register_success), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (IOException e) {
                            App.Uihandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ActivitySignUp.this, XString.GetString(ActivitySignUp.this, R.string.request_fails), Toast.LENGTH_SHORT).show();
                                }
                            });
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }
}
