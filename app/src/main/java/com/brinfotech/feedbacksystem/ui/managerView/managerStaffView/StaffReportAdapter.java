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
import com.brinfotech.feedbacksystem.data.staffReport.StaffReportDataModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StaffReportAdapter extends RecyclerView.Adapter<StaffReportAdapter.DashboardHolder> {


    Activity activity;
    ArrayList<StaffReportDataModel> arrVisitorDetails;

    public StaffReportAdapter(Activity activity, ArrayList<StaffReportDataModel> arrVisitorDetails) {
        this.activity = activity;
        this.arrVisitorDetails = arrVisitorDetails;
    }

    @NonNull
    @Override
    public DashboardHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View v = LayoutInflater.from(activity).inflate(R.layout.cell_staff_report_view, parent, false);
        return new DashboardHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardHolder holder, @SuppressLint("RecyclerView") final int position) {

        StaffReportDataModel arrItem = arrVisitorDetails.get(position);
        holder.txtName.setText(arrItem.getStaff_name());
        holder.txtSignInTime.setText(arrItem.getTime_in());
        holder.txtSignOutTime.setText(arrItem.getTime_out());

    }


    @Override
    public int getItemCount() {
        return arrVisitorDetails.size();
    }


    void updateData(ArrayList<StaffReportDataModel> arrStaffReport){
        this.arrVisitorDetails=arrStaffReport;
        notifyDataSetChanged();

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
