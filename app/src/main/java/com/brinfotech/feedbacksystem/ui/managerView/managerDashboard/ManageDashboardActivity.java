package com.brinfotech.feedbacksystem.ui.managerView.managerDashboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;
import com.brinfotech.feedbacksystem.helpers.PreferenceKeys;
import com.brinfotech.feedbacksystem.ui.managerView.qrCodeScannerView.ManagerQrCodeScannerViewActivity;
import com.pixplicity.easyprefs.library.Prefs;

import butterknife.BindView;

public class ManageDashboardActivity extends BaseActivity {
    private static final int REQUEST_QR_CODE_SCANNER = 10000;
    @BindView(R.id.rLoutRemoteView)
    RelativeLayout rLoutRemoteView;

    @BindView(R.id.rLoutStaffView)
    RelativeLayout rLoutStaffView;

    @BindView(R.id.rLoutFireEvacuationView)
    RelativeLayout rLoutFireEvacuationView;

    @BindView(R.id.imgQrCodeView)
    ImageView imgQrCodeView;

    @BindView(R.id.txtWelcomeUserId)
    TextView txtWelcomeUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rLoutRemoteView.setOnClickListener(this);
        rLoutStaffView.setOnClickListener(this);
        rLoutFireEvacuationView.setOnClickListener(this);

        Bitmap qrCode = generateQRCode(Prefs.getString(PreferenceKeys.USER_ID, ""));
        displayQRCode(qrCode);

        txtWelcomeUserId.setText(String.format("Hi, %s", Prefs.getString(PreferenceKeys.USER_NAME, "")));
    }

    private void displayQRCode(Bitmap qrCode) {
        if (qrCode != null) {
            imgQrCodeView.setImageBitmap(qrCode);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_manager_dashboard;
    }

    @Override
    public void onClick(View view) {
        if (view == rLoutStaffView) {
            openStaffReportActivity();
        }
        if (view == rLoutFireEvacuationView) {
            openFireMarshalEvacuationActivity();
        }

        if (view == rLoutRemoteView) {
            Intent intent = new Intent(ManageDashboardActivity.this, ManagerQrCodeScannerViewActivity.class);
            startActivityForResult(intent, REQUEST_QR_CODE_SCANNER);
        }
    }
}
