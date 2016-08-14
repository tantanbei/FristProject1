package fristproject1.sample.com.fristproject1.http;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;

import fristproject1.sample.com.fristproject1.App;
import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.db.Pref;
import fristproject1.sample.com.fristproject1.networkpacket.base.JsonBase;
import fristproject1.sample.com.fristproject1.string.XString;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Http {
    final private static String SUCCEED = "succeed";//0:mean is failed; 1: mean is succeed

    final public static MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    static OkHttpClient client = new OkHttpClient();

    static public Response Get(final Context context, final String url) {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            if (!checkIsSucceed(response)) {
                throw new IOException();
            }

            return response;

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

            Response response = client.newCall(request).execute();
            if (!checkIsSucceed(response)) {
                throw new IOException();
            }

            return response;
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

            Response response = client.newCall(request).execute();
            if (!checkIsSucceed(response)) {
                throw new IOException();
            }

            return response;
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

    static public Response Post(final Context context, final String url, final JsonBase packet, final boolean withSession) {
        if (!withSession) {
            return Post(context, url, packet);
        }

        try {

            int userId = Pref.Get(Pref.USERID, 0);

            String json = packet.ToJsonString();

            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .header("userid", String.valueOf(userId))
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            if (!checkIsSucceed(response)) {
                throw new IOException();
            }

            return response;

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

    static private boolean checkIsSucceed(Response response) {
        if (response.header(SUCCEED, "0").equals("0")) {
            return false;
        } else {
            return true;
        }
    }
}
