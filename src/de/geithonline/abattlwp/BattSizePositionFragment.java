package de.geithonline.abattlwp;

import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * This fragment shows the preferences for the first header.
 */
public class BattSizePositionFragment extends MyAbstractPreferenceFragment {

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_size);

		// initializing Members
		enableProFeatures();
	}

	private void enableProFeatures() {
	}

	@Override
	public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
	}

}
