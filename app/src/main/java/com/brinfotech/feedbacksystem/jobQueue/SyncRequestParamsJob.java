package com.brinfotech.feedbacksystem.jobQueue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.brinfotech.feedbacksystem.data.importFireEvacuation.ImportFireEvacuationRequestModel;
import com.brinfotech.feedbacksystem.data.importFireEvacuation.ImportFireEvacuationResponseModel;
import com.brinfotech.feedbacksystem.dataBase.API.RequestAPI;
import com.brinfotech.feedbacksystem.dataBase.table.MST_REQUEST_SYNC;
import com.brinfotech.feedbacksystem.network.RetrofitClient;
import com.brinfotech.feedbacksystem.network.RetrofitInterface;
import com.google.gson.Gson;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;

public class SyncRequestParamsJob extends Job {

    String parameters;

    public SyncRequestParamsJob(String parameters) {
        super(new Params(Priority.HIGH).persist().requireNetwork());
        this.parameters = parameters;
    }

    @Override
    public void onAdded() {
        Realm myRealm = null;
        try {
            myRealm = Realm.getDefaultInstance();
            RequestAPI.addRequestData(myRealm, parameters);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (myRealm != null) {
                myRealm.close();
            }
        }
    }

    @Override
    public void onRun() throws Throwable {
        Realm myRealm = null;
        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        try {
            myRealm = Realm.getDefaultInstance();
            RealmResults<MST_REQUEST_SYNC> requestParams = RequestAPI.getRequestParams(myRealm);
            for (int i = 0; i < requestParams.size(); i++) {
                if (requestParams.get(i) != null) {
                    Gson gson = new Gson(); // Or use new GsonBuilder().create();
                    String requestParam = requestParams.get(i).getREQUEST_PARAMS();
                    ImportFireEvacuationRequestModel requestModel = gson.fromJson(requestParam, ImportFireEvacuationRequestModel.class);
                    Call<ImportFireEvacuationResponseModel> call =
                            retrofitInterface.importFireEvacuationList(requestModel);
                    Response body = call.execute();
                    boolean isSuccessful = body.isSuccessful();
                    RequestAPI.updateRequestParams(myRealm, requestParams.get(i).getID(), isSuccessful);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (myRealm != null) {
                myRealm.close();
            }
        }
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
