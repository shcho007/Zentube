package com.yolomate.Zentube.fragments.local.bookmark;

import com.yolomate.Zentube.database.stream.StreamStatisticsEntry;

import com.yolomate.Zentube.R;

import java.util.Collections;
import java.util.List;

public final class LastPlayedFragment extends StatisticsPlaylistFragment {
    @Override
    protected String getName() {
        return getString(R.string.title_last_played);
    }

    @Override
    protected List<StreamStatisticsEntry> processResult(List<StreamStatisticsEntry> results)  {
        Collections.sort(results, (left, right) ->
                right.latestAccessDate.compareTo(left.latestAccessDate));
        return results;
    }
}
