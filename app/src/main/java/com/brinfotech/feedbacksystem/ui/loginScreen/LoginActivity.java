package com.brinfotech.feedbacksystem.ui.loginScreen;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;
import com.brinfotech.feedbacksystem.data.loginData.LoginRequestModel;
import com.brinfotech.feedbacksystem.data.loginData.LoginRequestParamsModel;
import com.brinfotech.feedbacksystem.data.loginData.LoginResponseModel;
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

public class LoginActivity extends BaseActivity implements View.OnClickListener, ZXingScannerView.ResultHandler, EasyPermissions.PermissionCallbacks,
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

        //startActivityAfterSeconds();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!EasyPermissions.hasPermissions(LoginActivity.this, CAMERA_AND_STORAGE)) {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_camera),
                    MY_CAMERA_REQUEST_CODE,
                    CAMERA_AND_STORAGE);
        } else {
            qrCodeScanner.startCamera();
            qrCodeScanner.setResultHandler(LoginActivity.this);
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
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeScanner.stopCamera();
    }

    private void callLoginMethod(String userId) {
        printLogMessage("userID", "" + userId);

        showProgressBar();

        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.loginFromScanner(getLoginRequest(userId)).enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                hideProgressBar();
                if (response.isSuccessful()) {
                    LoginResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS)) {
                        Prefs.putString(PreferenceKeys.USER_ID, userId);
                        Prefs.putString(PreferenceKeys.USER_TYPE, responseModel.getVisitor_details().get(0).getUser_type());
                        Prefs.putString(PreferenceKeys.USER_NAME, responseModel.getVisitor_details().get(0).getUser_name());
                        Prefs.putString(PreferenceKeys.SITE_ID, responseModel.getVisitor_details().get(0).getSite_details());
                        Prefs.putBoolean(PreferenceKeys.USER_LOGGED_IN, true);
                        redirectBasedOnUserType(getActivity());
                    }
                } else {
                    showErrorMessage();
                }

            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                t.printStackTrace();
                hideProgressBar();
            }
        });
    }

    private LoginRequestModel getLoginRequest(String userId) {
        String fcmToken = getFCMRefreshedToken();
        LoginRequestModel requestModel = new LoginRequestModel();
        LoginRequestParamsModel requestParamsModel = new LoginRequestParamsModel();
        requestParamsModel.setUser_id(userId);
        requestParamsModel.setUser_type(WebApiHelper.DEVICE_ANDROID);
        requestParamsModel.setToken(fcmToken);
        requestModel.setParam(requestParamsModel);
        return requestModel;
    }

    @Override
    public void handleResult(Result result) {
        String scannedId = result.getText();
        if (!scannedId.isEmpty()) {
            if (NetworkUtils.isNetworkConnected(getContext())) {
                callLoginMethod(scannedId);
            } else {
                showNoNetworkMessage();
            }

        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            qrCodeScanner.startCamera();
            qrCodeScanner.setResultHandler(LoginActivity.this);
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
