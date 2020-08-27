package com.brinfotech.feedbacksystem.ui.managerView.managerFireEvacuation;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.data.todaysVisitors.TodayVisitorDataModel;
import com.brinfotech.feedbacksystem.interfaces.OnStaffSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;


public class FireEvacuationListAdapter extends RecyclerView.Adapter<FireEvacuationListAdapter.FireEvacuationHolder> {


    Activity activity;
    ArrayList<TodayVisitorDataModel> arrTodaysVisitor;
    HashMap<String, String> selectedHashmap;
    OnStaffSelectedListener onStaffSelectedListener;

    public FireEvacuationListAdapter(Activity activity, ArrayList<TodayVisitorDataModel> arrTodaysVisitor,
                                     HashMap<String, String> selectedHashmap,OnStaffSelectedListener onStaffSelectedListener) {
        this.activity = activity;
        this.arrTodaysVisitor = arrTodaysVisitor;
        this.selectedHashmap = selectedHashmap;
        this.onStaffSelectedListener = onStaffSelectedListener;
    }

    @NonNull
    @Override
    public FireEvacuationHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View v = LayoutInflater.from(activity).inflate(R.layout.cell_fire_evacuation, parent, false);
        return new FireEvacuationHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FireEvacuationHolder fireEvacuationHolder, int position) {

        TodayVisitorDataModel arrItem = arrTodaysVisitor.get(position);

        if (selectedHashmap != null && selectedHashmap.containsKey(arrItem.getVisitor_id())) {
            fireEvacuationHolder.chkFilterView.setChecked(true);
        } else {
            fireEvacuationHolder.chkFilterView.setChecked(false);
        }

        fireEvacuationHolder.txtVisitorType.setText(R.string.staff);
        fireEvacuationHolder.txtVisitorName.setText(arrItem.getVisitor_name());
        fireEvacuationHolder.layoutNotificationFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedHashmap.containsKey(arrItem.getVisitor_id())) {
                    selectedHashmap.remove(arrItem.getVisitor_id());
                } else {
                    selectedHashmap.put(arrItem.getVisitor_id(), "true");
                }

                onStaffSelectedListener.OnStaffSelectedListener(selectedHashmap);

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrTodaysVisitor.size();
    }

    public void updateData(ArrayList<TodayVisitorDataModel> arrTodaysVisitor) {
        this.arrTodaysVisitor = arrTodaysVisitor;
        notifyDataSetChanged();
    }


    static class FireEvacuationHolder extends RecyclerView.ViewHolder {

        TextView txtVisitorName;
        TextView txtVisitorType;
        CheckBox chkFilterView;
        LinearLayout layoutNotificationFilter;

        FireEvacuationHolder(@NonNull View itemView) {
            super(itemView);
            this.txtVisitorName = itemView.findViewById(R.id.txtNotificationFilter);
            this.txtVisitorType = itemView.findViewById(R.id.txtVisitorType);
            this.chkFilterView = itemView.findViewById(R.id.chkFilterView);
            this.layoutNotificationFilter = itemView.findViewById(R.id.layoutNotificationFilter);
        }
    }
}
