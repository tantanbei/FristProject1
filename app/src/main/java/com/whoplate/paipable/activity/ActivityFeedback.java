package com.whoplate.paipable.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.whoplate.paipable.BuildConfig;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.http.Http;
import com.whoplate.paipable.string.XString;
import com.whoplate.paipable.toast.XToast;

import okhttp3.Response;

public class ActivityFeedback extends XActivity {

    private EditText content;
    private String DeviceMessage = "\nDevice: " + Build.MANUFACTURER +
            "/" + Build.MODEL + "\nAndroid Version:" + Build.VERSION.RELEASE +
            "\nApp Version:" + BuildConfig.VERSION_NAME;

    @Override
    public int GetContentView() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText("意见反馈");
        rigthBtn.setText(R.string.submit);
        rigthBtn.setVisibility(View.VISIBLE);

        content = (EditText) findViewById(R.id.content);

        rigthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String contentStr = content.getText().toString();

                if (XString.IsEmpty(contentStr)) {
                    XToast.Show(R.string.alert_feedback_content);
                    return;
                }

                Response response = Http.Post("url", "json string");
                //// TODO: 16-9-25  
            }
        });
    }
}
