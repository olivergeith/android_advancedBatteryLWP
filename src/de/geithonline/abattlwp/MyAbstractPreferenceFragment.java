package de.geithonline.abattlwp;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.View;

/**
 * Diese Klasse sorgt dafüür, dass die Fragmente die richtigen Preferences benutzen... Dazu schaut diese Klasse in die Zugehörige Activity und entscheidet dann
 * welche Preferences genutzt werden
 * 
 * @author Oliver
 *
 */
public abstract class MyAbstractPreferenceFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	protected SharedPreferences prefs;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// die richtigen Preferences benutzen
		/**
		 * Hier schauen wir, wer die Parent Activity ist... und dann entscheiden wir welches PreferenceFile wir benutzen So können wir dies später erweitern für
		 * ein Widget! Und das Widget benutz dann andere Preferencen bzw eine andere PreferenceActivity!
		 */
		// if (getActivity() instanceof LiveWallpaperPreferencesActivity) {
		// getPreferenceManager().setSharedPreferencesName(Settings.LWP_PREFERENCE_FILE);
		// prefs = getPreferenceManager().getSharedPreferences();
		// } else {
		// // default Preferences
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		// }

	}

	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// deshalb sind wir Abvstract, damit unsere Kinder onPreferenceChange implementieren implemntieren!
		prefs.registerOnSharedPreferenceChangeListener(this);
	}

}
