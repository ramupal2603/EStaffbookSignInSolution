package com.brinfotech.feedbacksystem.ui.staffView.dashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;
import com.brinfotech.feedbacksystem.helpers.PreferenceKeys;
import com.brinfotech.feedbacksystem.ui.staffView.qrCodeScannerView.QrCodeScannerViewActivity;
import com.pixplicity.easyprefs.library.Prefs;

import butterknife.BindView;

public class StaffDashboardActivity extends BaseActivity {


    @BindView(R.id.rLoutStaffView)
    RelativeLayout rLoutStaffView;

    @BindView(R.id.imgQrCodeView)
    ImageView imgQrCodeView;

    @BindView(R.id.txtWelcomeUserId)
    TextView txtWelcomeUserId;

    int REQUEST_QR_CODE_SCANNER;

    private static final int REQUEST_NOTIFICATION_PERMISSION = 123;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bitmap qrCode = generateQRCode(Prefs.getString(PreferenceKeys.USER_ID, ""));
        displayQRCode(qrCode);

        rLoutStaffView.setOnClickListener(this);

        txtWelcomeUserId.setText(String.format("Hi, %s", Prefs.getString(PreferenceKeys.USER_NAME, "")));
    }

    private void displayQRCode(Bitmap qrCode) {
        if (qrCode != null) {
            imgQrCodeView.setImageBitmap(qrCode);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        askNotificationPermission();
    }

    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_NOTIFICATION_PERMISSION);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can show notifications
            } else {
                // Permission denied, handle accordingly
            }
        }
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
