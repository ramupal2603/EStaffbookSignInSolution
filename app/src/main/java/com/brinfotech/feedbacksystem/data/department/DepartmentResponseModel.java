package com.brinfotech.feedbacksystem.data.department;

import java.util.ArrayList;

public class DepartmentResponseModel {

    String status;
    ArrayList<DepartmentResponseModelData> department_details;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<DepartmentResponseModelData> getDepartment_details() {
        return department_details;
    }

    public void setDepartment_details(ArrayList<DepartmentResponseModelData> department_details) {
        this.department_details = department_details;
    }
}
