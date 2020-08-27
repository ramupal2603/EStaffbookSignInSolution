package com.brinfotech.feedbacksystem.dataBase;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DBUtils {

    public static final long DB_SCHEMA_VERSION = 1;
    public static String DEFAULT_REALM_FILE_NAME = "default.realm"; // Eventually replace this if you're using a custom db name

    public static void initRealm(Context context) {

        try {
            Realm.init(context);
            RealmConfiguration realmConfiguration = null;
            RealmConfiguration.Builder builder = new RealmConfiguration.Builder()
                    .schemaVersion(DB_SCHEMA_VERSION);

            builder.name(DEFAULT_REALM_FILE_NAME);
            builder.migration(new MyRealmMigration());
            realmConfiguration = builder.build();
            Realm.setDefaultConfiguration(realmConfiguration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
