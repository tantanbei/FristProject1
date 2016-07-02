package fristproject1.sample.com.fristproject1.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class AuctionHistoryResult {

    @JsonField
    String date;

    @JsonField
    int limiation;

    @JsonField
    int peopleNumber;

    @JsonField
    int minumumPrice;

    @JsonField
    int averagePrice;

    @JsonField
    int cautionPrice;

    @Override
    public String toString() {
        return "date:" + date + " limiation:" + limiation + " peopleNumber:" + peopleNumber + " minumumPrice:" + minumumPrice + " averagePrice:" + averagePrice + " cautionPrice:" + cautionPrice;
    }
}
