package com.yolomate.Zentube.fragments.local.bookmark;

import com.yolomate.Zentube.R;
import com.yolomate.Zentube.database.stream.StreamStatisticsEntry;

import java.util.Collections;
import java.util.List;

public final class MostPlayedFragment extends StatisticsPlaylistFragment {
    @Override
    protected String getName() {
        return getString(R.string.title_most_played);
    }

    @Override
    protected List<StreamStatisticsEntry> processResult(List<StreamStatisticsEntry> results)  {
        Collections.sort(results, (left, right) ->
                ((Long) right.watchCount).compareTo(left.watchCount));
        return results;
    }

}
