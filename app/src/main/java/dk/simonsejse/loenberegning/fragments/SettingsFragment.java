package dk.simonsejse.loenberegning.fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import dk.simonsejse.loenberegning.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }

}