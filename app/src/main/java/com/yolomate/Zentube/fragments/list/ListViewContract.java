package com.yolomate.Zentube.fragments.list;

import com.yolomate.Zentube.fragments.ViewContract;

public interface ListViewContract<I, N> extends ViewContract<I> {
    void showListFooter(boolean show);

    void handleNextItems(N result);
}
