package fristproject1.sample.com.fristproject1.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class CarPriceOneTime {

    @JsonField
    public String time;

    @JsonField
    public String price;

    @JsonField
    public String remark;

    public CarPriceOneTime() {
    }

    @Override
    public String toString() {
        return "time:"+time+" price:"+price+" remark:"+remark;
    }
}
