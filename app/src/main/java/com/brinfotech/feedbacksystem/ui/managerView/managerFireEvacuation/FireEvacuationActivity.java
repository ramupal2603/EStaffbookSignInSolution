package com.brinfotech.feedbacksystem.ui.managerView.managerFireEvacuation;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;

import butterknife.BindView;

public class FireEvacuationActivity extends BaseActivity {

    @BindView(R.id.rcvFireEvacuationList)
    RecyclerView rcvFireEvacuationList;

    FireEvacuationListAdapter fireEvacuationListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpRecyclerView();

        setUpAdapter();
    }

    private void setUpAdapter() {
        fireEvacuationListAdapter = new FireEvacuationListAdapter(FireEvacuationActivity.this);
        rcvFireEvacuationList.setAdapter(fireEvacuationListAdapter);
    }

    private void setUpRecyclerView() {
        rcvFireEvacuationList.setLayoutManager(new LinearLayoutManager(FireEvacuationActivity.this, RecyclerView.VERTICAL, false));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_fire_evacuation;
    }

    @Override
    public void onClick(View view) {

    }
}
