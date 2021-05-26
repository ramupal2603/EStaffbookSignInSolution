package com.brinfotech.feedbacksystem.ui.staffView.qrCodeScannerView;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;
import com.brinfotech.feedbacksystem.data.getUserStatus.GetUserStatusParamModel;
import com.brinfotech.feedbacksystem.data.getUserStatus.GetUserStatusRequestModel;
import com.brinfotech.feedbacksystem.data.getUserStatus.GetUserStatusResponseModel;
import com.brinfotech.feedbacksystem.data.signINOut.ScanQrCodeResponseModel;
import com.brinfotech.feedbacksystem.data.signINOut.SignInOutParamsModel;
import com.brinfotech.feedbacksystem.data.signINOut.SignInOutRequestModel;
import com.brinfotech.feedbacksystem.helpers.ConstantClass;
import com.brinfotech.feedbacksystem.helpers.PreferenceKeys;
import com.brinfotech.feedbacksystem.network.RetrofitClient;
import com.brinfotech.feedbacksystem.network.RetrofitInterface;
import com.brinfotech.feedbacksystem.network.utils.WebApiHelper;
import com.google.zxing.BarcodeFormat;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrCodeScannerViewActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks {

    @BindView(R.id.rLoutStaffSignIn)
    RelativeLayout rLoutStaffSignIn;

    @BindView(R.id.txtSignIn)
    TextView txtSignIn;

    @BindView(R.id.txtSignOut)
    TextView txtSignOut;

    @BindView(R.id.txtWelcomeUserId)
    TextView txtWelcomeUserId;


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

//        initiateSignedInView();

        getUsersCurrentStatus();

        txtWelcomeUserId.setText(String.format("Hi, %s", Prefs.getString(PreferenceKeys.USER_NAME, "")));

    }

    private void getUsersCurrentStatus() {
        showProgressBar();

        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.getUserStatus(getUsersStatusRequest()).enqueue(new Callback<GetUserStatusResponseModel>() {
            @Override
            public void onResponse(Call<GetUserStatusResponseModel> call, Response<GetUserStatusResponseModel> response) {
                hideProgressBar();
                if (response.isSuccessful()) {
                    GetUserStatusResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus() != null) {
                        initiateSignedInView(responseModel.getStatus());
                    } else {
                        initiateSignedInView("0");
                    }

                } else {
                    showErrorMessage();
                }

            }

            @Override
            public void onFailure(Call<GetUserStatusResponseModel> call, Throwable t) {
                t.printStackTrace();
                hideProgressBar();
            }
        });
    }

    private GetUserStatusRequestModel getUsersStatusRequest() {
        GetUserStatusRequestModel requestModel = new GetUserStatusRequestModel();
        GetUserStatusParamModel paramModel = new GetUserStatusParamModel();
        paramModel.setVisitor_id(Prefs.getString(PreferenceKeys.USER_ID, ""));
        paramModel.setVisitor_type(Prefs.getString(PreferenceKeys.USER_TYPE, ""));
        requestModel.setParam(paramModel);
        return requestModel;

    }

    private void initiateSignedInView(String status) {


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

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        paramsModel.setUser_type(Prefs.getString(PreferenceKeys.USER_TYPE, ""));
        paramsModel.setSite_id(Prefs.getString(PreferenceKeys.LOCATION_ID, "0"));
        requestModel.setParam(paramsModel);

        return requestModel;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == MY_CAMERA_REQUEST_CODE) {

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
