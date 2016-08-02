package fristproject1.sample.com.fristproject1.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class User {
    @JsonField
    public int UserId;

    @JsonField
    public String UserPhone;

    @JsonField
    public String UserName;

    public User() {

    }
}
