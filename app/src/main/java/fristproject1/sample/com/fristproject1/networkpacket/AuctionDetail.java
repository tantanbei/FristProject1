package fristproject1.sample.com.fristproject1.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class AuctionDetail {

    @JsonField
    public int Distance;

    @JsonField
    public int Price;

    public AuctionDetail() {

    }
}
