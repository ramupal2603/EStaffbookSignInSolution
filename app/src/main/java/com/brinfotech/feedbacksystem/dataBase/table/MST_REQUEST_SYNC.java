package com.brinfotech.feedbacksystem.dataBase.table;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MST_REQUEST_SYNC extends RealmObject {

    @PrimaryKey
    Integer ID;

    String REQUEST_PARAMS;

    Boolean SYNC_PENDING = false;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getREQUEST_PARAMS() {
        return REQUEST_PARAMS;
    }

    public void setREQUEST_PARAMS(String REQUEST_PARAMS) {
        this.REQUEST_PARAMS = REQUEST_PARAMS;
    }

    public Boolean getSYNC_PENDING() {
        return SYNC_PENDING;
    }

    public void setSYNC_PENDING(Boolean SYNC_PENDING) {
        this.SYNC_PENDING = SYNC_PENDING;
    }
}
