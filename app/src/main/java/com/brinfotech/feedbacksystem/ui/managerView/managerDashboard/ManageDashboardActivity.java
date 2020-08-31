package com.brinfotech.feedbacksystem.ui.managerView.managerDashboard;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;
import com.brinfotech.feedbacksystem.helpers.PreferenceKeys;
import com.pixplicity.easyprefs.library.Prefs;

import butterknife.BindView;

public class ManageDashboardActivity extends BaseActivity {
    @BindView(R.id.rLoutStaffView)
    RelativeLayout rLoutStaffView;

    @BindView(R.id.rLoutFireEvacuationView)
    RelativeLayout rLoutFireEvacuationView;

    @BindView(R.id.txtWelcomeUserId)
    TextView txtWelcomeUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rLoutStaffView.setOnClickListener(this);
        rLoutFireEvacuationView.setOnClickListener(this);

        txtWelcomeUserId.setText(String.format("Hi , %s", Prefs.getString(PreferenceKeys.USER_NAME, "")));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_manager_dashboard;
    }

    @Override
    public void onClick(View view) {
        if (view == rLoutStaffView) {
            openStaffReportActivity();
        }if (view == rLoutFireEvacuationView) {
            openFireEvacuationActivity();
        }
    }
}
