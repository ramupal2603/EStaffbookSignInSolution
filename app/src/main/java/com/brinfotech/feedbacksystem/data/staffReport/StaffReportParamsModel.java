package com.brinfotech.feedbacksystem.data.staffReport;

import com.google.gson.annotations.SerializedName;

public class StaffReportParamsModel {
    @SerializedName("company_id")
    String site_id;

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }
}
