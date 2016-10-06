package com.whoplate.paipable.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class AuctionHistoryResults {

    @JsonField
    public AuctionHistoryResult[] auctionHistoryResults;

    @Override
    public String toString() {
        if (auctionHistoryResults == null){
            return "";
        }

        String str = "";
        for (AuctionHistoryResult result : auctionHistoryResults) {
            str += result.toString();
        }
        return str;
    }
}
