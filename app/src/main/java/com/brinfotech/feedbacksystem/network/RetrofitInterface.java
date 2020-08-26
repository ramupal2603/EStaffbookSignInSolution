package com.brinfotech.feedbacksystem.network;


import com.brinfotech.feedbacksystem.data.loginData.LoginRequestModel;
import com.brinfotech.feedbacksystem.data.loginData.LoginResponseModel;
import com.brinfotech.feedbacksystem.data.signINOut.ScanQrCodeResponseModel;
import com.brinfotech.feedbacksystem.data.signINOut.SignInOutRequestModel;
import com.brinfotech.feedbacksystem.network.utils.WebApiHelper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST(WebApiHelper.LOGIN)
    Call<LoginResponseModel> loginFromScanner(@Body LoginRequestModel requestModel);

    @POST(WebApiHelper.SIGN_IN_OUT_QRCODE)
    Call<ScanQrCodeResponseModel> scanQRCodeSignInOut(@Body SignInOutRequestModel requestModel);


}
