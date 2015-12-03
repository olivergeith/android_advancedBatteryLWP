package de.geithonline.abattlwp;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import de.geithonline.abattlwp.R;

public class BattNumberPreferencesFragment extends PreferenceFragment {
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_batt_number);
		enableProFeatures();
	}

	private void enableProFeatures() {
		// final Preference showNumber = findPreference("show_number");
		// if (Settings.isPremium()) {
		// showNumber.setEnabled(true);
		// } else {
		// Settings.prefs.edit().putBoolean("show_number", true).commit();
		// showNumber.setEnabled(false);
		// }

		// final Preference showStatus = findPreference("show_status");
		// if (Settings.isPremium()) {
		// showStatus.setEnabled(true);
		// } else {
		// Settings.prefs.edit().putBoolean("show_status", false).commit();
		// showStatus.setEnabled(false);
		// }

	}

}
