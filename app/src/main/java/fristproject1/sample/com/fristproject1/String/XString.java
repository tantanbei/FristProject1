package fristproject1.sample.com.fristproject1.String;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;

import fristproject1.sample.com.fristproject1.R;

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
