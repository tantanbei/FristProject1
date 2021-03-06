package com.whoplate.paipable.networkpacket.base;

import com.bluelinelabs.logansquare.LoganSquare;
import com.whoplate.paipable.util.XDebug;

import java.io.IOException;

public class JsonBase {

    public String ToJsonString(){
        try {
            return LoganSquare.serialize(this);
        } catch (IOException e) {
            XDebug.Handle(e);
        }

        return null;
    }
}
