package fristproject1.sample.com.fristproject1.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;

@JsonObject
public class CurrentPrice {

    @JsonField
    public int price;

    public CurrentPrice() {
    }

    @Override
    public String toString() {
        return "price:"+this.price;
    }
}
