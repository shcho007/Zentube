package com.yolomate.Zentube.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yolomate.Zentube.BaseFragment;
import com.yolomate.Zentube.R;

public class BlankFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar()
                    .setTitle("Zentube");
        }
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            if(activity != null && activity.getSupportActionBar() != null) {
                activity.getSupportActionBar()
                        .setTitle("NewPipe");
            }
        }
    }
}
