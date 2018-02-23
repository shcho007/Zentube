package com.yolomate.Zentube.settings;

import android.os.Bundle;

import com.yolomate.Zentube.R;

public class HistorySettingsFragment extends BasePreferenceFragment {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.history_settings);
    }
}
