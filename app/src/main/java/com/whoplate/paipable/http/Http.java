package com.whoplate.paipable.http;

import com.whoplate.paipable.R;
import com.whoplate.paipable.db.Pref;
import com.whoplate.paipable.networkpacket.base.JsonBase;
import com.whoplate.paipable.toast.XToast;
import com.whoplate.paipable.util.XDebug;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Http {
    final private static String HEADER_SUCCEED = "succeed";//0:mean is failed; 1: mean is succeed
    final private static String FAILED = "0";
    final private static String SUCCEED = "1";

    final private static String HEADER_TOKEN = "t_tokenid";
    final private static String HEADER_ERROR = "t_error_code";

    final private static String ERR_CODE_AUTH_FAILED = "0";
    final private static String ERR_CODE_FAIL_DATA = "1";

    final public static MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    static OkHttpClient client;

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        client = builder.build();
    }

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
            XDebug.Handle(e);
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
            XDebug.Handle(e);
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
            XDebug.Handle(e);
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
            XDebug.Handle(e);
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
            XDebug.Handle(e);
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
            XDebug.Handle(e);
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
            XDebug.Handle(e);
            XToast.Show(R.string.request_fails);
        }

        return null;
    }

    static private boolean checkIsSucceed(Response response) {
        if (response == null || !response.header(HEADER_SUCCEED, FAILED).equals(SUCCEED)) {
            return false;
        } else {
            return true;
        }
    }
}
