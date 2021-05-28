package com.brinfotech.feedbacksystem.ui.adminView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.data.getNoOfContractor.AdminSiteListModel;
import com.brinfotech.feedbacksystem.interfaces.OnSiteSelectedListener;

import java.util.ArrayList;


public class SiteSelectionListAdapter extends RecyclerView.Adapter<SiteSelectionListAdapter.DashboardHolder> {


    Activity activity;
    OnSiteSelectedListener onSiteSelectedListener;
    ArrayList<AdminSiteListModel> arrSiteList;

    public SiteSelectionListAdapter(Activity activity, OnSiteSelectedListener onSiteSelectedListener,
                                    ArrayList<AdminSiteListModel> arrSiteList) {
        this.activity = activity;
        this.arrSiteList = arrSiteList;
        this.onSiteSelectedListener = onSiteSelectedListener;

    }

    @NonNull
    @Override
    public DashboardHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View v = LayoutInflater.from(activity).inflate(R.layout.cell_site_selection_list, parent, false);
        return new DashboardHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardHolder holder, final int position) {
        final AdminSiteListModel arrItem = arrSiteList.get(position);

        holder.txtSiteName.setText(arrItem.getSite_name());
        holder.txtContractorSite.setText(String.format("%s People are on site", arrItem.getVisitor_count()));

        if (position % 2 == 0) {
            holder.loutSiteSelectionView.setBackgroundColor(activity.getResources().getColor(R.color.whiteColor));
        } else {
            holder.loutSiteSelectionView.setBackgroundColor(activity.getResources().getColor(R.color.lightPinkColor));
        }

        holder.loutSiteSelectionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSiteSelectedListener.openForm(position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return arrSiteList.size();
    }

    public void updateData(ArrayList<AdminSiteListModel> arrSiteList) {
        this.arrSiteList = arrSiteList;
        notifyDataSetChanged();
    }


    static class DashboardHolder extends RecyclerView.ViewHolder {

        ImageView imgEye;
        TextView txtSiteName;
        TextView txtContractorSite;
        LinearLayout loutSiteSelectionView;


        DashboardHolder(@NonNull View itemView) {
            super(itemView);
            txtSiteName = itemView.findViewById(R.id.txtSiteName);
            txtContractorSite = itemView.findViewById(R.id.txtContractorSite);
            imgEye = itemView.findViewById(R.id.imgEye);
            loutSiteSelectionView = itemView.findViewById(R.id.loutSiteSelectionView);

        }
    }
}
