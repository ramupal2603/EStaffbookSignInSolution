package com.brinfotech.feedbacksystem.ui.managerView.managerStaffView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brinfotech.feedbacksystem.R;


import butterknife.BindView;
import butterknife.ButterKnife;

public class StaffReportAdapter extends RecyclerView.Adapter<StaffReportAdapter.DashboardHolder> {


    Activity activity;

    public StaffReportAdapter(Activity activity) {
        this.activity = activity;


    }

    @NonNull
    @Override
    public DashboardHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View v = LayoutInflater.from(activity).inflate(R.layout.cell_staff_report_view, parent, false);
        return new DashboardHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardHolder holder, @SuppressLint("RecyclerView") final int position) {


    }


    @Override
    public int getItemCount() {
        return 100;
    }


    static class DashboardHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtName)
        TextView txtName;

        @BindView(R.id.txtSignInTime)
        TextView txtSignInTime;

        @BindView(R.id.txtSignOutTime)
        TextView txtSignOutTime;

        DashboardHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
