package com.brinfotech.feedbacksystem.ui.fireMarshalView.fireEvacuation;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brinfotech.feedbacksystem.MyApplication;
import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;
import com.brinfotech.feedbacksystem.data.department.DepartmentRequestModel;
import com.brinfotech.feedbacksystem.data.department.DepartmentRequestParamModel;
import com.brinfotech.feedbacksystem.data.department.DepartmentResponseModel;
import com.brinfotech.feedbacksystem.data.department.DepartmentResponseModelData;
import com.brinfotech.feedbacksystem.data.importFireEvacuation.ImportFireEvacuationParamModel;
import com.brinfotech.feedbacksystem.data.importFireEvacuation.ImportFireEvacuationRequestModel;
import com.brinfotech.feedbacksystem.data.importFireEvacuation.ImportFireEvacuationVisitorModel;
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

public class FireMarshalEvacuationActivity extends BaseActivity implements OnStaffSelectedListener {

    @BindView(R.id.rcvFireEvacuationList)
    RecyclerView rcvFireEvacuationList;

    @BindView(R.id.txtVisitorListEmptyView)
    TextView txtVisitorListEmptyView;

    @BindView(R.id.loutAllView)
    LinearLayout loutAllView;

    @BindView(R.id.loutDepartment)
    LinearLayout loutDepartment;

    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    @BindView(R.id.txtDeptName)
    TextView txtDeptName;

    @BindView(R.id.txtAllCounter)
    TextView txtAllCounter;

    @BindView(R.id.loutSuccessView)
    LinearLayout loutSuccessView;

    FireEvacuationListAdapter fireEvacuationListAdapter;
    ArrayList<TodayVisitorDataModel> arrTodaysVisitor = new ArrayList<>();
    ArrayList<DepartmentResponseModelData> arrDeptList = new ArrayList<>();
    ArrayList<TodayVisitorDataModel> arrDeptWiseVisitor = new ArrayList<>();

    HashMap<String, String> selectedHashmap = new HashMap<>();
    HashMap<String, String> tempHashmap = new HashMap<>();
    private String selectedDeptId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btnSubmit.setOnClickListener(this::onClick);
        loutDepartment.setOnClickListener(this::onClick);
        loutAllView.setOnClickListener(this::onClick);
        setUpRecyclerView();

        setUpAdapter();

        getDepartmentList();

        getTodayVisitorsData();
    }

    private void getDepartmentList() {
        if (NetworkUtils.isNetworkConnected(getContext())) {

            showProgressBar();

            RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
            apiService.getDepartment(getDepartmentData()).enqueue(new Callback<DepartmentResponseModel>() {
                @Override
                public void onResponse(Call<DepartmentResponseModel> call, Response<DepartmentResponseModel> response) {
                    hideProgressBar();
                    if (response.isSuccessful()) {
                        DepartmentResponseModel responseModel = response.body();
                        if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS)) {
                            arrDeptList.clear();
                            arrDeptList.addAll(responseModel.getDepartment_details());
                        } else {
                            showEmptyView();
                        }
                    }
                }

                @Override
                public void onFailure(Call<DepartmentResponseModel> call, Throwable t) {
                    hideProgressBar();
                    t.printStackTrace();

                }
            });
        } else {
            showNoNetworkMessage();
        }
    }

    private DepartmentRequestModel getDepartmentData() {
        DepartmentRequestModel requestModel = new DepartmentRequestModel();
        DepartmentRequestParamModel paramModel = new DepartmentRequestParamModel();
        paramModel.setCompany_id(Prefs.getString(PreferenceKeys.SITE_ID, ""));
        paramModel.setUser_type(Prefs.getString(PreferenceKeys.USER_TYPE, ""));
        paramModel.setUser_id(Prefs.getString(PreferenceKeys.USER_ID, ""));
        requestModel.setParam(paramModel);
        return requestModel;
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

                            showCounter();

                            showResultView();

                            updateAdapter();
                        } else {
                            showEmptyView();
                            showCounter();
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

    private void showCounter() {
        int counter = arrTodaysVisitor.size();
        txtAllCounter.setText(String.valueOf(counter));
    }

    private TodaysRequestModel getTodayRequestData() {
        TodaysRequestModel requestModel = new TodaysRequestModel();
        TodaysParamsModel paramsModel = new TodaysParamsModel();
        paramsModel.setSite_id(Prefs.getString(PreferenceKeys.SITE_ID, ""));
        paramsModel.setLocation_id(Prefs.getString(PreferenceKeys.LOCATION_ID, "0"));
        paramsModel.setUserID(Prefs.getString(PreferenceKeys.USER_ID, "0"));
        paramsModel.setUserType(Prefs.getString(PreferenceKeys.USER_TYPE, "0"));
        requestModel.setParam(paramsModel);
        return requestModel;
    }

    private void setUpAdapter() {
        tempHashmap.clear();
        fireEvacuationListAdapter = new FireEvacuationListAdapter(FireMarshalEvacuationActivity.this, arrTodaysVisitor, tempHashmap, FireMarshalEvacuationActivity.this);
        rcvFireEvacuationList.setAdapter(fireEvacuationListAdapter);
    }

    private void setUpRecyclerView() {
        rcvFireEvacuationList.setLayoutManager(new LinearLayoutManager(FireMarshalEvacuationActivity.this, RecyclerView.VERTICAL, false));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_fire_marshal_evacuation;
    }

    @Override
    public void onClick(View view) {

        if (view == btnSubmit) {
            createRequest();
        }

        if (view == loutDepartment) {
            if (!arrTodaysVisitor.isEmpty()) {
                showDepartmentDialog();
            }

        }

        if (view == loutAllView) {

            if (arrTodaysVisitor.isEmpty()) {
                showEmptyView();
            } else {
                showResultView();
                txtDeptName.setText("Department");
                tempHashmap.clear();
                fireEvacuationListAdapter.updateData(arrTodaysVisitor);
            }

        }
    }

    private void showDepartmentDialog() {
        final Dialog dialog = new Dialog(FireMarshalEvacuationActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.searchview);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final ListView lv = (ListView) dialog.findViewById(R.id.listView1);
        TextView header = (TextView) dialog.findViewById(R.id.header);
        header.setText("Select Department");
        Button btn = (Button) dialog.findViewById(R.id.cancel);
        Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);

        //CREATE AND SET ADAPTER TO LISTVIEW
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FireMarshalEvacuationActivity.this,
                android.R.layout.simple_list_item_multiple_choice);


        for (int i = 0; i < arrDeptList.size(); i++) {
            arrayAdapter.add(arrDeptList.get(i).getName());
        }

        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setAdapter(arrayAdapter);
        EditText sv = (EditText) dialog.findViewById(R.id.search);
        sv.setHint("Search Name or scroll down");
        sv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrayAdapter.getFilter().filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //BUTTON
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray sparseBooleanArray = lv.getCheckedItemPositions();
                StringBuilder selectedIDs = new StringBuilder();
                ArrayList<String> arrSelectedName = new ArrayList<>();

                for (int i = 0; i < arrDeptList.size(); i++) {
                    if (sparseBooleanArray.get(i)) {
                        arrSelectedName.add(arrDeptList.get(i).getName());
                        selectedIDs.append(arrDeptList.get(i).getDepartment_id()).append(",");
                    }
                }

                selectedDeptId = selectedIDs.toString();
                txtDeptName.setText(arrSelectedName.toString().replaceAll("\\[", "").replaceAll("]", ""));
                filteredOutData();
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    private void filteredOutData() {
        String[] arrSelectedDept = selectedDeptId.split(",");

        arrDeptWiseVisitor.clear();
        for (String s : arrSelectedDept) {
            for (int j = 0; j < arrTodaysVisitor.size(); j++) {
                if (arrTodaysVisitor.get(j).getDepartment_id().equals(s)) {
                    arrDeptWiseVisitor.add(arrTodaysVisitor.get(j));
                }
            }
        }

        if (!arrDeptWiseVisitor.isEmpty()) {

            showResultView();

            tempHashmap.clear();
            fireEvacuationListAdapter.updateData(arrDeptWiseVisitor);
        } else {
            showEmptyView();
        }

    }

    private void createRequest() {
        ArrayList<ImportFireEvacuationVisitorModel> arrFireEvacuationList = new ArrayList<>();
        ArrayList<String> arrSelectedVisitors = new ArrayList<>(selectedHashmap.keySet());


        for (int j = 0; j < arrTodaysVisitor.size(); j++) {
            TodayVisitorDataModel arrItem = arrTodaysVisitor.get(j);
            if (arrSelectedVisitors.contains(arrItem.getVisitor_id())) {

                ImportFireEvacuationVisitorModel evacuationImport = new ImportFireEvacuationVisitorModel();
                evacuationImport.setVisitor_id(arrItem.getVisitor_id());
                evacuationImport.setVisitor_name(arrItem.getVisitor_name());
                evacuationImport.setVisitor_type(arrItem.getVisitor_type());
                evacuationImport.setLog_id(arrItem.getLog_id());
                evacuationImport.setEvacuation_date(DateTimeUtils.getCurrentDate(FireMarshalEvacuationActivity.this));
                evacuationImport.setEvacuation_time(DateTimeUtils.getCurrentTime(FireMarshalEvacuationActivity.this));
                evacuationImport.setSite_id(arrItem.getSite_id());
                evacuationImport.setIsSelected("1");
                evacuationImport.setLocation_id(Prefs.getString(PreferenceKeys.LOCATION_ID, "0"));

                arrFireEvacuationList.add(evacuationImport);
            } else {
                ImportFireEvacuationVisitorModel evacuationImport = new ImportFireEvacuationVisitorModel();
                evacuationImport.setVisitor_id(arrItem.getVisitor_id());
                evacuationImport.setVisitor_name(arrItem.getVisitor_name());
                evacuationImport.setVisitor_type(arrItem.getVisitor_type());
                evacuationImport.setLog_id(arrItem.getLog_id());
                evacuationImport.setEvacuation_date(DateTimeUtils.getCurrentDate(FireMarshalEvacuationActivity.this));
                evacuationImport.setEvacuation_time(DateTimeUtils.getCurrentTime(FireMarshalEvacuationActivity.this));
                evacuationImport.setSite_id(arrItem.getSite_id());
                evacuationImport.setIsSelected("0");
                evacuationImport.setLocation_id(Prefs.getString(PreferenceKeys.LOCATION_ID, "0"));

                arrFireEvacuationList.add(evacuationImport);
            }
        }


        if (!arrFireEvacuationList.isEmpty()) {

            ImportFireEvacuationRequestModel requestModel = new ImportFireEvacuationRequestModel();
            ImportFireEvacuationParamModel paramModel = new ImportFireEvacuationParamModel();
            paramModel.setUser_id(Prefs.getString(PreferenceKeys.USER_ID, ""));
            paramModel.setVisitor_details(arrFireEvacuationList);
            requestModel.setParam(paramModel);

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
