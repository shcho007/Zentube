package com.yolomate.Zentube.database.subscription;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.yolomate.Zentube.database.BasicDAO;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface SubscriptionDAO extends BasicDAO<SubscriptionEntity> {
    @Override
    @Query("SELECT * FROM " + SubscriptionEntity.SUBSCRIPTION_TABLE)
    Flowable<List<SubscriptionEntity>> getAll();

    @Override
    @Query("DELETE FROM " + SubscriptionEntity.SUBSCRIPTION_TABLE)
    int deleteAll();

    @Override
    @Query("SELECT * FROM " + SubscriptionEntity.SUBSCRIPTION_TABLE + " WHERE " + SubscriptionEntity.SUBSCRIPTION_SERVICE_ID + " = :serviceId")
    Flowable<List<SubscriptionEntity>> listByService(int serviceId);

    @Query("SELECT * FROM " + SubscriptionEntity.SUBSCRIPTION_TABLE + " WHERE " +
            SubscriptionEntity.SUBSCRIPTION_URL + " LIKE :url AND " +
            SubscriptionEntity.SUBSCRIPTION_SERVICE_ID + " = :serviceId")
    Flowable<List<SubscriptionEntity>> getSubscription(int serviceId, String url);
}
