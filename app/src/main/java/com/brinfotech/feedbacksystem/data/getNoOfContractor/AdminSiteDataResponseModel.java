package com.brinfotech.feedbacksystem.data.getNoOfContractor;

import java.util.List;

public class AdminSiteDataResponseModel {
    List<AdminSiteListModel> site_details;

    public List<AdminSiteListModel> getSite_details() {
        return site_details;
    }

    public void setSite_details(List<AdminSiteListModel> site_details) {
        this.site_details = site_details;
    }
}
