package com.brinfotech.feedbacksystem.data.todaysVisitors;

import java.util.ArrayList;

public class TodayVisitorResponseModel {
    String status;
    ArrayList<TodayVisitorDataModel> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<TodayVisitorDataModel> getData() {
        return data;
    }

    public void setData(ArrayList<TodayVisitorDataModel> data) {
        this.data = data;
    }
}
