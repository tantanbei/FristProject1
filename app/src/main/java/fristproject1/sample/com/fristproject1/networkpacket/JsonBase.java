package fristproject1.sample.com.fristproject1.networkpacket;

import com.bluelinelabs.logansquare.LoganSquare;

import java.io.IOException;

public class JsonBase {

    public String ToJsonString(){
        try {
            return LoganSquare.serialize(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
