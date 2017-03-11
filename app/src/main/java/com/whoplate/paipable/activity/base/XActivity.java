package com.whoplate.paipable.activity.base;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;
import com.whoplate.paipable.App;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.ActivityHome;
import com.whoplate.paipable.activity.ActivityLogIn;
import com.whoplate.paipable.interfeet.GrantedPermissionCallback;
import com.whoplate.paipable.session.XSession;
import com.whoplate.paipable.stack.XStack;
import com.whoplate.paipable.thread.XThread;
import com.whoplate.paipable.toast.XToast;
import com.whoplate.paipable.util.Goto;
import com.whoplate.paipable.util.XDebug;
import com.whoplate.paipable.util.XFabric;

public abstract class XActivity extends AppCompatActivity {

    public IconTextView goBack;
    public TextView title;
    public TextView rigthBtn;

    //permission calls back indexed by requestId...
    public SparseArray<GrantedPermissionCallback> PermissionCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init the fabric
        XThread.RunBackground(new Runnable() {
            @Override
            public void run() {
                XFabric.Load();
            }
        });

        setContentView(GetContentView());

        if (!(this instanceof ActivityHome)) {

            goBack = (IconTextView) findViewById(R.id.goBack);
            title = (TextView) findViewById(R.id.title);
            rigthBtn = (TextView) findViewById(R.id.right_button);

            goBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        XStack.Push(this);

        if (!XSession.IsValid()) {
            Goto.Login();
        }
    }

    public abstract int GetContentView();

    @Override
    protected void onStart() {
        super.onStart();

        XStack.Push(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        XStack.Push(this);

        Log.d("tan", "onResume: xstack size:" + XStack.Size());
    }

    //users should override this
    //success is true is all permission was granted...false if not..
    @SuppressWarnings("UnusedParameters")
    final public void DefaultGrantedPermission(final boolean success, @NonNull final String[] permissions, final int requestId) {
        if (success) {
            for (String permission : permissions) {
                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //we need verify that WRITE_EXTERNAL actually works...
                    App.ResetExternalPaths();
                    return;
                }
            }
        } else {
            XToast.Show(R.string.warning_unsupport_feature);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestId,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        try {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length == 0) {
                //we only accept 1 permission result
                //do nothing is the operation is cancelled.
                return;
            }

            boolean grantedAll = true;

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    grantedAll = false;
                    break;
                }
            }

            DefaultGrantedPermission(grantedAll, permissions, requestId);

            if (PermissionCallbacks != null) {
                final GrantedPermissionCallback cb = PermissionCallbacks.get(requestId);
                if (cb != null) {
                    cb.GrantedPermission(grantedAll, permissions);
                    //remove callback after callback triggers..
                    PermissionCallbacks.remove(requestId);
                }
            }
        } catch (Throwable e) {
            XDebug.Handle(e);
        }
    }
}
