package com.whoplate.paipable.networkpacket.base;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.whoplate.paipable.networkpacket.Paper;

import java.util.ArrayList;

@JsonObject
public class Papers extends JsonBase{
    @JsonField
    public ArrayList<Paper> Data;

    public Papers() {
    }
}
