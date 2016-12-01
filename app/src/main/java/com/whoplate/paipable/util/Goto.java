package com.whoplate.paipable.util;

import android.content.Context;
import android.content.Intent;

import com.whoplate.paipable.App;
import com.whoplate.paipable.activity.ActivityLogIn;
import com.whoplate.paipable.activity.ActivitySignUp;
import com.whoplate.paipable.stack.XStack;

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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        c.startActivity(intent);
    }
}
