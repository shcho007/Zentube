package com.yolomate.Zentube.fragments.local.holder;

import android.view.View;
import android.view.ViewGroup;

import com.yolomate.Zentube.database.LocalItem;

import com.yolomate.Zentube.database.playlist.PlaylistMetadataEntry;
import com.yolomate.Zentube.fragments.local.LocalItemBuilder;

import java.text.DateFormat;

public class LocalPlaylistItemHolder extends PlaylistItemHolder {

    public LocalPlaylistItemHolder(LocalItemBuilder infoItemBuilder, ViewGroup parent) {
        super(infoItemBuilder, parent);
    }

    @Override
    public void updateFromItem(final LocalItem localItem, final DateFormat dateFormat) {
        if (!(localItem instanceof PlaylistMetadataEntry)) return;
        final PlaylistMetadataEntry item = (PlaylistMetadataEntry) localItem;

        itemTitleView.setText(item.name);
        itemStreamCountView.setText(String.valueOf(item.streamCount));
        itemUploaderView.setVisibility(View.INVISIBLE);

        itemBuilder.displayImage(item.thumbnailUrl, itemThumbnailView, DISPLAY_THUMBNAIL_OPTIONS);

        super.updateFromItem(localItem, dateFormat);
    }
}
