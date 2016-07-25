package fristproject1.sample.com.fristproject1.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import fristproject1.sample.com.fristproject1.networkpacket.base.JsonBase;

@JsonObject
public class RequestAuctionHistoryPacket extends JsonBase {

    @JsonField
    boolean needDate;

    @JsonField
    boolean needLimiation;

    @JsonField
    boolean needPeopleNum;

    @JsonField
    boolean needMinimumPrice;

    @JsonField
    boolean needAveragePrice;

    @JsonField
    boolean needCautionPrice;

    @Override
    public String toString() {
        return "needDate:" + needDate + " needLimiation:" + needLimiation + " needPeopleNum:" + needPeopleNum + " needMinimumPrice:" + needMinimumPrice + " needAveragePrice:" + needAveragePrice + " needCautionPrice:" + needCautionPrice;
    }

    public RequestAuctionHistoryPacket(){}

    public RequestAuctionHistoryPacket(boolean needDate, boolean needLimiation,boolean needPeopleNum,boolean needMinimimPrice,boolean needAveragePrice,boolean needCautionPrice) {
        this.needDate = needDate;
        this.needLimiation = needLimiation;
        this.needPeopleNum = needPeopleNum;
        this.needMinimumPrice = needMinimimPrice;
        this.needAveragePrice = needAveragePrice;
        this.needCautionPrice = needCautionPrice;
    }
}
