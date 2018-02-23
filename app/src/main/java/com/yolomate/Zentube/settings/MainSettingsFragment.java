package com.yolomate.Zentube.settings;

import android.os.Bundle;

import com.yolomate.Zentube.R;

public class MainSettingsFragment extends BasePreferenceFragment {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.main_settings);
    }
}
