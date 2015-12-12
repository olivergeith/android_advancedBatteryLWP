package de.geithonline.abattlwp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import de.geithonline.abattlwp.bitmapdrawer.enums.PositionVertical;
import de.geithonline.abattlwp.settings.Settings;

/**
 * This fragment shows the preferences for the first header.
 */
public class BattSizePositionFragment extends MyAbstractPreferenceFragment {

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_size);

		// initializing Members
		handlePreferences();
		enableProFeatures();
	}

	private void handlePreferences() {
		final Preference offset1 = findPreference("vertical_positionInt");
		final Preference offset2 = findPreference("vertical_position_landscapeInt");
		if (Settings.getVerticalPosition().equals(PositionVertical.Custom)) {
			offset1.setEnabled(true);
			offset2.setEnabled(true);
		} else {
			offset1.setEnabled(false);
			offset2.setEnabled(false);
		}
	}

	private void enableProFeatures() {
	}

	@Override
	public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
		handlePreferences();
	}

}
