package com.yolomate.Zentube;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

import com.yolomate.Zentube.database.Migrations;
import com.yolomate.Zentube.database.AppDatabase;

import static com.yolomate.Zentube.database.AppDatabase.DATABASE_NAME;

public final class NewPipeDatabase {

    private static AppDatabase databaseInstance;

    private NewPipeDatabase() {
        //no instance
    }

    public static void init(Context context) {
        databaseInstance = Room
                .databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                .addMigrations(Migrations.MIGRATION_11_12)
                .fallbackToDestructiveMigration()
                .build();
    }

    @NonNull
    @Deprecated
    public static AppDatabase getInstance() {
        if (databaseInstance == null) throw new RuntimeException("Database not initialized");

        return databaseInstance;
    }

    @NonNull
    public static AppDatabase getInstance(Context context) {
        if (databaseInstance == null) init(context);
        return databaseInstance;
    }
}
