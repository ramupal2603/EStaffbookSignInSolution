package com.brinfotech.feedbacksystem.ui.staffView.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;
import com.brinfotech.feedbacksystem.ui.qrCodeScannerView.QrCodeScannerViewActivity;

import butterknife.BindView;

public class StaffDashboardActivity extends BaseActivity {


    @BindView(R.id.rLoutStaffView)
    RelativeLayout rLoutStaffView;

    int REQUEST_QR_CODE_SCANNER;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rLoutStaffView.setOnClickListener(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_staff_view;
    }

    @Override
    public void onClick(View view) {
        if (view == rLoutStaffView) {
            Intent intent = new Intent(StaffDashboardActivity.this, QrCodeScannerViewActivity.class);
            startActivityForResult(intent, REQUEST_QR_CODE_SCANNER);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
