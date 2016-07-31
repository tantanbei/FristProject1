package fristproject1.sample.com.fristproject1.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import fristproject1.sample.com.fristproject1.networkpacket.base.JsonBase;

@JsonObject
public class SignUpInPacket extends JsonBase {

    @JsonField
    public String Phone;

    @JsonField
    public String Password;

    public SignUpInPacket() {
    }

    public SignUpInPacket(String phone, String password) {
        this.Phone = phone;
        this.Password = password;
    }
}
