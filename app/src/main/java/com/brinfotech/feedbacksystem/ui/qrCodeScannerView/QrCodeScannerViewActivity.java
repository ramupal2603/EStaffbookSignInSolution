package com.brinfotech.feedbacksystem.ui.qrCodeScannerView;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import pub.devrel.easypermissions.EasyPermissions;

public class QrCodeScannerViewActivity extends BaseActivity implements View.OnClickListener, ZXingScannerView.ResultHandler, EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks {

    @BindView(R.id.qrCodeScannerView)
    ZXingScannerView qrCodeScanner;

    private static final String[] CAMERA_AND_STORAGE =
            {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE};

    List<BarcodeFormat> arrFormatList = new ArrayList<>();
    private static final int MY_CAMERA_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeScannerView();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!EasyPermissions.hasPermissions(QrCodeScannerViewActivity.this, CAMERA_AND_STORAGE)) {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_camera),
                    MY_CAMERA_REQUEST_CODE,
                    CAMERA_AND_STORAGE);
        } else {
            qrCodeScanner.startCamera();
            qrCodeScanner.setResultHandler(QrCodeScannerViewActivity.this);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initializeScannerView() {
        arrFormatList.add(BarcodeFormat.QR_CODE);
        qrCodeScanner.setFormats(arrFormatList);
        qrCodeScanner.setAutoFocus(true);
        qrCodeScanner.setLaserColor(R.color.colorAccent);
        qrCodeScanner.setMaskColor(R.color.colorAccent);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_scan_qr_code;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeScanner.stopCamera();
    }

    private void callLoginMethod(String userName, String userPassword) {
    }

    @Override
    public void handleResult(Result result) {

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            qrCodeScanner.startCamera();
            qrCodeScanner.setResultHandler(QrCodeScannerViewActivity.this);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onRationaleAccepted(int requestCode) {

    }

    @Override
    public void onRationaleDenied(int requestCode) {

    }
}
