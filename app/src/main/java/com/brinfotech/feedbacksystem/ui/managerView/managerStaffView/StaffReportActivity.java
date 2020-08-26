package com.brinfotech.feedbacksystem.ui.managerView.managerStaffView;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;

import butterknife.BindView;

public class StaffReportActivity extends BaseActivity {

    @BindView(R.id.rcvStaffReport)
    RecyclerView rcvStaffReport;

    StaffReportAdapter staffReportAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpRecyclerView();

        setUpAdapter();
    }

    private void setUpAdapter() {
        staffReportAdapter = new StaffReportAdapter(StaffReportActivity.this);
        rcvStaffReport.setAdapter(staffReportAdapter);
    }

    private void setUpRecyclerView() {
        rcvStaffReport.setLayoutManager(new LinearLayoutManager(StaffReportActivity.this, RecyclerView.VERTICAL, false));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_staff_report;
    }

    @Override
    public void onClick(View view) {

    }
}
