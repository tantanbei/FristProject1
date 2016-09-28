package com.whoplate.paipable.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class AuctionHistoryResults {

    @JsonField
    public AuctionHistoryResult[] auctionHistoryResults;

    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < auctionHistoryResults.length; i++) {
            str += auctionHistoryResults[i].toString();
        }
        return str;
    }
}
