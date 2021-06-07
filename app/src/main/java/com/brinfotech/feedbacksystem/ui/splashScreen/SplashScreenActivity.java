package com.brinfotech.feedbacksystem.ui.splashScreen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.brinfotech.feedbacksystem.BuildConfig;
import com.brinfotech.feedbacksystem.MyApplication;
import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;
import com.brinfotech.feedbacksystem.data.currentVersion.CurrentVersionRequestModel;
import com.brinfotech.feedbacksystem.data.currentVersion.CurrentVersionRequestParamModel;
import com.brinfotech.feedbacksystem.data.currentVersion.CurrentVersionResponseModel;
import com.brinfotech.feedbacksystem.helpers.ConstantClass;
import com.brinfotech.feedbacksystem.jobQueue.StartUpApplicationJob;
import com.brinfotech.feedbacksystem.network.RetrofitInterface;
import com.brinfotech.feedbacksystem.network.VersionRetrofitClient;
import com.brinfotech.feedbacksystem.network.utils.WebApiHelper;
import com.brinfotech.feedbacksystem.ui.loginScreen.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAndCompareCurrentVersion();
    }

    private void getAndCompareCurrentVersion() {
        RetrofitInterface apiService = VersionRetrofitClient.getVersionRetrofit().create(RetrofitInterface.class);
        apiService.getCurrentVersion(getRequestParamsForCurrentVersion()).
                enqueue(new Callback<CurrentVersionResponseModel>() {
                    @Override
                    public void onResponse(Call<CurrentVersionResponseModel> call, Response<CurrentVersionResponseModel> response) {
                        if (response.isSuccessful()) {
                            CurrentVersionResponseModel responseModel = response.body();
                            if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS)) {
                                WebApiHelper.BASE_URL = responseModel.getUrl();
                                compareCurrentVersion(responseModel.getVersion());
                            } else {
                                WebApiHelper.BASE_URL = WebApiHelper.VERSION_BASE_URL;
                                redirectDashboardActivity();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CurrentVersionResponseModel> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    private CurrentVersionRequestModel getRequestParamsForCurrentVersion() {
        CurrentVersionRequestModel requestModel = new CurrentVersionRequestModel();
        CurrentVersionRequestParamModel paramModel = new CurrentVersionRequestParamModel();
        paramModel.setMobile_app(WebApiHelper.DEVICE_ANDROID);
        requestModel.setParam(paramModel);
        return requestModel;
    }

    private void compareCurrentVersion(String currentVersion) {
        String versionName = BuildConfig.VERSION_NAME;
        if (versionName.equals(currentVersion)) {
            redirectDashboardActivity();
        } else {
            showUpdatePopUpDialog(currentVersion);
        }

    }

    private void showUpdatePopUpDialog(String latestVersion) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashScreenActivity.this);

        alertDialogBuilder.setTitle(getString(R.string.youAreNotUpdatedTitle));
        alertDialogBuilder.setMessage(getString(R.string.youAreNotUpdatedMessage) + " " + latestVersion + " " + getString(R.string.youAreNotUpdatedMessage1));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                dialog.cancel();
            }
        });
        alertDialogBuilder.show();

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash_screen;
    }

    private void redirectDashboardActivity() {
        MyApplication.getInstance().getMainJobManager().addJobInBackground(new StartUpApplicationJob());
        if (isUserLoggedIn()) {
            redirectBasedOnUserType(getActivity());
        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View view) {

    }
}
