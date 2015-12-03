package de.geithonline.abattlwp;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import de.geithonline.abattlwp.R;

public class BattColorPreferencesFragment extends PreferenceFragment {
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_batt_colors);
	}
}
