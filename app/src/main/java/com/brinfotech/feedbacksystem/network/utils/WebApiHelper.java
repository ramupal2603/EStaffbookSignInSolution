package com.brinfotech.feedbacksystem.network.utils;

public class WebApiHelper {

    //Method Listing
    public static final String LOGIN = "RestController.php?action=userLogin";
    public static final String DASHBOARD = "RestController.php?action=getPermitdetails";
    public static final String UPDATE_SIGNATURE = "RestController.php?action=updatePermitsignatures";
    public static final String UPDATE_DETAILS = "RestController.php?action=updateDetails";
    public static final String UPDATE_PERMIT_FORM = "RestController.php?action=updatePermitform";
    public static final String GET_PERSON_IN_CHARGE_LIST = "RestController.php?action=getAdminuser";


    //Constant
    public static final String userType = "1";


    //BaseUrl of Repository
    public static String BASE_URL = "https://advertico.co.uk/Jll/Webservice/";


}
