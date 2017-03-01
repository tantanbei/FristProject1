package com.whoplate.paipable.http;

import android.content.Intent;
import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.whoplate.paipable.App;
import com.whoplate.paipable.Const;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.ActivityLogIn;
import com.whoplate.paipable.db.Pref;
import com.whoplate.paipable.networkpacket.base.JsonBase;
import com.whoplate.paipable.session.XSession;
import com.whoplate.paipable.stack.XStack;
import com.whoplate.paipable.toast.XToast;
import com.whoplate.paipable.util.XDebug;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
                    .url(Const.SERVER_IP + url)
                    .get()
                    .addHeader(HEADER_TOKEN, Pref.Get(Pref.TOKENID, ""))
                    .build();

            Response response = client.newCall(request).execute();
            if (!checkIsSucceed(response)) {
                if (response == null || response.header(HEADER_ERROR, "").equals(ERR_CODE_AUTH_FAILED)) {
                    XSession.Logout();
                    Intent intent = new Intent(XStack.GetLastAliveActivity(), ActivityLogIn.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    XStack.GetLastAliveActivity().startActivity(intent);
                    return null;
                }
                throw new IOException();
            }

            return response;

        } catch (IOException e) {
            XToast.Show(R.string.request_fails);
//            XDebug.Handle(e);
        }

        return null;
    }

    static public Response Post(final String url, final Object packet) {
        try {

            String json = LoganSquare.serialize(packet);

            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(Const.SERVER_IP + url)
                    .header(HEADER_TOKEN, Pref.Get(Pref.TOKENID, ""))
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            if (!checkIsSucceed(response)) {
                if (response == null || response.header(HEADER_ERROR, "").equals(ERR_CODE_AUTH_FAILED)) {
                    XSession.Logout();
                    Intent intent = new Intent(XStack.GetLastAliveActivity(), ActivityLogIn.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    XStack.GetLastAliveActivity().startActivity(intent);
                    return null;
                }
                throw new IOException();
            }

            return response;
        } catch (IOException e) {
            XToast.Show(R.string.request_fails);
//            XDebug.Handle(e);
        }

        return null;
    }

    static public Response Post(final String url, final ArrayList<String> keys, final ArrayList<byte[]> values) {
        if (keys.size() != values.size()) {
            return null;
        }

        try {

            final MultipartBody.Builder multiBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            for (int i = 0; i < keys.size(); i++) {
                multiBuilder.addFormDataPart(keys.get(i), "", MultipartBody.create(null, values.get(i)));
            }

            MultipartBody body = multiBuilder.build();

            Request request = new Request.Builder()
                    .url(Const.SERVER_IP + url)
                    .header(HEADER_TOKEN, Pref.Get(Pref.TOKENID, ""))
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            if (!checkIsSucceed(response)) {
                if (response == null || response.header(HEADER_ERROR, "").equals(ERR_CODE_AUTH_FAILED)) {
                    XSession.Logout();
                    Intent intent = new Intent(XStack.GetLastAliveActivity(), ActivityLogIn.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    XStack.GetLastAliveActivity().startActivity(intent);
                    return null;
                }
                throw new IOException();
            }

            return response;
        } catch (IOException e) {
            XToast.Show(R.string.request_fails);
//            XDebug.Handle(e);
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
