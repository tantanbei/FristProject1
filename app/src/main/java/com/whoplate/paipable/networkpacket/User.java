package com.whoplate.paipable.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class User {
    @JsonField
    public int UserId;

    @JsonField
    public String UserPhone;

    @JsonField
    public String UserName;

    @JsonField
    public String TokenId;

    public User() {

    }
}
