package com.whoplate.paipable.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.whoplate.paipable.networkpacket.base.JsonBase;


@JsonObject
public class AuctionDetails extends JsonBase {

    @JsonField
    public AuctionDetail[] Details;

    public AuctionDetails() {

    }
}
