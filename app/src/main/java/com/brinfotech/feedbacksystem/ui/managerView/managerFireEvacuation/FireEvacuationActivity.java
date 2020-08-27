package com.brinfotech.feedbacksystem.ui.managerView.managerFireEvacuation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;
import com.brinfotech.feedbacksystem.data.todaysVisitors.TodayVisitorDataModel;
import com.brinfotech.feedbacksystem.data.todaysVisitors.TodayVisitorResponseModel;
import com.brinfotech.feedbacksystem.data.todaysVisitors.TodaysParamsModel;
import com.brinfotech.feedbacksystem.data.todaysVisitors.TodaysRequestModel;
import com.brinfotech.feedbacksystem.helpers.ConstantClass;
import com.brinfotech.feedbacksystem.helpers.PreferenceKeys;
import com.brinfotech.feedbacksystem.interfaces.OnStaffSelectedListener;
import com.brinfotech.feedbacksystem.network.RetrofitClient;
import com.brinfotech.feedbacksystem.network.RetrofitInterface;
import com.brinfotech.feedbacksystem.network.utils.NetworkUtils;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FireEvacuationActivity extends BaseActivity implements OnStaffSelectedListener {

    @BindView(R.id.rcvFireEvacuationList)
    RecyclerView rcvFireEvacuationList;

    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    @BindView(R.id.loutSuccessView)
    LinearLayout loutSuccessView;

    FireEvacuationListAdapter fireEvacuationListAdapter;
    ArrayList<TodayVisitorDataModel> arrTodaysVisitor = new ArrayList<>();

    HashMap<String, String> selectedHashmap = new HashMap<>();
    HashMap<String, String> tempHashmap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btnSubmit.setOnClickListener(this::onClick);
        setUpRecyclerView();

        setUpAdapter();

        getTodayVisitorsData();
    }

    private void getTodayVisitorsData() {
        if (NetworkUtils.isNetworkConnected(getContext())) {

            showProgressBar();

            RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
            apiService.getTodayVisitors(getTodayRequestData()).enqueue(new Callback<TodayVisitorResponseModel>() {
                @Override
                public void onResponse(Call<TodayVisitorResponseModel> call, Response<TodayVisitorResponseModel> response) {
                    hideProgressBar();
                    if (response.isSuccessful()) {
                        TodayVisitorResponseModel responseModel = response.body();
                        if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS)) {
                            arrTodaysVisitor.clear();
                            arrTodaysVisitor.addAll(responseModel.getData());

                            updateAdapter();
                        }
                    }
                }

                @Override
                public void onFailure(Call<TodayVisitorResponseModel> call, Throwable t) {
                    hideProgressBar();
                    t.printStackTrace();

                }
            });
        } else {
            showNoNetworkMessage();
        }
    }

    private void updateAdapter() {
        if (fireEvacuationListAdapter != null) {
            fireEvacuationListAdapter.updateData(arrTodaysVisitor);
        }
    }

    private TodaysRequestModel getTodayRequestData() {
        TodaysRequestModel requestModel = new TodaysRequestModel();
        TodaysParamsModel paramsModel = new TodaysParamsModel();
        paramsModel.setSite_id(Prefs.getString(PreferenceKeys.SITE_ID, ""));
        requestModel.setParam(paramsModel);
        return requestModel;
    }

    private void setUpAdapter() {
        tempHashmap.clear();
        fireEvacuationListAdapter = new FireEvacuationListAdapter(FireEvacuationActivity.this, arrTodaysVisitor, tempHashmap, FireEvacuationActivity.this);
        rcvFireEvacuationList.setAdapter(fireEvacuationListAdapter);
    }

    private void setUpRecyclerView() {
        rcvFireEvacuationList.setLayoutManager(new LinearLayoutManager(FireEvacuationActivity.this, RecyclerView.VERTICAL, false));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_fire_evacuation;
    }

    @Override
    public void onClick(View view) {

        if (view == btnSubmit) {

        }
    }

    @Override
    public void OnStaffSelectedListener(HashMap<String, String> selectedHashmap) {
        this.selectedHashmap = selectedHashmap;

    }
}
