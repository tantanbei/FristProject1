package com.whoplate.paipable.string;

import android.content.Context;
import android.support.annotation.StringRes;

import com.whoplate.paipable.stack.XStack;

public class XString {

    static public String GetString(@StringRes int id){
        return XStack.GetLastAliveActivity().getString(id);
    }

    static public boolean IsEmpty(String str){
        if (str == null || str.length() == 0 || str.equals("")){
            return true;
        }else {
            return false;
        }
    }
}
