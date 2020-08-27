package com.brinfotech.feedbacksystem.dataBase.API;

import androidx.annotation.NonNull;

import com.brinfotech.feedbacksystem.dataBase.table.MST_REQUEST_SYNC;

import io.realm.Realm;
import io.realm.RealmResults;

public class RequestAPI {
    public static String ID = "ID";

    public static void addRequestData(@NonNull Realm myRealm, String request) {
        final MST_REQUEST_SYNC mstRequest = buildRequestData(myRealm, request);
        myRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm myRealm) {
                myRealm.insertOrUpdate(mstRequest);
            }
        });
    }

    private static MST_REQUEST_SYNC buildRequestData(Realm myRealm, String request) {
        MST_REQUEST_SYNC mstRequest = new MST_REQUEST_SYNC();
        int nextId = getLatestRecordNo(myRealm);
        mstRequest.setID(nextId);
        mstRequest.setREQUEST_PARAMS(request);
        mstRequest.setSYNC_PENDING(false);
        return mstRequest;
    }

    private static Integer getLatestRecordNo(Realm myRealm) {
        Number id = myRealm.where(MST_REQUEST_SYNC.class).max(ID);
        return (id == null) ? 1 : id.intValue() + 1;
    }

    public static RealmResults<MST_REQUEST_SYNC> getRequestParams(Realm myRealm) {
        return myRealm.where(MST_REQUEST_SYNC.class).
                equalTo("SYNC_PENDING", false).findAll();
    }

    public static void updateRequestParams(@NonNull Realm myRealm, final Integer id, final boolean isSuccessfull) {
        myRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm myRealm) {
                MST_REQUEST_SYNC mstRequest = myRealm.where(MST_REQUEST_SYNC.class).equalTo(ID, id).findFirst();
                if (mstRequest != null) {
                    mstRequest.setSYNC_PENDING(isSuccessfull);
                }
            }
        });
    }
}
