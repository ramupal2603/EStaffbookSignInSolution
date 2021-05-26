package com.brinfotech.feedbacksystem.network;


import com.brinfotech.feedbacksystem.data.changePassword.ChangePasswordRequestModel;
import com.brinfotech.feedbacksystem.data.changePassword.ChangePasswordResponseModel;
import com.brinfotech.feedbacksystem.data.currentVersion.CurrentVersionRequestModel;
import com.brinfotech.feedbacksystem.data.currentVersion.CurrentVersionResponseModel;
import com.brinfotech.feedbacksystem.data.department.DepartmentRequestModel;
import com.brinfotech.feedbacksystem.data.department.DepartmentResponseModel;
import com.brinfotech.feedbacksystem.data.getUserStatus.GetUserStatusRequestModel;
import com.brinfotech.feedbacksystem.data.getUserStatus.GetUserStatusResponseModel;
import com.brinfotech.feedbacksystem.data.importFireEvacuation.ImportFireEvacuationRequestModel;
import com.brinfotech.feedbacksystem.data.importFireEvacuation.ImportFireEvacuationResponseModel;
import com.brinfotech.feedbacksystem.data.loginData.LoginRequestModel;
import com.brinfotech.feedbacksystem.data.loginData.LoginResponseModel;
import com.brinfotech.feedbacksystem.data.loginData.ManualLogin.ManualLoginRequestDataModel;
import com.brinfotech.feedbacksystem.data.loginData.ManualLogin.ManualLoginRequestModel;
import com.brinfotech.feedbacksystem.data.signINOut.ScanQrCodeResponseModel;
import com.brinfotech.feedbacksystem.data.signINOut.SignInOutRequestModel;
import com.brinfotech.feedbacksystem.data.staffReport.StaffReportRequestModel;
import com.brinfotech.feedbacksystem.data.staffReport.StaffReportResponseModel;
import com.brinfotech.feedbacksystem.data.todaysVisitors.TodayVisitorResponseModel;
import com.brinfotech.feedbacksystem.data.todaysVisitors.TodaysRequestModel;
import com.brinfotech.feedbacksystem.network.utils.WebApiHelper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitInterface {

    @POST(WebApiHelper.LOGIN)
    Call<LoginResponseModel> loginFromScanner(@Body LoginRequestModel requestModel);

    @POST(WebApiHelper.GET_USER_STATUS)
    Call<GetUserStatusResponseModel> getUserStatus(@Body GetUserStatusRequestModel requestModel);

    @POST(WebApiHelper.MANUAL_LOGIN)
    Call<LoginResponseModel> manualLogin(@Body ManualLoginRequestModel requestModel);

    @POST(WebApiHelper.SIGN_IN_OUT_QRCODE)
    Call<ScanQrCodeResponseModel> scanQRCodeSignInOut(@Body SignInOutRequestModel requestModel);

    @POST(WebApiHelper.GET_STAFF_REPORT)
    Call<StaffReportResponseModel> getStaffReports(@Body StaffReportRequestModel requestModel);

    @POST(WebApiHelper.GET_TODAY_VISITORS)
    Call<TodayVisitorResponseModel> getTodayVisitors(@Body TodaysRequestModel requestModel);

    @POST(WebApiHelper.IMPORT_FIRE_EVACUATION)
    Call<ImportFireEvacuationResponseModel> importFireEvacuationList(@Body ImportFireEvacuationRequestModel requestModel);

    @POST(WebApiHelper.GET_DEPARTMENT)
    Call<DepartmentResponseModel> getDepartment(@Body DepartmentRequestModel requestModel);

    @POST(WebApiHelper.CHANGE_PASSWORD)
    Call<ChangePasswordResponseModel> changePassword(@Body ChangePasswordRequestModel requestModel);

    @POST(WebApiHelper.GET_VERSION)
    Call<CurrentVersionResponseModel> getCurrentVersion(@Body CurrentVersionRequestModel requestModel);
}
