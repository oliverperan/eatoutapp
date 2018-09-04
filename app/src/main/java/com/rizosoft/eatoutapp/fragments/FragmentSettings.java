package com.rizosoft.eatoutapp.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.rizosoft.eatoutapp.R;

/**
 * Created by oliver on 30/04/2018.
 */

public class FragmentSettings extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences_fragment);
    }
}
