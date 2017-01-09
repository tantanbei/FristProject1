package com.whoplate.paipable.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class Product {
    @JsonField
    public int ProduceId;
    @JsonField
    public String Title;
    @JsonField
    public int Point;
    @JsonField
    public int ProduceType;
    @JsonField
    public String Value;
    @JsonField
    public int Stock;
    @JsonField
    public long ExpiryDate;
//    @JsonField
//    public int ImageId;

    public Product() {
    }
}
