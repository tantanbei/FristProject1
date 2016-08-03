package fristproject1.sample.com.fristproject1.db;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.HashMap;

@JsonObject
public class PrefPacket {

    @JsonField
    HashMap<String,String> Data;

    public PrefPacket() {
        Data = new HashMap<>();
    }
}
