package com.brinfotech.feedbacksystem.data.staffReport;

import java.util.ArrayList;

public class StaffReportResponseModel {
    String status;
    ArrayList<StaffReportDataModel> visitor_details;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<StaffReportDataModel> getVisitor_details() {
        return visitor_details;
    }

    public void setVisitor_details(ArrayList<StaffReportDataModel> visitor_details) {
        this.visitor_details = visitor_details;
    }
}
