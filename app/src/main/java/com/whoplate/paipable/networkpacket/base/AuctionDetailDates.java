package com.whoplate.paipable.networkpacket.base;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;

@JsonObject
public class AuctionDetailDates {
    @JsonField
    public ArrayList<String> Dates;

    public AuctionDetailDates() {
    }
}
