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




public class FireEvacuationListAdapter extends RecyclerView.Adapter<FireEvacuationListAdapter.FireEvacuationHolder> {


    Activity activity;


    public FireEvacuationListAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public FireEvacuationHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View v = LayoutInflater.from(activity).inflate(R.layout.cell_fire_evacuation, parent, false);
        return new FireEvacuationHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FireEvacuationHolder fireEvacuationHolder, int position) {

    }

    @Override
    public int getItemCount() {
        return 100;
    }


    static class FireEvacuationHolder extends RecyclerView.ViewHolder {

        TextView txtNotificationFilter;
        TextView txtVisitorType;
        CheckBox chkFilterView;
        LinearLayout layoutNotificationFilter;

        FireEvacuationHolder(@NonNull View itemView) {
            super(itemView);
            this.txtNotificationFilter = itemView.findViewById(R.id.txtNotificationFilter);
            this.txtVisitorType = itemView.findViewById(R.id.txtVisitorType);
            this.chkFilterView = itemView.findViewById(R.id.chkFilterView);
            this.layoutNotificationFilter = itemView.findViewById(R.id.layoutNotificationFilter);
        }
    }
}
