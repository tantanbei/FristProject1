package fristproject1.sample.com.fristproject1.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.bluelinelabs.logansquare.typeconverters.IntBasedTypeConverter;

import java.util.ArrayList;

import fristproject1.sample.com.fristproject1.networkpacket.base.JsonBase;

@JsonObject
public class Purchase_intent_car extends JsonBase{
    @JsonField
    public boolean HaveORNotCar;

    @JsonField
    public ArrayList<Integer> HaveCarStyles;

    @JsonField
    public ArrayList<Integer> HaveCarAges;

    @JsonField
    public int IntentCarStyle;

}
