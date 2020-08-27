package com.brinfotech.feedbacksystem.network.utils;

public class WebApiHelper {

    //Method Listing
    public static final String LOGIN = "scanQrcodelogin";
    public static final String SIGN_IN_OUT_QRCODE = "scanQrcode";
    public static final String GET_STAFF_REPORT = "getStaffrecords";
    public static final String GET_TODAY_VISITORS = "getTodayvisitors";
    public static final String IMPORT_FIRE_EVACUATION = "importFireevacuation";


    //USER_TYPE
    public static final String USER_TYPE_MANAGER = "2";
    public static final String USER_TYPE_STAFF = "0";

    //STATUS
    public static final String STATUS_SIGNED_IN = "1";
    public static final String STATUS_SIGNED_OUT = "2";

    //DEVICE_TYPE
    public static final String DEVICE_TYPE_TAB = "1";
    public static final String DEVICE_TYPE_MOBILE = "2";


    //BaseUrl of Repository
    public static String BASE_URL = "http://konnectico.co.uk/Staffbook/Webservice/";


}
