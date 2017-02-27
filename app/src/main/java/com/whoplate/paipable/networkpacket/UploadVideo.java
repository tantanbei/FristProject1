package com.whoplate.paipable.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class UploadVideo {
    @JsonField
    public String Title;
    @JsonField
    public String Summary;
    @JsonField
    public byte[] Thumbnail;
    @JsonField
    public byte[] Video;

    public UploadVideo() {
    }
}
