package com.whoplate.paipable.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.whoplate.paipable.App;
import com.whoplate.paipable.Const;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.ActivityHome;
import com.whoplate.paipable.activity.ActivityLogIn;
import com.whoplate.paipable.activity.ActivitySignUp;
import com.whoplate.paipable.stack.XStack;
import com.whoplate.paipable.toast.XToast;

public class Goto {

    public static synchronized void Login() {
        Context c = XStack.GetLastAliveActivity();
        if (c instanceof ActivityLogIn || c instanceof ActivitySignUp) {
            return;
        }

        if (c == null) {
            c = App.INSTANCE;
        }

        Intent intent = new Intent(c, ActivityLogIn.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(intent);
    }

    public static synchronized void Home() {
        Intent intent = new Intent(App.INSTANCE, ActivityHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        XStack.Clear();

        App.INSTANCE.startActivity(intent);
    }

    public static synchronized void DialCustomer(){
        try{
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Const.CUSTOMER_SERVICE_PHONE));
            XStack.GetLastAliveActivity().startActivity(intent);
        }catch (Exception e){
            //do nothing
            XToast.Show(R.string.warning_unsupport_feature);
        }
    }
}
