package fristproject1.sample.com.fristproject1.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class OkPacket {
    @JsonField
    public boolean Ok;

    @JsonField
    public String Data;

    public OkPacket() {
    }
}
