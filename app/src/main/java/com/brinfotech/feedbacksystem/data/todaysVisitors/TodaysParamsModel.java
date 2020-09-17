package com.brinfotech.feedbacksystem.data.todaysVisitors;

import com.google.gson.annotations.SerializedName;

public class TodaysParamsModel {

    @SerializedName("company_id")
    String site_id;

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }
}
