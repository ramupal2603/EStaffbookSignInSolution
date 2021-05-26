package com.brinfotech.feedbacksystem.data.currentVersion;

public class CurrentVersionRequestParamModel {

    String user_id;
    String user_type;
    String mobile_app;

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMobile_app() {
        return mobile_app;
    }

    public void setMobile_app(String mobile_app) {
        this.mobile_app = mobile_app;
    }
}
