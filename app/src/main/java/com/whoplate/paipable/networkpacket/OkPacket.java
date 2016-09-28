package com.whoplate.paipable.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class OkPacket {
    @JsonField
    public boolean Ok;

    @JsonField
    public String Data;

    public OkPacket() {
    }
}
