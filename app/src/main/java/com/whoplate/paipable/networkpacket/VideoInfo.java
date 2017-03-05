package com.whoplate.paipable.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class VideoInfo {
    @JsonField
    public int VideoId;
    @JsonField
    public String Title;
    @JsonField
    public String Summary;
    @JsonField
    public int UserId;
    @JsonField
    public String UserName;
    @JsonField
    public int ImageId;
    @JsonField
    public long DataSubmit;

}
