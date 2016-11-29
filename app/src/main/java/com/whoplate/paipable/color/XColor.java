package com.whoplate.paipable.color;

import android.support.annotation.ColorRes;

import com.whoplate.paipable.App;

public class XColor {
    static public int Get(@ColorRes int res){
        return App.GetResources().getColor(res);
    }
}
