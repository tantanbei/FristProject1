package fristproject1.sample.com.fristproject1.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class AuctionHistoryResult {

    @JsonField
    public String date;

    @JsonField
    public int limiation;

    @JsonField
    public int peopleNumber;

    @JsonField
    public int minumumPrice;

    @JsonField
    public int averagePrice;

    @JsonField
    public int cautionPrice;

    @Override
    public String toString() {
        return "date:" + date + " limiation:" + limiation + " peopleNumber:" + peopleNumber + " minumumPrice:" + minumumPrice + " averagePrice:" + averagePrice + " cautionPrice:" + cautionPrice;
    }
}
