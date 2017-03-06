package com.whoplate.paipable;

import java.util.ArrayList;

public class Const {

//    static final public String SERVER_IP = "http://192.168.1.6";
    static final public String SERVER_IP = "http://114.55.174.165";

    static final public String CUSTOMER_SERVICE_PHONE = "18117541072";
    static final public String DOMAIN_EXT_PATH = "/paipable";

    static final public String URL_APN = "/apn";
    static final public String URL_API = "/api";

    static final public String URL_SING_UP = "/signup";
    static final public String URL_LOG_IN = "/login";
    static final public String URL_GET_CODE = "/signup/code";
    static final public String URL_RESET_PASSWORD = "/password/reset";
    static final public String URL_PURCHASE_INTENT = "/user/purchase";
    static final public String URL_CHANGE_USERNAME = "/user/change/username";
    static final public String URL_AUCTION_STATUS = "/auction/status";
    static final public String URL_AUCTION_STRATEGY = "/auction/strategy";
    static final public String URL_AUCTION_PRICE = "/auction/price";
    static final public String URL_AUCTION_DETAIL = "/auction/detail";
    static final public String URL_AUCTION_DETAIL_DATES = "/auction/detail/dates";
    static final public String URL_POINT_STATUS = "/point/get";
    static final public String URL_ALL_PRODUCTS = "/product/get";
    static final public String URL_EXCHANGE = "/product/exchange";
    static final public String URL_SIGN_IN = "/signin";
    static final public String URL_FEEDBACK = "/feedback/add";
    static final public String URL_PAPER_CONTENT = "/paper/content";
    static final public String URL_PAPER = "/paper";
    static final public String URL_GET_IMAGE = "/image/get";
    static final public String URL_GET_VIDEO_LIST = "/video/list";
    static final public String URL_UPLOAD_VIDEO = "/video/upload";

    static final public ArrayList<String> DataKeywords = new ArrayList<>();
    static final public ArrayList<String> Reprints = new ArrayList<>();

    static {
        DataKeywords.add(0, "");
        DataKeywords.add(1, "201501");
        DataKeywords.add(2, "201502");
        DataKeywords.add(3, "201503");
        DataKeywords.add(4, "201504");
        DataKeywords.add(5, "201505");
        DataKeywords.add(6, "201506");
        DataKeywords.add(7, "201507");
        DataKeywords.add(8, "201508");
        DataKeywords.add(9, "201509");
        DataKeywords.add(10, "201510");
        DataKeywords.add(11, "201511");
        DataKeywords.add(12, "201512");
        DataKeywords.add(13, "201601");
        DataKeywords.add(14, "201602");
        DataKeywords.add(15, "201603");
        DataKeywords.add(16, "201604");
        DataKeywords.add(17, "201605");
        DataKeywords.add(18, "201606");
        DataKeywords.add(19, "201607");
        DataKeywords.add(20, "201608");
        DataKeywords.add(21, "201609");
        DataKeywords.add(22, "201610");
        DataKeywords.add(23, "201611");
        DataKeywords.add(24, "201612");
        DataKeywords.add(25, "201701");
        DataKeywords.add(26, "201702");
        DataKeywords.add(27, "201703");
        DataKeywords.add(28, "201704");
        DataKeywords.add(29, "201705");
        DataKeywords.add(30, "201706");
        DataKeywords.add(31, "201707");
        DataKeywords.add(32, "201708");
        DataKeywords.add(33, "201709");
        DataKeywords.add(34, "201710");
        DataKeywords.add(35, "201711");
        DataKeywords.add(36, "201712");
        DataKeywords.add(37, "201801");

        Reprints.add(0, "");
        Reprints.add(1, "拍牌宝");
        Reprints.add(2, "国拍网");
        Reprints.add(3, "51沪牌");

    }
}
