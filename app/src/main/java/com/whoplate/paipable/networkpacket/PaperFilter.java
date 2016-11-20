package com.whoplate.paipable.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;

@JsonObject
public class PaperFilter {

    @JsonField
    public int CategoryId;

    @JsonField
    public ArrayList<Integer> KeywordIds;

    public PaperFilter() {
        KeywordIds = new ArrayList<>();
    }
}
