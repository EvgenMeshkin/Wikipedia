package by.evgen.android.apiclient.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import by.evgen.android.apiclient.R;

/**
 * Created by evgen on 24.01.2015.
 */
public class SetingsFragment extends PreferenceFragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_prefs);
    }

}
