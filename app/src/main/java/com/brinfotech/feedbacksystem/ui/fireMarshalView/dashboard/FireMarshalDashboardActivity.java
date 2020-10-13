package com.brinfotech.feedbacksystem.ui.fireMarshalView.dashboard;

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
import com.brinfotech.feedbacksystem.ui.fireMarshalView.qrCodeScannerView.FireMarshalQrCodeScannerViewActivity;
import com.pixplicity.easyprefs.library.Prefs;

import butterknife.BindView;

public class FireMarshalDashboardActivity extends BaseActivity {
    private static final int REQUEST_QR_CODE_SCANNER = 10000;
    @BindView(R.id.rLoutRemoteView)
    RelativeLayout rLoutRemoteView;

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
        return R.layout.activity_fire_marshal_dashboard;
    }

    @Override
    public void onClick(View view) {
        if (view == rLoutFireEvacuationView) {
            openFireMarshalEvacuationActivity();
        }

        if (view == rLoutRemoteView) {
            Intent intent = new Intent(FireMarshalDashboardActivity.this, FireMarshalQrCodeScannerViewActivity.class);
            startActivityForResult(intent, REQUEST_QR_CODE_SCANNER);
        }
    }
}
