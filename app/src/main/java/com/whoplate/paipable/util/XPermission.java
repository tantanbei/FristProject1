package com.whoplate.paipable.util;

import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.interfeet.GrantedPermissionCallback;

public class XPermission {

    static public boolean Want(@NonNull XActivity a, String[] features, int requestId, @NonNull GrantedPermissionCallback cb) {

        //android < 6.0 do not need permission..
        if (Build.VERSION.SDK_INT < 23) {
            a.DefaultGrantedPermission(true, features, requestId);
            cb.GrantedPermission(true, features);
            return true;
        }

        Log.d("xing", "Permission want permission.." + features + ":" + requestId);

        if (features == null || features.length == 0) {
            XDebug.Handle(new RuntimeException("features is empty string array"));
            return false;
        }

        //default to true;
        boolean grantedAll = true;
        //list of permissions not granted but wanted...;
        final ArrayList<String> notGranted = new ArrayList<>();

        for (String feature : features) {

            int result = ContextCompat.checkSelfPermission(a, feature);

            if (result != PackageManager.PERMISSION_GRANTED) {
                Log.d("xing", "Permission did not get permission.." + feature + ":" + requestId);
                notGranted.add(feature);
                grantedAll = false;
            } else {
                Log.d("xing", "Permission got permission.." + feature + ":" + requestId);
            }

        }

        if (grantedAll) {
            //fire event
            a.DefaultGrantedPermission(grantedAll, features, requestId);
            cb.GrantedPermission(true, features);
        } else {
            if (a.PermissionCallbacks == null) {
                a.PermissionCallbacks = new SparseArray<>();
            }

            //register callback
            a.PermissionCallbacks.put(requestId, cb);
            Ask(a, notGranted.toArray(new String[notGranted.size()]), requestId);
        }

        return grantedAll;

    }

    static public void Ask(@NonNull XActivity a, @NonNull String[] features, int requestId) {
        Log.d("xing", "Permission asking for permission.." + features + ":" + requestId);
        ActivityCompat.requestPermissions(a,
                features,
                requestId);

    }
}

