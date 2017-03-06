package com.whoplate.paipable.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class OkPacket2 {
    @JsonField
    public int Code;
    @JsonField
    public String Data;

    public OkPacket2() {
        Code = -1;
        Data = "Can not use default value";
    }
}
