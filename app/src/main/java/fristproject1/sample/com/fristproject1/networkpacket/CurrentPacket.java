package fristproject1.sample.com.fristproject1.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;

@JsonObject
public class CurrentPacket {

    @JsonField
    public int currentTransactionPrice;

    @JsonField
    public int forecastTransactionPrice;

    @JsonField
    public int limitation;

    @JsonField
    public int currentPeopleNumber;

    @JsonField
    public long serverTime;

    public CurrentPacket() {
    }

    @Override
    public String toString() {
        return "CurrentPacket { currentTransactionPrice:" + this.currentTransactionPrice + " serverTime:" + serverTime + " }";
    }
}
