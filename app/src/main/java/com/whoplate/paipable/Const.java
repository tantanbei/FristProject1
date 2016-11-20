package com.whoplate.paipable;

import java.util.ArrayList;

public class Const {

//        static final public String SERVER_IP = "http://192.168.1.4";
    static final public String SERVER_IP = "http://114.55.174.165";

    static final public String CUSTOMER_SERVICE_PHONE = "18117541072";

    static final public String URL_SING_UP = "/signup";
    static final public String URL_LOG_IN = "/login";
    static final public String URL_GET_CODE = "/signup/code";
    static final public String URL_PURCHASE_INTENT = "/user/purchase";
    static final public String URL_CHANGE_USERNAME = "/user/change/username";
    static final public String URL_AUCTION_STATUS = "/auction/status";
    static final public String URL_AUCTION_STRATEGY = "/auction/strategy";
    static final public String URL_AUCTION_DETAIL_DATES = "/auction/detail/dates";
    static final public String URL_POINT_STATUS = "/point/get";
    static final public String URL_SIGN_IN = "/signin";
    static final public String URL_FEEDBACK = "/feedback/add";
    static final public String URL_PAPER_CONTENT = "/paper/content";


    static final public ArrayList<String> DataKeywords = new ArrayList<>();

    static {
        DataKeywords.add(1,"201501");
        DataKeywords.add(2,"201502");
        DataKeywords.add(3,"201503");
        DataKeywords.add(4,"201504");
        DataKeywords.add(5,"201505");
        DataKeywords.add(6,"201506");
        DataKeywords.add(7,"201507");
        DataKeywords.add(8,"201508");
        DataKeywords.add(9,"201509");
        DataKeywords.add(10,"201510");
        DataKeywords.add(11,"201511");
        DataKeywords.add(12,"201512");
        DataKeywords.add(13,"201601");
        DataKeywords.add(14,"201602");
        DataKeywords.add(15,"201603");
        DataKeywords.add(16,"201604");
        DataKeywords.add(17,"201605");
        DataKeywords.add(18,"201606");
        DataKeywords.add(19,"201607");
        DataKeywords.add(20,"201608");
        DataKeywords.add(21,"201609");
        DataKeywords.add(22,"201610");
        DataKeywords.add(23,"201611");
        DataKeywords.add(24,"201612");
    }
}
