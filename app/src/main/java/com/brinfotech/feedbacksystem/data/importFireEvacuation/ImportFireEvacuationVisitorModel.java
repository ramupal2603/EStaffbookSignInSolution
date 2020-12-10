package com.brinfotech.feedbacksystem.data.importFireEvacuation;

import com.google.gson.annotations.SerializedName;

public class ImportFireEvacuationVisitorModel {
    String evacuation_date;
    String evacuation_time;
    String visitor_id;
    String log_id;
    String visitor_name;
    String visitor_type;
    @SerializedName("company_id")
    String site_id;
    @SerializedName("site_id")
    String location_id;

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public String getEvacuation_date() {
        return evacuation_date;
    }

    public void setEvacuation_date(String evacuation_date) {
        this.evacuation_date = evacuation_date;
    }

    public String getEvacuation_time() {
        return evacuation_time;
    }

    public void setEvacuation_time(String evacuation_time) {
        this.evacuation_time = evacuation_time;
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

    public String getVisitor_type() {
        return visitor_type;
    }

    public void setVisitor_type(String visitor_type) {
        this.visitor_type = visitor_type;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }
}
