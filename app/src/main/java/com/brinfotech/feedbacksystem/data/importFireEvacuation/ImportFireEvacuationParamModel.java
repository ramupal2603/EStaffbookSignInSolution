package com.brinfotech.feedbacksystem.data.importFireEvacuation;

import java.util.ArrayList;

public class ImportFireEvacuationParamModel {
    String user_id;
    ArrayList<ImportFireEvacuationVisitorModel> visitor_details;

    public ArrayList<ImportFireEvacuationVisitorModel> getVisitor_details() {
        return visitor_details;
    }

    public void setVisitor_details(ArrayList<ImportFireEvacuationVisitorModel> visitor_details) {
        this.visitor_details = visitor_details;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
