package com.brinfotech.feedbacksystem.ui.changePasswordView;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;
import com.brinfotech.feedbacksystem.data.changePassword.ChangePasswordRequestModel;
import com.brinfotech.feedbacksystem.data.changePassword.ChangePasswordRequestParamModel;
import com.brinfotech.feedbacksystem.data.changePassword.ChangePasswordResponseModel;
import com.brinfotech.feedbacksystem.helpers.ConstantClass;
import com.brinfotech.feedbacksystem.helpers.PasswordStrength;
import com.brinfotech.feedbacksystem.helpers.PreferenceKeys;
import com.brinfotech.feedbacksystem.helpers.StringUtils;
import com.brinfotech.feedbacksystem.network.RetrofitClient;
import com.brinfotech.feedbacksystem.network.RetrofitInterface;
import com.brinfotech.feedbacksystem.network.utils.NetworkUtils;
import com.pixplicity.easyprefs.library.Prefs;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.edtNewPassword)
    EditText edtNewPassword;

    @BindView(R.id.edtConfirmPassword)
    EditText edtConfirmPassword;

    @BindView(R.id.btnUpdate)
    Button btnUpdate;

    @BindView(R.id.imgToggleView)
    ImageView imgToggleView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btnUpdate.setOnClickListener(this::onClick);
        imgToggleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtNewPassword.getTransformationMethod().getClass().getSimpleName().equals("PasswordTransformationMethod")) {
                    imgToggleView.setImageResource(R.drawable.ic_eye);
                    edtNewPassword.setTransformationMethod(new SingleLineTransformationMethod());
                } else {
                    imgToggleView.setImageResource(R.drawable.ic_eye_closed);
                    edtNewPassword.setTransformationMethod(new PasswordTransformationMethod());
                }

                edtNewPassword.setSelection(edtNewPassword.getText().length());
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_change_password_view;
    }

    @Override
    public void onClick(View view) {
        if (view == btnUpdate) {
            validatePasswordView();
        }
    }

    private void validatePasswordView() {
        if (!StringUtils.checkEmptyEditText(edtNewPassword)) {
            PasswordStrength passwordStrength = calculatePasswordStrength(edtNewPassword.getText().toString().trim());
            if (passwordStrength != PasswordStrength.VERY_STRONG) {
                showToastMessage(getString(R.string.err_msg_strong_pwd));
            } else if (StringUtils.checkEmptyEditText(edtConfirmPassword)) {
                showToastMessage(getString(R.string.error_msg_confirm_password_empty));
            } else if (!StringUtils.checkTwoEditText(edtNewPassword, edtConfirmPassword)) {
                showToastMessage(getString(R.string.error_msg_confirm_pwd_match));
            } else {
                callUpdatePasswordAPI(edtNewPassword.getText().toString().trim());
            }
        } else {
            showToastMessage("Please enter new password");
        }

    }

    private void callUpdatePasswordAPI(String changedPassword) {
        if (NetworkUtils.isNetworkConnected(getContext())) {
            showProgressBar();

            RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
            apiService.changePassword(getChangePasswordRequestModel(changedPassword)).enqueue(new Callback<ChangePasswordResponseModel>() {
                @Override
                public void onResponse(Call<ChangePasswordResponseModel> call, Response<ChangePasswordResponseModel> response) {
                    if (response.isSuccessful()) {
                        ChangePasswordResponseModel responseModel = response.body();
                        if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS)) {
                            showToastMessage(getResources().getString(R.string.password_changed_successfully));
                            redirectBasedOnUserType(ChangePasswordActivity.this);
                        } else {
                            showToastMessage(getResources().getString(R.string.something_went_wrong));
                        }
                    }
                    hideProgressBar();
                }

                @Override
                public void onFailure(Call<ChangePasswordResponseModel> call, Throwable t) {
                    t.printStackTrace();
                    hideProgressBar();
                }
            });
        } else {
            showToastMessage(getResources().getString(R.string.no_internet_connection));
        }

    }

    private ChangePasswordRequestModel getChangePasswordRequestModel(String changedPassword) {
        ChangePasswordRequestModel changePasswordRequestModel = new ChangePasswordRequestModel();
        ChangePasswordRequestParamModel paramModel = new ChangePasswordRequestParamModel();
        paramModel.setUser_id(Prefs.getString(PreferenceKeys.USER_ID, ""));
        paramModel.setPassword(changedPassword);
        changePasswordRequestModel.setParam(paramModel);
        return changePasswordRequestModel;
    }

    private PasswordStrength calculatePasswordStrength(String str) {
        return PasswordStrength.calculate(str);
    }
}
