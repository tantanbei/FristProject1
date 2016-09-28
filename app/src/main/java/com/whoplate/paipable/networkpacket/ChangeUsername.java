package com.whoplate.paipable.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.whoplate.paipable.networkpacket.base.JsonBase;


@JsonObject
public class ChangeUsername extends JsonBase {

    @JsonField
    String Username;

    public ChangeUsername(){}

    public ChangeUsername(final String newUsername){
        this.Username = newUsername;
    }
}
