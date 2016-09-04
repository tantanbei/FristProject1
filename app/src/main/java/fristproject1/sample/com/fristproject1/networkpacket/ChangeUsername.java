package fristproject1.sample.com.fristproject1.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import fristproject1.sample.com.fristproject1.networkpacket.base.JsonBase;

@JsonObject
public class ChangeUsername extends JsonBase{

    @JsonField
    String Username;

    public ChangeUsername(){}

    public ChangeUsername(final String newUsername){
        this.Username = newUsername;
    }
}
