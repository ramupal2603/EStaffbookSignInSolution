package com.brinfotech.feedbacksystem.data.todaysVisitors;

import com.google.gson.annotations.SerializedName;

public class TodaysParamsModel {

    @SerializedName("company_id")
    String site_id;

    @SerializedName("site_id")
    String location_id;

    @SerializedName("user_id")
    String UserID;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
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
