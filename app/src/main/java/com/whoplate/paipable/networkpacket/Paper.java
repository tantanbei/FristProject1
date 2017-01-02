package com.whoplate.paipable.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class Paper {
    @JsonField
    public int PaperId;
    @JsonField
    public String Title;
    @JsonField
    public int CategoryId;
    @JsonField
    public int KeywordId1;
    @JsonField
    public int KeywordId2;
    @JsonField
    public int KeywordId3;
    @JsonField
    public int KeywordId4;
    @JsonField
    public int ReprintId;
    @JsonField
    public long DateSubmit;
    @JsonField
    public int ImageId;

    public Paper() {
    }
}
