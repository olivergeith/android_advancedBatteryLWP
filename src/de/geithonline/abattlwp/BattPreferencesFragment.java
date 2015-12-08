package de.geithonline.abattlwp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;
import de.geithonline.abattlwp.bitmapdrawer.IBitmapDrawer;
import de.geithonline.abattlwp.settings.DrawerManager;
import de.geithonline.abattlwp.settings.Settings;
import de.geithonline.abattlwp.stylelistrecycler.StyleListRecyclerActivity;
import de.geithonline.android.basics.preferences.IconOnlyPreference;

/**
 * This fragment shows the preferences for the first header.
 */
public class BattPreferencesFragment extends MyAbstractPreferenceFragment {
	private static final int REQUESTCODE_START_STYL_RECYCLER = 987;
	// private ListPreference stylePref;
	private IconOnlyPreference stylePreview;
	private int level = 66;
	// private SharedPreferences prefs;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences_style);

		// adding an additional imageview
		stylePreview = (IconOnlyPreference) findPreference("stylePreview");
		stylePreview.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(final Preference preference) {
				final Intent i = new Intent(getActivity(), StyleListRecyclerActivity.class);
				// open Recyclerview
				startActivityForResult(i, REQUESTCODE_START_STYL_RECYCLER);
				return true;
			}
		});
		// initializing Members
		// stylePref = (ListPreference) findPreference(Settings.KEY_BATT_STYLE);
		// // changelistener auf stylepicker
		// stylePref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
		//
		// @Override
		// public boolean onPreferenceChange(final Preference preference, final Object newStyle) {
		// enableSettingsForStyle((String) newStyle);
		// return true;
		// }
		// });
		// initialize Properties
		Log.i(this.getClass().getSimpleName(), "Initializing Style -> " + Settings.getStyle());
		enableSettingsForStyle(Settings.getStyle());
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		if (requestCode == REQUESTCODE_START_STYL_RECYCLER) {
			Log.i("MENU", "Coming beack from Style Selection with recyclerview");
			redrawPreview();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void redrawPreview() {
		final String style = Settings.getStyle();
		level++;
		if (level > 100) {
			level = 0;
		}
		final Bitmap b = DrawerManager.getIconForDrawerForceDrawNew(style, Settings.getIconSize(), level);
		if (b != null) {
			// stylePref.setIcon(BitmapHelper.bitmapToIcon(b));
			stylePreview.setImage(b);
			stylePreview.setTitle(style + "    (click image to choose)");
		}
	}

	private void enableSettingsForStyle(final String style) {
		// Ausgewählte Batterie zeichnen
		redrawPreview();

		final IBitmapDrawer drawer = DrawerManager.getDrawer(style);
		// was supported der style?
		handleAvailability(findPreference("show_zeiger"), drawer.supportsShowPointer());
		handleAvailability(findPreference("color_zeiger"), drawer.supportsPointerColor());

		handleAvailability(findPreference("levelStyles"), drawer.supportsLevelStyle());
		handleAvailability(findPreference("levelMode"), drawer.supportsLevelStyle());
		handleAvailability(findPreference("showExtraLevelBars"), drawer.supportsExtraLevelBars());

		handleAvailability(findPreference("show_rand"), drawer.supportsShowRand());
		handleAvailability(findPreference("glowScala"), drawer.supportsGlowScala());

		handleAvailability(findPreference("showVoltmeter"), drawer.supportsVoltmeter());
		handleAvailability(findPreference("showThermometer"), drawer.supportsThermometer());

		// pro Features
		handlePremium(findPreference("levelMode"));
		handlePremium(findPreference("levelStyles"));
		handlePremium(findPreference("showVoltmeter"));
		handlePremium(findPreference("showThermometer"));
	}

	private static void handlePremium(final Preference preference) {
		if (Settings.isPremium()) {
			// preference.setSummary("");
		} else {
			preference.setSummary(R.string.premiumOnly);
			preference.setEnabled(false);
		}
	}

	private static void handleAvailability(final Preference preference, final boolean available) {
		if (available) {
			// Nothing so far ---- preference.setSummary("");
		} else {
			preference.setSummary("Not available for this battery-style");
		}
		preference.setEnabled(available);
	}

	@Override
	public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
		redrawPreview();
	}
}
