package com.brinfotech.feedbacksystem.ui.staffView.qrCodeScannerView;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;
import com.brinfotech.feedbacksystem.data.signINOut.ScanQrCodeResponseModel;
import com.brinfotech.feedbacksystem.data.signINOut.SignInOutParamsModel;
import com.brinfotech.feedbacksystem.data.signINOut.SignInOutRequestModel;
import com.brinfotech.feedbacksystem.helpers.ConstantClass;
import com.brinfotech.feedbacksystem.helpers.PreferenceKeys;
import com.brinfotech.feedbacksystem.network.RetrofitClient;
import com.brinfotech.feedbacksystem.network.RetrofitInterface;
import com.brinfotech.feedbacksystem.network.utils.NetworkUtils;
import com.brinfotech.feedbacksystem.network.utils.WebApiHelper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrCodeScannerViewActivity extends BaseActivity implements View.OnClickListener, ZXingScannerView.ResultHandler, EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks {

    @BindView(R.id.qrCodeScannerView)
    ZXingScannerView qrCodeScanner;

    @BindView(R.id.rLoutStaffSignIn)
    RelativeLayout rLoutStaffSignIn;

    @BindView(R.id.txtSignIn)
    TextView txtSignIn;

    @BindView(R.id.txtSignOut)
    TextView txtSignOut;


    @BindView(R.id.rLoutStaffSignOut)
    RelativeLayout rLoutStaffSignOut;

    private static final String[] CAMERA_AND_STORAGE =
            {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE};

    List<BarcodeFormat> arrFormatList = new ArrayList<>();
    private static final int MY_CAMERA_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeScannerView();

        initiateSignedInView();

    }

    private void initiateSignedInView() {

        String status = Prefs.getString(PreferenceKeys.SCAN_STATUS, "0");


        if (status.equals("0") || status.equals(ConstantClass.RESPONSE_SUCCESS_SIGN_OUT)) {
            txtSignIn.setTextColor(getResources().getColor(R.color.colorBlack));
            txtSignOut.setTextColor(getResources().getColor(R.color.grayColor));
            rLoutStaffSignIn.setOnClickListener(this::onClick);
            rLoutStaffSignOut.setOnClickListener(null);
        } else if (status.equals(ConstantClass.RESPONSE_SUCCESS_SIGN_IN)) {
            txtSignIn.setTextColor(getResources().getColor(R.color.grayColor));
            txtSignOut.setTextColor(getResources().getColor(R.color.colorBlack));
            rLoutStaffSignIn.setOnClickListener(null);
            rLoutStaffSignOut.setOnClickListener(this::onClick);
        }
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

        if (view == rLoutStaffSignIn) {
            String userID = Prefs.getString(PreferenceKeys.USER_ID, "");
            callSignInOutMethod(userID);
        }

        if (view == rLoutStaffSignOut) {
            String userID = Prefs.getString(PreferenceKeys.USER_ID, "");
            callSignInOutMethod(userID);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeScanner.stopCamera();
    }


    @Override
    public void handleResult(Result result) {
        String scannedId = result.getText();
        if (!scannedId.isEmpty()) {
            if (NetworkUtils.isNetworkConnected(getContext())) {
                callSignInOutMethod(scannedId);
            } else {
                showNoNetworkMessage();
            }

        }
    }

    private void callSignInOutMethod(String scannedId) {
        printLogMessage("userID", "" + scannedId);

        showProgressBar();

        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.scanQRCodeSignInOut(getSignInOutRequest(scannedId)).enqueue(new Callback<ScanQrCodeResponseModel>() {
            @Override
            public void onResponse(Call<ScanQrCodeResponseModel> call, Response<ScanQrCodeResponseModel> response) {
                hideProgressBar();
                if (response.isSuccessful()) {
                    ScanQrCodeResponseModel responseModel = response.body();
                    if (responseModel != null)
                        if (responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS_SIGN_IN)
                                || responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS_SIGN_OUT)) {
                            openThankYouActivity(responseModel.getStatus());
                            Prefs.putString(PreferenceKeys.SCAN_STATUS, responseModel.getStatus());
                            finish();
                        }
                } else {
                    showErrorMessage();
                }

            }

            @Override
            public void onFailure(Call<ScanQrCodeResponseModel> call, Throwable t) {
                t.printStackTrace();
                hideProgressBar();
            }
        });
    }

    private SignInOutRequestModel getSignInOutRequest(String scannedId) {
        SignInOutRequestModel requestModel = new SignInOutRequestModel();
        SignInOutParamsModel paramsModel = new SignInOutParamsModel();
        paramsModel.setUser_id(scannedId);
        paramsModel.setDevice_type(WebApiHelper.DEVICE_TYPE_MOBILE);
        requestModel.setParam(paramsModel);

        return requestModel;
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
