package com.brinfotech.feedbacksystem.ui.adminView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;
import com.brinfotech.feedbacksystem.data.getNoOfContractor.AdminSiteListModel;
import com.brinfotech.feedbacksystem.data.getNoOfContractor.AdminSitesRequestModel;
import com.brinfotech.feedbacksystem.data.getNoOfContractor.AdminSitesRequestParamModel;
import com.brinfotech.feedbacksystem.data.getNoOfContractor.AdminSitesResponseModel;
import com.brinfotech.feedbacksystem.helpers.ConstantClass;
import com.brinfotech.feedbacksystem.helpers.PreferenceKeys;
import com.brinfotech.feedbacksystem.interfaces.OnSiteSelectedListener;
import com.brinfotech.feedbacksystem.network.RetrofitClient;
import com.brinfotech.feedbacksystem.network.RetrofitInterface;
import com.brinfotech.feedbacksystem.ui.managerView.managerDashboard.ManageDashboardActivity;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminSiteSelectionScreen extends BaseActivity implements OnSiteSelectedListener {

    @BindView(R.id.rcvDashboardList)
    RecyclerView rcvDashboardList;

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;

    SiteSelectionListAdapter siteSelectionListAdapter;

    ArrayList<AdminSiteListModel> arrSiteList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setupRecyclerView();

        setUpAdapter();

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swiperefresh.setRefreshing(true);
                getSiteDetails(true);
            }
        });

        getSiteDetails(false);
    }

    private void getSiteDetails(boolean isFromRefreshing) {
        showProgressBar();

        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        Call<AdminSitesResponseModel> call = retrofitInterface.getSiteVisitors(getSiteRequestModel());
        call.enqueue(new Callback<AdminSitesResponseModel>() {
            @Override
            public void onResponse(Call<AdminSitesResponseModel> call, Response<AdminSitesResponseModel> response) {
                if (response.isSuccessful()) {
                    arrSiteList.clear();
                    if (response.body() != null) {
                        AdminSitesResponseModel responseModel = response.body();
                        if (responseModel.getStatus().equals(ConstantClass.TRUE)) {
                            arrSiteList.addAll(responseModel.getData().getSite_details());
                            updateAdapter();
                        }

                    }
                }
                if (isFromRefreshing) {
                    swiperefresh.setRefreshing(false);
                }

                hideProgressBar();
            }

            @Override
            public void onFailure(Call<AdminSitesResponseModel> call, Throwable t) {
                t.printStackTrace();
                hideProgressBar();
                showToastMessage("Something went wrong");
            }
        });
    }

    private void updateAdapter() {
        if (siteSelectionListAdapter != null) {
            siteSelectionListAdapter.updateData(arrSiteList);
        }
    }

    private AdminSitesRequestModel getSiteRequestModel() {
        AdminSitesRequestModel adminSitesRequestModel = new AdminSitesRequestModel();
        AdminSitesRequestParamModel paramModel = new AdminSitesRequestParamModel();
        paramModel.setUser_id(Prefs.getString(PreferenceKeys.USER_ID, "0"));
        paramModel.setUser_type(Prefs.getString(PreferenceKeys.USER_TYPE, "0"));
        adminSitesRequestModel.setParam(paramModel);
        return adminSitesRequestModel;

    }

    private void setUpAdapter() {
        siteSelectionListAdapter = new SiteSelectionListAdapter(AdminSiteSelectionScreen.this, AdminSiteSelectionScreen.this, arrSiteList);
        rcvDashboardList.setAdapter(siteSelectionListAdapter);
    }

    private void setupRecyclerView() {
        rcvDashboardList.setLayoutManager(new LinearLayoutManager(AdminSiteSelectionScreen.this,
                LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_admin_site_selection_screen;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void openForm(int position) {
        String selectedSiteId = arrSiteList.get(position).getSite_id();
        Prefs.putString(PreferenceKeys.LOCATION_ID, selectedSiteId);
        Intent intent = new Intent(getActivity(), ManageDashboardActivity.class);
        startActivity(intent);
    }
}
