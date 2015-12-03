package de.geithonline.abattlwp;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class BattNumberPreferencesFragment extends PreferenceFragment {
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_batt_number);
		enableProFeatures();
	}

	private void enableProFeatures() {

	}

}
