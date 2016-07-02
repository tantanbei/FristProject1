package fristproject1.sample.com.fristproject1.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class RequestAuctionHistoryPacket {

    @JsonField
    boolean needDate;

    @JsonField
    boolean needLimiation;

    @JsonField
    boolean needPeopleNum;

    @JsonField
    boolean needMinimimPrice;

    @JsonField
    boolean needAveragePrice;

    @JsonField
    boolean needCautionPrice;

    @Override
    public String toString() {
        return "needDate:" + needDate + " needLimiation:" + needLimiation + " needPeopleNum:" + needPeopleNum + " needMinimimPrice:" + needMinimimPrice + " needAveragePrice:" + needAveragePrice + " needCautionPrice:" + needCautionPrice;
    }
}
