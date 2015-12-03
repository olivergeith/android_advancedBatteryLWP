package de.geithonline.abattlwp;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import de.geithonline.abattlwp.R;

/**
 * This fragment shows the preferences for the first header.
 */
public class BattSizePositionFragment extends PreferenceFragment {

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_size);

		// initializing Members
		enableProFeatures();
	}

	private void enableProFeatures() {
	}

}
