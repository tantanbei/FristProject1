package fristproject1.sample.com.fristproject1.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;

@JsonObject
public class CarPrices {

    @JsonField
    public int id;

    @JsonField
    public ArrayList<CarPriceOneTime> carPrice;

    public CarPrices() {
    }

    @Override
    public String toString() {
        return "id:"+this.id
                +"carPrice:"+this.carPrice.toString();
    }
}
