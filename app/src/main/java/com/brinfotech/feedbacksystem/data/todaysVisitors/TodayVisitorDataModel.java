package com.brinfotech.feedbacksystem.data.todaysVisitors;

import com.google.gson.annotations.SerializedName;

public class TodayVisitorDataModel {
    String visitor_id;
    String visitor_name;
    String log_id;
    String visitor_type;

    public String getVisitor_type() {
        return visitor_type;
    }

    public void setVisitor_type(String visitor_type) {
        this.visitor_type = visitor_type;
    }

    @SerializedName("company_id")
    String site_id;
    String visitor_type;
    String log_id;

    public String getVisitor_type() {
        return visitor_type;
    }

    public void setVisitor_type(String visitor_type) {
        this.visitor_type = visitor_type;
    }

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public String getSite_id() {
        return site_id;
    }

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getVisitor_id() {
        return visitor_id;
    }

    public void setVisitor_id(String visitor_id) {
        this.visitor_id = visitor_id;
    }

    public String getVisitor_name() {
        return visitor_name;
    }

    public void setVisitor_name(String visitor_name) {
        this.visitor_name = visitor_name;
    }
}
