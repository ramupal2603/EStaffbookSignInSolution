package com.brinfotech.feedbacksystem.dataBase;


import androidx.annotation.NonNull;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;


/**
 * Created by PREMIUM on 21-09-2018.
 */

public class MyRealmMigration implements RealmMigration {

    @Override
    public void migrate(@NonNull DynamicRealm myRealm, long oldVersion, long newVersion) {
        RealmSchema schema = myRealm.getSchema();

        //ADDED Field NEW_MSG_DATE

    }

    @Override
    public int hashCode() {
        return 37;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof RealmMigration);
    }
}