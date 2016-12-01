package com.whoplate.paipable.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.whoplate.paipable.networkpacket.base.JsonBase;


@JsonObject
public class GetCode extends JsonBase {

    @JsonField
    public String Phone;

    //the phone must be registered
    @JsonField
    public boolean MustRegistered;

    @JsonField
    public String Code;

    public GetCode() {
    }

    public GetCode(String Phone, boolean MustRegistered, String Code) {
        this.Phone = Phone;
        this.MustRegistered = MustRegistered;
        this.Code = Code;
    }
}
