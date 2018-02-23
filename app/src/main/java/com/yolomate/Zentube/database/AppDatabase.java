package com.yolomate.Zentube.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.yolomate.Zentube.database.history.dao.SearchHistoryDAO;
import com.yolomate.Zentube.database.history.dao.StreamHistoryDAO;
import com.yolomate.Zentube.database.history.model.SearchHistoryEntry;
import com.yolomate.Zentube.database.playlist.dao.PlaylistDAO;
import com.yolomate.Zentube.database.playlist.dao.PlaylistRemoteDAO;
import com.yolomate.Zentube.database.playlist.dao.PlaylistStreamDAO;
import com.yolomate.Zentube.database.playlist.model.PlaylistEntity;
import com.yolomate.Zentube.database.playlist.model.PlaylistRemoteEntity;
import com.yolomate.Zentube.database.playlist.model.PlaylistStreamEntity;
import com.yolomate.Zentube.database.stream.dao.StreamDAO;
import com.yolomate.Zentube.database.stream.dao.StreamStateDAO;
import com.yolomate.Zentube.database.stream.model.StreamEntity;
import com.yolomate.Zentube.database.subscription.SubscriptionDAO;
import com.yolomate.Zentube.database.subscription.SubscriptionEntity;
import com.yolomate.Zentube.database.history.model.StreamHistoryEntity;
import com.yolomate.Zentube.database.stream.model.StreamStateEntity;

@TypeConverters({Converters.class})
@Database(
        entities = {
                SubscriptionEntity.class, SearchHistoryEntry.class,
                StreamEntity.class, StreamHistoryEntity.class, StreamStateEntity.class,
                PlaylistEntity.class, PlaylistStreamEntity.class, PlaylistRemoteEntity.class
        },
        version = Migrations.DB_VER_12_0,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "newpipe.db";

    public abstract SubscriptionDAO subscriptionDAO();

    public abstract SearchHistoryDAO searchHistoryDAO();

    public abstract StreamDAO streamDAO();

    public abstract StreamHistoryDAO streamHistoryDAO();

    public abstract StreamStateDAO streamStateDAO();

    public abstract PlaylistDAO playlistDAO();

    public abstract PlaylistStreamDAO playlistStreamDAO();

    public abstract PlaylistRemoteDAO playlistRemoteDAO();
}
