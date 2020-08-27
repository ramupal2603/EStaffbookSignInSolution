package com.brinfotech.feedbacksystem.network;


import com.brinfotech.feedbacksystem.data.importFireEvacuation.ImportFireEvacuationRequestModel;
import com.brinfotech.feedbacksystem.data.importFireEvacuation.ImportFireEvacuationResponseModel;
import com.brinfotech.feedbacksystem.data.loginData.LoginRequestModel;
import com.brinfotech.feedbacksystem.data.loginData.LoginResponseModel;
import com.brinfotech.feedbacksystem.data.signINOut.ScanQrCodeResponseModel;
import com.brinfotech.feedbacksystem.data.signINOut.SignInOutRequestModel;
import com.brinfotech.feedbacksystem.data.staffReport.StaffReportRequestModel;
import com.brinfotech.feedbacksystem.data.staffReport.StaffReportResponseModel;
import com.brinfotech.feedbacksystem.data.todaysVisitors.TodayVisitorResponseModel;
import com.brinfotech.feedbacksystem.data.todaysVisitors.TodaysRequestModel;
import com.brinfotech.feedbacksystem.network.utils.WebApiHelper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST(WebApiHelper.LOGIN)
    Call<LoginResponseModel> loginFromScanner(@Body LoginRequestModel requestModel);

    @POST(WebApiHelper.SIGN_IN_OUT_QRCODE)
    Call<ScanQrCodeResponseModel> scanQRCodeSignInOut(@Body SignInOutRequestModel requestModel);

    @POST(WebApiHelper.GET_STAFF_REPORT)
    Call<StaffReportResponseModel> getStaffReports(@Body StaffReportRequestModel requestModel);

    @POST(WebApiHelper.GET_TODAY_VISITORS)
    Call<TodayVisitorResponseModel> getTodayVisitors(@Body TodaysRequestModel requestModel);

    @POST(WebApiHelper.IMPORT_FIRE_EVACUATION)
    Call<ImportFireEvacuationResponseModel> importFireEvacuationList(@Body ImportFireEvacuationRequestModel requestModel);


}
