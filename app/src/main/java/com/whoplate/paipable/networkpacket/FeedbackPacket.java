package com.whoplate.paipable.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.whoplate.paipable.networkpacket.base.JsonBase;

@JsonObject
public class FeedbackPacket extends JsonBase{
    @JsonField
    public String Content;

    @JsonField
    public String DeviceMessage;

    public FeedbackPacket() {
    }

    public FeedbackPacket(String content, String deviceMessage) {
        this.Content = content;
        this.DeviceMessage = deviceMessage;
    }
}
