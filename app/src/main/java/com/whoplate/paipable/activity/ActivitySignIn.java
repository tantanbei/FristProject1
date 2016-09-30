package com.whoplate.paipable.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.bluelinelabs.logansquare.LoganSquare;
import com.whoplate.paipable.Const;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.http.Http;
import com.whoplate.paipable.networkpacket.PointStatus;
import com.whoplate.paipable.thread.XThread;
import com.whoplate.paipable.toast.XToast;

import java.io.IOException;

import okhttp3.Response;

public class ActivitySignIn extends XActivity {
    TextView myPoint;
    TextView myKeepDays;

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

                    PointStatus pointStatus = LoganSquare.parse(response.body().byteStream(), PointStatus.class);

                    Log.d("tan", "pointStatus:" + pointStatus.ToJsonString());
                } catch (IOException e) {
                    e.printStackTrace();
                    XToast.Show(R.string.request_fails);
                }
            }
        });
    }
}
