package fristproject1.sample.com.fristproject1.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import fristproject1.sample.com.fristproject1.BuildConfig;
import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.activity.base.XActivity;
import fristproject1.sample.com.fristproject1.http.Http;
import fristproject1.sample.com.fristproject1.session.XSession;
import fristproject1.sample.com.fristproject1.string.XString;
import fristproject1.sample.com.fristproject1.toast.XToast;
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
