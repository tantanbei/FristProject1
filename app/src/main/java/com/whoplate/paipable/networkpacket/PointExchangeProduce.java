package com.whoplate.paipable.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class PointExchangeProduce {
    @JsonField
    int ProduceId;
    @JsonField
    String Title;
    @JsonField
    int Point;
    @JsonField
    int Inventory;
    @JsonField
    int ImageId;

    public PointExchangeProduce() {
    }
}
