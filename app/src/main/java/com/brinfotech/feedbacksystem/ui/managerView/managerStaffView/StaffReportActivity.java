package com.brinfotech.feedbacksystem.ui.managerView.managerStaffView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;
import com.brinfotech.feedbacksystem.data.staffReport.StaffReportDataModel;
import com.brinfotech.feedbacksystem.data.staffReport.StaffReportParamsModel;
import com.brinfotech.feedbacksystem.data.staffReport.StaffReportRequestModel;
import com.brinfotech.feedbacksystem.data.staffReport.StaffReportResponseModel;
import com.brinfotech.feedbacksystem.helpers.ConstantClass;
import com.brinfotech.feedbacksystem.helpers.PreferenceKeys;
import com.brinfotech.feedbacksystem.network.RetrofitClient;
import com.brinfotech.feedbacksystem.network.RetrofitInterface;
import com.brinfotech.feedbacksystem.network.utils.NetworkUtils;
import com.brinfotech.feedbacksystem.network.utils.WebApiHelper;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffReportActivity extends BaseActivity {

    @BindView(R.id.rcvStaffReport)
    RecyclerView rcvStaffReport;

    @BindView(R.id.txtRemotelyWorkingCounter)
    TextView txtRemotelyWorkingCounter;

    @BindView(R.id.txtOfficeWorkingCounter)
    TextView txtOfficeWorkingCounter;

    @BindView(R.id.loutRemoteView)
    LinearLayout loutRemoteView;

    @BindView(R.id.loutAllView)
    LinearLayout loutAllView;

    @BindView(R.id.loutVisitor)
    LinearLayout loutVisitor;

    @BindView(R.id.txtAllCounter)
    TextView txtAllCounter;

    @BindView(R.id.txtVisitorCounter)
    TextView txtVisitorCounter;

    @BindView(R.id.loutOfficeView)
    LinearLayout loutOfficeView;

    StaffReportAdapter staffReportAdapter;

    ArrayList<StaffReportDataModel> arrStaffReport = new ArrayList<>();
    ArrayList<StaffReportDataModel> arrFilteredData = new ArrayList<>();

    ArrayList<StaffReportDataModel> arrRemoteWorkData = new ArrayList<>();
    ArrayList<StaffReportDataModel> arrVisitorsData = new ArrayList<>();
    ArrayList<StaffReportDataModel> arrOfficeWorkData = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpRecyclerView();

        getStaffReports();

        setUpAdapter(arrStaffReport);

        loutOfficeView.setOnClickListener(this::onClick);
        loutRemoteView.setOnClickListener(this::onClick);
        loutAllView.setOnClickListener(this::onClick);
        loutVisitor.setOnClickListener(this::onClick);
    }

    private void getStaffReports() {
        if (NetworkUtils.isNetworkConnected(getContext())) {
            showProgressBar();

            RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
            apiService.getStaffReports(getStaffReportRequest()).enqueue(new Callback<StaffReportResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<StaffReportResponseModel> call, @NonNull Response<StaffReportResponseModel> response) {
                    hideProgressBar();
                    if (response.isSuccessful()) {
                        StaffReportResponseModel responseModel = response.body();
                        if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS)) {
                            arrStaffReport.clear();
                            arrStaffReport.addAll(responseModel.getVisitor_details());
                        }
                        updateView(false, arrStaffReport);
                    }
                }

                @Override
                public void onFailure(Call<StaffReportResponseModel> call, Throwable t) {
                    hideProgressBar();
                    t.printStackTrace();
                }
            });
        } else {
            showNoNetworkMessage();
        }
    }

    private void updateView(boolean isFiltered, ArrayList<StaffReportDataModel> arrStaffReport) {
        arrFilteredData.clear();
        arrFilteredData.addAll(arrStaffReport);

        if (staffReportAdapter != null) {
            staffReportAdapter.updateData(arrStaffReport);
        }

        if (arrStaffReport.isEmpty()) {
            hideResultView();
        } else {
            showResultView();
        }
        if (!isFiltered) {
            showCountValues();
        }
    }

    private void showCountValues() {
        arrRemoteWorkData.clear();
        arrOfficeWorkData.clear();

        for (StaffReportDataModel arrItem : arrStaffReport) {
            if (arrItem.getDevice_type().equals(WebApiHelper.DEVICE_TYPE_TAB) &&
                    !arrItem.getVisitor_type().equals(WebApiHelper.USER_TYPE_PRE_BOOK_VISITOR)) {
                arrOfficeWorkData.add(arrItem);
            } else if (arrItem.getDevice_type().equals(WebApiHelper.DEVICE_TYPE_MOBILE)) {
                arrRemoteWorkData.add(arrItem);
            }

            if (arrItem.getVisitor_type().equals(WebApiHelper.USER_TYPE_PRE_BOOK_VISITOR)) {
                arrVisitorsData.add(arrItem);
            }
        }

        int remoteCount = arrRemoteWorkData.size();
        int officeCount = arrOfficeWorkData.size();
        int allCount = arrStaffReport.size();
        int visitorCount = arrVisitorsData.size();

        txtOfficeWorkingCounter.setText(String.valueOf(officeCount));
        txtRemotelyWorkingCounter.setText(String.valueOf(remoteCount));
        txtAllCounter.setText(String.valueOf(allCount));
        txtVisitorCounter.setText(String.valueOf(visitorCount));
    }

    private void showResultView() {
        rcvStaffReport.setVisibility(View.VISIBLE);
    }

    private void hideResultView() {
        rcvStaffReport.setVisibility(View.GONE);
    }

    private StaffReportRequestModel getStaffReportRequest() {
        StaffReportRequestModel staffReportRequestModel = new StaffReportRequestModel();
        StaffReportParamsModel paramsModel = new StaffReportParamsModel();
        paramsModel.setSite_id(Prefs.getString(PreferenceKeys.SITE_ID, "0"));
        staffReportRequestModel.setParam(paramsModel);
        return staffReportRequestModel;
    }

    private void setUpAdapter(ArrayList<StaffReportDataModel> arrVisitorDetails) {
        staffReportAdapter = new StaffReportAdapter(StaffReportActivity.this, arrVisitorDetails);
        rcvStaffReport.setAdapter(staffReportAdapter);
    }

    private void setUpRecyclerView() {
        rcvStaffReport.setLayoutManager(new LinearLayoutManager(StaffReportActivity.this, RecyclerView.VERTICAL, false));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_staff_report;
    }

    @Override
    public void onClick(View view) {

        if (view == loutRemoteView) {
            updateView(true, arrRemoteWorkData);
        } else if (view == loutOfficeView) {
            updateView(true, arrOfficeWorkData);
        } else if (view == loutAllView) {
            updateView(true, arrStaffReport);
        } else if (view == loutVisitor) {
            updateView(true, arrVisitorsData);
        }
    }
}
