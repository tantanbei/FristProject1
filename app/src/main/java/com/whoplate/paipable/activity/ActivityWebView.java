package com.whoplate.paipable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.bluelinelabs.logansquare.LoganSquare;
import com.whoplate.paipable.App;
import com.whoplate.paipable.Const;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.http.Http;
import com.whoplate.paipable.networkpacket.OkPacket;
import com.whoplate.paipable.thread.XThread;
import com.whoplate.paipable.util.XDebug;

import okhttp3.Response;

public class ActivityWebView extends XActivity {
    final static private String htmlHead = "<head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\"><meta name=\"apple-mobile-web-app-capable\" content=\"yes\"/></head>";
    final static private String htmlBodyStart = "<body>";
    final static private String htmlBodyEnd = "</body>";
    final static private String htmlStart = "<html lang=\"en\">";
    final static private String htmlEnd = "</html>";
    private WebView webView;

    private int paperId;
    private String titleStr;

    @Override
    public int GetContentView() {
        return R.layout.activity_webview;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = (WebView) findViewById(R.id.webview);

        Intent intent = getIntent();
        paperId = intent.getIntExtra("paperid", 0);
        titleStr = intent.getStringExtra("title");
        title.setText(titleStr);

        if (paperId == 0) {
            return;
        }

        showWebview();
    }

    private void showWebview() {
        XThread.RunBackground(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = Http.Get(Const.URL_APN + Const.URL_PAPER_CONTENT + "?paperid=" + paperId);

                    final OkPacket packet = LoganSquare.parse(response.body().byteStream(), OkPacket.class);
                    if (packet.Ok) {
                        generateWebview(packet.Data);
                    }

                } catch (Exception e) {
                    XDebug.Handle(e);
                }
            }
        });
    }

    private void generateWebview(final String content) {

        StringBuilder sb = new StringBuilder(htmlStart);
        sb.append(htmlHead);
        sb.append(htmlBodyStart);
        sb.append(content);
        sb.append(htmlBodyEnd);
        sb.append(htmlEnd);

        final String html = sb.toString();

        Log.d("tan", "html:" + html);

        App.Uihandler.post(new Runnable() {
            @Override
            public void run() {
                webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
            }
        });
    }
}
