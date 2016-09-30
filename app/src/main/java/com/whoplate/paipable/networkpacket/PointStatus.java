package com.whoplate.paipable.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.whoplate.paipable.networkpacket.base.JsonBase;

@JsonObject
public class PointStatus extends JsonBase{
    @JsonField
    public int KeepDays;

    @JsonField
    public int Point;

    @JsonField
    public boolean IsSignInToday;

    public PointStatus() {
    }
}
