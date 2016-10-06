package com.whoplate.paipable.networkpacket;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.bluelinelabs.logansquare.typeconverters.IntBasedTypeConverter;
import com.whoplate.paipable.networkpacket.base.JsonBase;

import java.util.ArrayList;


@JsonObject
public class PurchaseIntentCar extends JsonBase {
    @JsonField
    public boolean HaveORNotCar;

    @JsonField
    public ArrayList<Integer> HaveCarStyles;

    @JsonField
    public ArrayList<Integer> HaveCarAges;

    @JsonField
    public int IntentCarStyle;

    public PurchaseIntentCar(){}

}
