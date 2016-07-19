package fristproject1.sample.com.fristproject1.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class AuctionStatus {

    @JsonField
    public int Id;

    @JsonField
    public long[] Data;
}
