package com.whoplate.paipable.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class SignInBack {
    @JsonField
    public int KeepDays;

    @JsonField
    public int Point;

    public SignInBack() {
    }
}
