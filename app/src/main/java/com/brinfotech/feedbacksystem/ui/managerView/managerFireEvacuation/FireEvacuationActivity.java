package com.brinfotech.feedbacksystem.ui.managerView.managerFireEvacuation;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brinfotech.feedbacksystem.MyApplication;
import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;
import com.brinfotech.feedbacksystem.data.importFireEvacuation.ImportFireEvacuationParamsModel;
import com.brinfotech.feedbacksystem.data.importFireEvacuation.ImportFireEvacuationRequestModel;
import com.brinfotech.feedbacksystem.data.todaysVisitors.TodayVisitorDataModel;
import com.brinfotech.feedbacksystem.data.todaysVisitors.TodayVisitorResponseModel;
import com.brinfotech.feedbacksystem.data.todaysVisitors.TodaysParamsModel;
import com.brinfotech.feedbacksystem.data.todaysVisitors.TodaysRequestModel;
import com.brinfotech.feedbacksystem.helpers.ConstantClass;
import com.brinfotech.feedbacksystem.helpers.DateTimeUtils;
import com.brinfotech.feedbacksystem.helpers.PreferenceKeys;
import com.brinfotech.feedbacksystem.interfaces.OnStaffSelectedListener;
import com.brinfotech.feedbacksystem.jobQueue.SyncRequestParamsJob;
import com.brinfotech.feedbacksystem.network.RetrofitClient;
import com.brinfotech.feedbacksystem.network.RetrofitInterface;
import com.brinfotech.feedbacksystem.network.utils.NetworkUtils;
import com.google.gson.Gson;
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

    @BindView(R.id.txtVisitorListEmptyView)
    TextView txtVisitorListEmptyView;

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

                            showResultView();

                            updateAdapter();
                        } else {
                            showEmptyView();
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

    private void showEmptyView() {
        rcvFireEvacuationList.setVisibility(View.GONE);
        txtVisitorListEmptyView.setVisibility(View.VISIBLE);
    }

    private void showResultView() {
        rcvFireEvacuationList.setVisibility(View.VISIBLE);
        txtVisitorListEmptyView.setVisibility(View.GONE);
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
            createRequest();
        }
    }

    private void createRequest() {
        ArrayList<ImportFireEvacuationParamsModel> arrFireEvacuationList = new ArrayList<>();

        ArrayList<String> arrSelectedVisitors = new ArrayList<>(selectedHashmap.keySet());

        for (int i = 0; i < arrSelectedVisitors.size(); i++) {
            for (int j = 0; j < arrTodaysVisitor.size(); j++) {
                String selectedId = arrSelectedVisitors.get(i);
                TodayVisitorDataModel arrItem = arrTodaysVisitor.get(j);
                if (selectedId.equals(arrItem.getVisitor_id())) {

                    ImportFireEvacuationParamsModel evacuationImport = new ImportFireEvacuationParamsModel();
                    evacuationImport.setVisitor_id(arrItem.getVisitor_id());
                    evacuationImport.setVisitor_name(arrItem.getVisitor_name());
                    evacuationImport.setVisitor_type(arrItem.getVisitor_type());
                    evacuationImport.setLog_id(arrItem.getLog_id());
                    evacuationImport.setEvacuation_date(DateTimeUtils.getCurrentDate(FireEvacuationActivity.this));
                    evacuationImport.setEvacuation_time(DateTimeUtils.getCurrentTime(FireEvacuationActivity.this));
                    evacuationImport.setSite_id(arrItem.getSite_id());
                    evacuationImport.setLocation_id(Prefs.getString(PreferenceKeys.LOCATION_ID,"0"));


                    arrFireEvacuationList.add(evacuationImport);
                }
            }
        }

        if (!arrFireEvacuationList.isEmpty()) {
            ImportFireEvacuationRequestModel requestModel = new ImportFireEvacuationRequestModel();
            requestModel.setParam(arrFireEvacuationList);

            Gson gson = new Gson();
            String requestParameter = gson.toJson(requestModel);
            Log.i("Json", requestParameter);

            MyApplication.getInstance().getMainJobManager().addJobInBackground(new SyncRequestParamsJob(requestParameter));

            loutSuccessView.setVisibility(View.VISIBLE);

            tempHashmap = new HashMap<>();
            selectedHashmap = new HashMap<>();
            fireEvacuationListAdapter.setData(tempHashmap);
            showSuccessView();

        }
    }

    private void showSuccessView() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                finish();
            }
        }, ConstantClass.REDIRECTION_INTERVAL);
    }

    @Override
    public void OnStaffSelectedListener(HashMap<String, String> selectedHashmap) {
        this.selectedHashmap = selectedHashmap;

    }
}
