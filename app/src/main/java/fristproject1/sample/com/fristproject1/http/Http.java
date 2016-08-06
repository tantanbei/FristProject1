package fristproject1.sample.com.fristproject1.http;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;

import fristproject1.sample.com.fristproject1.App;
import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.networkpacket.base.JsonBase;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Http {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    static OkHttpClient client = new OkHttpClient();

    static public Response Get(final Context context, final String url) {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            Call call = client.newCall(request);

            return call.execute();
        } catch (IOException e) {
            e.printStackTrace();
            App.Uihandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, R.string.request_fails, Toast.LENGTH_SHORT).show();
                }
            });
        }

        return null;
    }

    static public Response Post(final Context context, final String url, final String json) {
        try {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            return client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
            App.Uihandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, R.string.request_fails, Toast.LENGTH_SHORT).show();
                }
            });
        }

        return null;
    }

    static public Response Post(final Context context, final String url, final JsonBase packet) {
        try {

            String json = packet.ToJsonString();

            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            return client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            App.Uihandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, R.string.request_fails, Toast.LENGTH_SHORT).show();
                }
            });
        }

        return null;
    }
}
