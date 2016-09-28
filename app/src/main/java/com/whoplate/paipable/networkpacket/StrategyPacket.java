package com.whoplate.paipable.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class StrategyPacket {
    @JsonField
    public int Style;
    @JsonField
    public int ForecastPrice;
    @JsonField
    public int Second30;
    @JsonField
    public int Second40;
    @JsonField
    public int Second45;
    @JsonField
    public int Second50;

    public StrategyPacket() {
    }
}
