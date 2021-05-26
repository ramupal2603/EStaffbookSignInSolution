package com.brinfotech.feedbacksystem.data.staffReport;

import com.google.gson.annotations.SerializedName;

public class StaffReportParamsModel {
    @SerializedName("company_id")
    String site_id;


    @SerializedName("site_id")
    String location_id;

    @SerializedName("user_id")
    String userID;

    @SerializedName("user_type")
    String user_type;

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }
}
