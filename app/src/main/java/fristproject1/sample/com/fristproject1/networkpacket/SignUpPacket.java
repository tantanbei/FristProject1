package fristproject1.sample.com.fristproject1.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import fristproject1.sample.com.fristproject1.networkpacket.base.JsonBase;

@JsonObject
public class SignUpPacket extends JsonBase {

    @JsonField
    public String phone;

    @JsonField
    public String password;

    public SignUpPacket() {
    }

    public SignUpPacket(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }
}
