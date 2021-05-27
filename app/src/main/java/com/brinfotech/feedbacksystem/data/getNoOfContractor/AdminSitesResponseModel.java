package com.brinfotech.feedbacksystem.data.getNoOfContractor;

public class AdminSitesResponseModel {
    String status;
    AdminSiteDataResponseModel data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AdminSiteDataResponseModel getData() {
        return data;
    }

    public void setData(AdminSiteDataResponseModel data) {
        this.data = data;
    }
}
