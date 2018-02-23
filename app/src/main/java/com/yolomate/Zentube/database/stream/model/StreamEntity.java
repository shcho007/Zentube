package com.yolomate.Zentube.database.stream.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import org.schabi.newpipe.extractor.stream.StreamInfo;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import org.schabi.newpipe.extractor.stream.StreamType;
import com.yolomate.Zentube.playlist.PlayQueueItem;
import com.yolomate.Zentube.util.Constants;

import java.io.Serializable;

import static com.yolomate.Zentube.database.stream.model.StreamEntity.STREAM_SERVICE_ID;
import static com.yolomate.Zentube.database.stream.model.StreamEntity.STREAM_TABLE;
import static com.yolomate.Zentube.database.stream.model.StreamEntity.STREAM_URL;

@Entity(tableName = STREAM_TABLE,
        indices = {@Index(value = {STREAM_SERVICE_ID, STREAM_URL}, unique = true)})
public class StreamEntity implements Serializable {

    final public static String STREAM_TABLE             = "streams";
    final public static String STREAM_ID                = "uid";
    final public static String STREAM_SERVICE_ID        = "service_id";
    final public static String STREAM_URL               = "url";
    final public static String STREAM_TITLE             = "title";
    final public static String STREAM_TYPE              = "stream_type";
    final public static String STREAM_DURATION          = "duration";
    final public static String STREAM_UPLOADER          = "uploader";
    final public static String STREAM_THUMBNAIL_URL     = "thumbnail_url";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = STREAM_ID)
    private long uid = 0;

    @ColumnInfo(name = STREAM_SERVICE_ID)
    private int serviceId = Constants.NO_SERVICE_ID;

    @ColumnInfo(name = STREAM_URL)
    private String url;

    @ColumnInfo(name = STREAM_TITLE)
    private String title;

    @ColumnInfo(name = STREAM_TYPE)
    private StreamType streamType;

    @ColumnInfo(name = STREAM_DURATION)
    private Long duration;

    @ColumnInfo(name = STREAM_UPLOADER)
    private String uploader;

    @ColumnInfo(name = STREAM_THUMBNAIL_URL)
    private String thumbnailUrl;

    public StreamEntity(final int serviceId, final String title, final String url,
                        final StreamType streamType, final String thumbnailUrl, final String uploader,
                        final long duration) {
        this.serviceId = serviceId;
        this.title = title;
        this.url = url;
        this.streamType = streamType;
        this.thumbnailUrl = thumbnailUrl;
        this.uploader = uploader;
        this.duration = duration;
    }

    @Ignore
    public StreamEntity(final StreamInfoItem item) {
        this(item.service_id, item.name, item.url, item.stream_type, item.thumbnail_url,
                item.uploader_name, item.duration);
    }

    @Ignore
    public StreamEntity(final StreamInfo info) {
        this(info.service_id, info.name, info.url, info.stream_type, info.thumbnail_url,
                info.uploader_name, info.duration);
    }

    @Ignore
    public StreamEntity(final PlayQueueItem item) {
        this(item.getServiceId(), item.getTitle(), item.getUrl(), item.getStreamType(),
                item.getThumbnailUrl(), item.getUploader(), item.getDuration());
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public StreamType getStreamType() {
        return streamType;
    }

    public void setStreamType(StreamType type) {
        this.streamType = type;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
