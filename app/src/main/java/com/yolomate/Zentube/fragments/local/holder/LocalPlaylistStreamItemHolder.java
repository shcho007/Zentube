package com.yolomate.Zentube.fragments.local.holder;

import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import com.yolomate.Zentube.R;
import com.yolomate.Zentube.database.LocalItem;
import com.yolomate.Zentube.database.playlist.PlaylistStreamEntry;
import org.schabi.newpipe.extractor.NewPipe;
import com.yolomate.Zentube.fragments.local.LocalItemBuilder;
import com.yolomate.Zentube.util.Localization;

import java.text.DateFormat;

public class LocalPlaylistStreamItemHolder extends LocalItemHolder {

    public final ImageView itemThumbnailView;
    public final TextView itemVideoTitleView;
    public final TextView itemAdditionalDetailsView;
    public final TextView itemDurationView;
    public final View itemHandleView;

    LocalPlaylistStreamItemHolder(LocalItemBuilder infoItemBuilder, int layoutId, ViewGroup parent) {
        super(infoItemBuilder, layoutId, parent);

        itemThumbnailView = itemView.findViewById(R.id.itemThumbnailView);
        itemVideoTitleView = itemView.findViewById(R.id.itemVideoTitleView);
        itemAdditionalDetailsView = itemView.findViewById(R.id.itemAdditionalDetails);
        itemDurationView = itemView.findViewById(R.id.itemDurationView);
        itemHandleView = itemView.findViewById(R.id.itemHandle);
    }

    public LocalPlaylistStreamItemHolder(LocalItemBuilder infoItemBuilder, ViewGroup parent) {
        this(infoItemBuilder, R.layout.list_stream_playlist_item, parent);
    }

    @Override
    public void updateFromItem(final LocalItem localItem, final DateFormat dateFormat) {
        if (!(localItem instanceof PlaylistStreamEntry)) return;
        final PlaylistStreamEntry item = (PlaylistStreamEntry) localItem;

        itemVideoTitleView.setText(item.title);
        itemAdditionalDetailsView.setText(Localization.concatenateStrings(item.uploader,
                NewPipe.getNameOfService(item.serviceId)));

        if (item.duration > 0) {
            itemDurationView.setText(Localization.getDurationString(item.duration));
            itemDurationView.setBackgroundColor(ContextCompat.getColor(itemBuilder.getContext(),
                    R.color.duration_background_color));
            itemDurationView.setVisibility(View.VISIBLE);
        } else {
            itemDurationView.setVisibility(View.GONE);
        }

        // Default thumbnail is shown on error, while loading and if the url is empty
        itemBuilder.displayImage(item.thumbnailUrl, itemThumbnailView, DISPLAY_THUMBNAIL_OPTIONS);

        itemView.setOnClickListener(view -> {
            if (itemBuilder.getOnItemSelectedListener() != null) {
                itemBuilder.getOnItemSelectedListener().selected(item);
            }
        });

        itemView.setLongClickable(true);
        itemView.setOnLongClickListener(view -> {
            if (itemBuilder.getOnItemSelectedListener() != null) {
                itemBuilder.getOnItemSelectedListener().held(item);
            }
            return true;
        });

        itemThumbnailView.setOnTouchListener(getOnTouchListener(item));
        itemHandleView.setOnTouchListener(getOnTouchListener(item));
    }

    private View.OnTouchListener getOnTouchListener(final PlaylistStreamEntry item) {
        return (view, motionEvent) -> {
            view.performClick();
            if (itemBuilder != null && itemBuilder.getOnItemSelectedListener() != null &&
                    motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                itemBuilder.getOnItemSelectedListener().drag(item,
                        LocalPlaylistStreamItemHolder.this);
            }
            return false;
        };
    }

    /**
     * Display options for stream thumbnails
     */
    private static final DisplayImageOptions DISPLAY_THUMBNAIL_OPTIONS =
            new DisplayImageOptions.Builder()
                    .cloneFrom(BASE_DISPLAY_IMAGE_OPTIONS)
                    .showImageOnFail(R.drawable.dummy_thumbnail)
                    .showImageForEmptyUri(R.drawable.dummy_thumbnail)
                    .showImageOnLoading(R.drawable.dummy_thumbnail)
                    .build();
}
