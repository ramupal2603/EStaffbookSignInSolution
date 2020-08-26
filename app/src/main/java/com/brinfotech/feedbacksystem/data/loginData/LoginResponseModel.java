package com.brinfotech.feedbacksystem.data.loginData;

import java.util.ArrayList;

public class LoginResponseModel {

    String user_id;
    String status;
    ArrayList<LoginResponseDataModel> visitor_details;

    public ArrayList<LoginResponseDataModel> getVisitor_details() {
        return visitor_details;
    }

    public void setVisitor_details(ArrayList<LoginResponseDataModel> visitor_details) {
        this.visitor_details = visitor_details;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
