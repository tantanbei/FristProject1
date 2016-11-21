package com.whoplate.paipable.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bluelinelabs.logansquare.LoganSquare;
import com.whoplate.paipable.App;
import com.whoplate.paipable.Const;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.http.Http;
import com.whoplate.paipable.networkpacket.PointStatus;
import com.whoplate.paipable.networkpacket.SignInBack;
import com.whoplate.paipable.session.XSession;
import com.whoplate.paipable.string.XString;
import com.whoplate.paipable.thread.XThread;
import com.whoplate.paipable.toast.XToast;
import com.whoplate.paipable.util.XDebug;

import java.io.IOException;

import okhttp3.Response;

public class ActivitySignIn extends XActivity {
    int base = 5;
    int MAX_TIMES = 4;

    TextView myPoint;
    TextView myKeepDays;
    TextView signIn;

    @Override
    public int GetContentView() {
        return R.layout.activity_signin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText(R.string.sign_in_everyday);

        myPoint = (TextView) findViewById(R.id.point);
        myKeepDays = (TextView) findViewById(R.id.keep_sign_in_day);
        signIn = (TextView) findViewById(R.id.sign_in);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XThread.RunBackground(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            Response response = Http.Get(Const.SERVER_IP + Const.URL_SIGN_IN, true);
                            final SignInBack packet = LoganSquare.parse(response.body().byteStream(), SignInBack.class);

                            App.Uihandler.post(new Runnable() {
                                @Override
                                public void run() {

                                    XToast.Show(String.format(XString.GetString(R.string.sign_in_tip), getAddPoint(packet.KeepDays), getAddPoint(packet.KeepDays + 1)));
                                    signIn.setClickable(false);
                                    signIn.setText(R.string.signed_in_today);
                                    signIn.setBackgroundResource(R.color.button_unClickable);
                                    myPoint.setText(Integer.toString(packet.Point));
                                    myKeepDays.setText(Integer.toString(packet.KeepDays));
                                }
                            });
                        } catch (IOException e) {
                            XToast.Show(R.string.request_fails);
                            XDebug.Handle(e);
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshData();
    }

    private void refreshData() {

        XThread.RunBackground(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = Http.Get(Const.SERVER_IP + Const.URL_POINT_STATUS, true);
                    final PointStatus pointStatus = LoganSquare.parse(response.body().byteStream(), PointStatus.class);

                    App.Uihandler.post(new Runnable() {
                        @Override
                        public void run() {
                            signIn.setClickable(!pointStatus.IsSignInToday);
                            signIn.setText(pointStatus.IsSignInToday ? R.string.signed_in_today : R.string.sign_in);
                            signIn.setBackgroundResource(pointStatus.IsSignInToday ? R.color.button_unClickable : R.color.colorPrimary);
                            myPoint.setText(Integer.toString(pointStatus.Point));
                            myKeepDays.setText(Integer.toString(pointStatus.KeepDays));
                        }
                    });

                    Log.d("tan", "pointStatus:" + pointStatus.ToJsonString());
                } catch (IOException e) {
                    XDebug.Handle(e);
                    XToast.Show(R.string.request_fails);
                }
            }
        });
    }

    private int getAddPoint(int keepSignIn) {
        if (keepSignIn < MAX_TIMES) {
            return base * keepSignIn;
        } else {
            return base * MAX_TIMES;
        }
    }
}
