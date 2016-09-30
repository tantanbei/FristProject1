package com.whoplate.paipable.http;

import android.content.Context;

import com.whoplate.paipable.R;
import com.whoplate.paipable.db.Pref;
import com.whoplate.paipable.networkpacket.base.JsonBase;
import com.whoplate.paipable.toast.XToast;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Http {
    final private static String SUCCEED = "succeed";//0:mean is failed; 1: mean is succeed

    final public static MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    static OkHttpClient client = new OkHttpClient();

    static public Response Get(final String url) {
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
            XToast.Show(R.string.request_fails);
        }

        return null;
    }

    static public Response Get(final String url, final boolean withSession) {
        try {
            int userId = Pref.Get(Pref.USERID, 0);

            Request request = new Request.Builder()
                    .url(url)
                    .header("userid", String.valueOf(userId))
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            if (!checkIsSucceed(response)) {
                throw new IOException();
            }

            return response;

        } catch (IOException e) {
            e.printStackTrace();
            XToast.Show(R.string.request_fails);
        }

        return null;
    }

    static public Response Get(final String url, final String heads, final String values) {
        try {

            Request.Builder builder = new Request.Builder()
                    .url(url)
                    .get();

            builder.header(heads, values);

            Request request = builder.build();

            Response response = client.newCall(request).execute();
            if (!checkIsSucceed(response)) {
                throw new IOException();
            }

            return response;

        } catch (IOException e) {
            e.printStackTrace();
            XToast.Show(R.string.request_fails);
        }

        return null;
    }

    static public Response Get(final String url, final ArrayList<String> heads, final ArrayList<String> values) {
        try {

            if (heads.size() != values.size()) {
                throw new RuntimeException("heads size is not equal value size!!!");
            }

            Request.Builder builder = new Request.Builder()
                    .url(url)
                    .get();

            for (int i = 0; i < heads.size(); i++) {
                builder.header(heads.get(i), values.get(i));
            }

            Request request = builder.build();

            Response response = client.newCall(request).execute();
            if (!checkIsSucceed(response)) {
                throw new IOException();
            }

            return response;

        } catch (IOException e) {
            e.printStackTrace();
            XToast.Show(R.string.request_fails);
        }

        return null;
    }

    static public Response Post(final String url, final String json) {
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
            XToast.Show(R.string.request_fails);
        }

        return null;
    }

    static public Response Post(final String url, final JsonBase packet) {
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
            XToast.Show(R.string.request_fails);
        }

        return null;
    }

    static public Response Post(final String url, final JsonBase packet, final boolean withSession) {
        if (!withSession) {
            return Post(url, packet);
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
            XToast.Show(R.string.request_fails);
        }

        return null;
    }

    static private boolean checkIsSucceed(Response response) {
        if (response == null || response.header(SUCCEED, "0").equals("0")) {
            return false;
        } else {
            return true;
        }
    }
}
