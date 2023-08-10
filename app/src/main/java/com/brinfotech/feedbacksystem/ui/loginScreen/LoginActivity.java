package com.brinfotech.feedbacksystem.ui.loginScreen;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;
import com.brinfotech.feedbacksystem.data.loginData.LoginRequestModel;
import com.brinfotech.feedbacksystem.data.loginData.LoginRequestParamsModel;
import com.brinfotech.feedbacksystem.data.loginData.LoginResponseModel;
import com.brinfotech.feedbacksystem.data.loginData.ManualLogin.ManualLoginRequestDataModel;
import com.brinfotech.feedbacksystem.data.loginData.ManualLogin.ManualLoginRequestModel;
import com.brinfotech.feedbacksystem.helpers.ConstantClass;
import com.brinfotech.feedbacksystem.helpers.PreferenceKeys;
import com.brinfotech.feedbacksystem.helpers.StringUtils;
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

    @BindView(R.id.loutQrCodeLoginView)
    LinearLayout loutQrCodeLoginView;

    @BindView(R.id.loutManualLoginView)
    LinearLayout loutManualLoginView;

    @BindView(R.id.rLoutManualView)
    RelativeLayout rLoutManualView;


    @BindView(R.id.edtUserName)
    EditText edtUserName;

    @BindView(R.id.edtPwd)
    EditText edtPwd;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.txtQrCodeLogin)
    TextView txtQrCodeLogin;

    private static final String[] CAMERA_AND_STORAGE = {Manifest.permission.CAMERA};

    List<BarcodeFormat> arrFormatList = new ArrayList<>();
    private static final int MY_CAMERA_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeScannerView();
        rLoutManualView.setOnClickListener(this::onClick);
        txtQrCodeLogin.setOnClickListener(this::onClick);
        btnLogin.setOnClickListener(this::onClick);

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

    private void resumeCameraPreview() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                qrCodeScanner.resumeCameraPreview(LoginActivity.this);
            }
        }, 5000);

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
        if (view == rLoutManualView) {
            loutQrCodeLoginView.setVisibility(View.GONE);
            loutManualLoginView.setVisibility(View.VISIBLE);
        } else if (view == txtQrCodeLogin) {
            loutQrCodeLoginView.setVisibility(View.VISIBLE);
            loutManualLoginView.setVisibility(View.GONE);
        } else if (view == btnLogin) {
            checkValidation();
        }
    }

    private void checkValidation() {
        if (StringUtils.checkEmptyEditText(edtUserName)) {
            showToastMessage("Please Enter Your Email Address");
        } else if (StringUtils.checkEmptyEditText(edtPwd)) {
            showToastMessage("Please Enter Your Password");
        } else {
            doManualLogin();
        }
    }

    private void doManualLogin() {
        showProgressBar();

        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.manualLogin(getManualLoginRequest()).enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                hideProgressBar();
                if (response.isSuccessful()) {
                    LoginResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS)) {
                        Prefs.putString(PreferenceKeys.USER_ID, responseModel.getVisitor_details().get(0).getUser_id());
                        Prefs.putString(PreferenceKeys.USER_TYPE, responseModel.getVisitor_details().get(0).getUser_type());
                        Prefs.putString(PreferenceKeys.USER_NAME, responseModel.getVisitor_details().get(0).getUser_name());
                        Prefs.putString(PreferenceKeys.SITE_ID, responseModel.getVisitor_details().get(0).getSite_details());
                        Prefs.putString(PreferenceKeys.LOCATION_ID, responseModel.getVisitor_details().get(0).getSite_id());
                        Prefs.putBoolean(PreferenceKeys.USER_LOGGED_IN, true);
                        redirectBasedOnUserType(getActivity());
                    } else if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_CHANGE_LOGIN_PWD)) {
                        Prefs.putString(PreferenceKeys.USER_ID, responseModel.getVisitor_details().get(0).getUser_id());
                        Prefs.putString(PreferenceKeys.USER_TYPE, responseModel.getVisitor_details().get(0).getUser_type());
                        Prefs.putString(PreferenceKeys.USER_NAME, responseModel.getVisitor_details().get(0).getUser_name());
                        Prefs.putString(PreferenceKeys.SITE_ID, responseModel.getVisitor_details().get(0).getSite_details());
                        Prefs.putString(PreferenceKeys.LOCATION_ID, responseModel.getVisitor_details().get(0).getSite_id());
                        edtUserName.setText("");
                        edtPwd.setText("");
                        redirectToChangePasswordScreen(LoginActivity.this);
                    } else if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_USER_ALREADY_LOGGED_IN)) {
                        showToastMessage(getResources().getString(R.string.user_already_logged_in));
                    } else {
                        showToastMessage(getResources().getString(R.string.invalid_credential));
                    }
                } else {
                    showLoginFailedMessage();
                }

            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                t.printStackTrace();
                hideProgressBar();
            }
        });
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
                        Prefs.putString(PreferenceKeys.LOCATION_ID, responseModel.getVisitor_details().get(0).getSite_id());
                        Prefs.putBoolean(PreferenceKeys.USER_LOGGED_IN, true);
                        redirectBasedOnUserType(getActivity());
                    } else {
                        showToastMessage(getString(R.string.invalid_credential));
                        resumeCameraPreview();
                    }
                } else {
                    if (response.code() != 401) {
                        showErrorMessage();
                    } else {
                        showLoginFailedMessage();
                    }
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
        requestParamsModel.setApp_type(WebApiHelper.DEVICE_ANDROID);
        requestParamsModel.setToken(fcmToken);
        requestModel.setParam(requestParamsModel);
        return requestModel;
    }

    private ManualLoginRequestModel getManualLoginRequest() {
        String fcmToken = getFCMRefreshedToken();
        ManualLoginRequestModel requestModel = new ManualLoginRequestModel();
        ManualLoginRequestDataModel manualLoginRequestDataModel = new ManualLoginRequestDataModel();
        manualLoginRequestDataModel.setEmail(edtUserName.getText().toString().trim());
        manualLoginRequestDataModel.setPassword(edtPwd.getText().toString().trim());
        manualLoginRequestDataModel.setToken(fcmToken);
        manualLoginRequestDataModel.setApp_type(WebApiHelper.DEVICE_ANDROID);
        requestModel.setParam(manualLoginRequestDataModel);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantClass.REQUEST_CODE_CHANGE_PASSWORD && resultCode == RESULT_OK) {
            Prefs.putBoolean(PreferenceKeys.USER_LOGGED_IN, true);
            showToastMessage(getResources().getString(R.string.password_changed_successfully));
            redirectBasedOnUserType(LoginActivity.this);
        }
    }
}
