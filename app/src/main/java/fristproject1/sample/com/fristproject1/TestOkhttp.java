package fristproject1.sample.com.fristproject1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestOkhttp extends Activity {

    Call call;
    OkHttpClient client;
    Request request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testokhttp);

        final TextView tvShow = (TextView) findViewById(R.id.tvShow);
        Button btSend = (Button) findViewById(R.id.btnSend);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client = new OkHttpClient();
                request = new Request.Builder()
                        .url("http://192.168.0.106:8080/chexiang/produce/id?id=" + 888)
//                        .url("https://github.com/hongyangAndroid")
                        .method("GET",null)
                        .build();

                Call call = client.newCall(request);

                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("tan", e.toString());
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String str = response.body().string();

                        tvShow.post(new Runnable() {
                            @Override
                            public void run() {
                                tvShow.setText(str);
                            }
                        });
                    }
                });
            }
        });
    }
}
