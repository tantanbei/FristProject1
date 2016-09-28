package com.whoplate.paipable.string;

import android.content.Context;
import android.support.annotation.StringRes;

public class XString {

    static public String GetString(Context context, @StringRes int id){
        return context.getString(id);
    }

    static public boolean IsEmpty(String str){
        if (str == null || str.length() == 0 || str.equals("")){
            return true;
        }else {
            return false;
        }
    }
}
