package com.whoplate.paipable.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.whoplate.paipable.App;
import com.whoplate.paipable.BuildConfig;
import com.whoplate.paipable.Const;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.http.Http;
import com.whoplate.paipable.networkpacket.FeedbackPacket;
import com.whoplate.paipable.string.XString;
import com.whoplate.paipable.thread.XThread;
import com.whoplate.paipable.toast.XToast;
import com.whoplate.paipable.ui.XView;

import okhttp3.Response;

public class ActivityFeedback extends XActivity {

    private EditText content;
    static final private String DeviceMessage = "{ Device: " + Build.MANUFACTURER +
            "/" + Build.MODEL + " && Android Version:" + Build.VERSION.RELEASE +
            " && App Version:" + BuildConfig.VERSION_NAME+" }";

    @Override
    public int GetContentView() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText(R.string.feedback);
        rigthBtn.setText(R.string.submit);
        XView.Show(rigthBtn);

        content = (EditText) findViewById(R.id.content);

        rigthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String contentStr = content.getText().toString();

                if (XString.IsEmpty(contentStr)) {
                    XToast.Show(R.string.alert_feedback_content);
                    return;
                }

                final FeedbackPacket packet = new FeedbackPacket(contentStr, DeviceMessage);

                XThread.RunBackground(new Runnable() {
                    @Override
                    public void run() {
                        Response response = Http.Post(Const.URL_API + Const.URL_FEEDBACK, packet);

                        if (response != null) {
                            XToast.Show(R.string.send_success);

                            App.Uihandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}
