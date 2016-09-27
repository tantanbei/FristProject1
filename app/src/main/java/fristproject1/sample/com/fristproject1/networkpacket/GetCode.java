package fristproject1.sample.com.fristproject1.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import fristproject1.sample.com.fristproject1.networkpacket.base.JsonBase;

@JsonObject
public class GetCode extends JsonBase {

    @JsonField
    public String Phone;

    @JsonField
    public boolean MustExist;

    @JsonField
    public String Code;

    public GetCode() {
    }

    public GetCode(String Phone, boolean MustExist, String Code) {
        this.Phone = Phone;
        this.MustExist = MustExist;
        this.Code = Code;
    }
}
