package com.whoplate.paipable.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class AuctionHistoryResult {

    @JsonField
    public String date;

    @JsonField
    public int limitation;

    @JsonField
    public int peopleNumber;

    @JsonField
    public int minimumPrice;

    @JsonField
    public int averagePrice;

    @JsonField
    public int cautionPrice;

    @Override
    public String toString() {
        return "date:" + date + " limitation:" + limitation + " peopleNumber:" + peopleNumber + " minimumPrice:" + minimumPrice + " averagePrice:" + averagePrice + " cautionPrice:" + cautionPrice;
    }
}
