package com.brinfotech.feedbacksystem.data.loginData;

import com.google.gson.annotations.SerializedName;

public class LoginResponseDataModel {

    String user_id;
    String user_name;
    String user_type;
    @SerializedName("company_details")
    String site_details;

    @SerializedName("site_id")
    String site_id;

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getSite_details() {
        return site_details;
    }

    public void setSite_details(String site_details) {
        this.site_details = site_details;
    }
}
