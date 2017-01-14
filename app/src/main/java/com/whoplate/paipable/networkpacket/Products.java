package com.whoplate.paipable.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;

@JsonObject
public class Products {
    @JsonField
    public ArrayList<ProductWithImage> Data;

    public Products() {
    }
}
