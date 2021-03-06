package com.whoplate.paipable.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.whoplate.paipable.networkpacket.base.JsonBase;

@JsonObject
public class RequestAuctionHistoryPacket extends JsonBase {

    @JsonField
    boolean needDate;

    @JsonField
    boolean needLimitation;

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
        return "needDate:" + needDate + " needLimiation:" + needLimitation + " needPeopleNum:" + needPeopleNum + " needMinimumPrice:" + needMinimumPrice + " needAveragePrice:" + needAveragePrice + " needCautionPrice:" + needCautionPrice;
    }

    public RequestAuctionHistoryPacket(){}

    public RequestAuctionHistoryPacket(boolean needDate, boolean needLimitation,boolean needPeopleNum,boolean needMinimimPrice,boolean needAveragePrice,boolean needCautionPrice) {
        this.needDate = needDate;
        this.needLimitation = needLimitation;
        this.needPeopleNum = needPeopleNum;
        this.needMinimumPrice = needMinimimPrice;
        this.needAveragePrice = needAveragePrice;
        this.needCautionPrice = needCautionPrice;
    }
}
