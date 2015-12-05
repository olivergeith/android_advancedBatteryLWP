package de.geithonline.abattlwp;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;
import de.geithonline.abattlwp.bitmapdrawer.IBitmapDrawer;
import de.geithonline.abattlwp.settings.DrawerManager;
import de.geithonline.abattlwp.settings.Settings;
import de.geithonline.abattlwp.utils.BitmapHelper;
import de.geithonline.android.basics.preferences.IconOnlyPreference;

/**
 * This fragment shows the preferences for the first header.
 */
public class BattPreferencesFragment extends MyAbstractPreferenceFragment {
	public static final String STYLE_PICKER_KEY = "batt_style";
	private ListPreference stylePref;
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
			private long lastclick = 0;

			@Override
			public boolean onPreferenceClick(final Preference preference) {
				final long time = System.currentTimeMillis();
				final long diff = time - lastclick;
				lastclick = time;
				Log.i("Click", "Time was " + diff);
				if (diff < 150) {
					Log.i("DoubleClick", "Time was " + diff);
				}
				redrawPreview();
				return true;
			}
		});
		// initializing Members
		stylePref = (ListPreference) findPreference(STYLE_PICKER_KEY);
		// changelistener auf stylepicker
		stylePref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(final Preference preference, final Object newStyle) {
				enableSettingsForStyle((String) newStyle);
				return true;
			}
		});
		// initialize Properties
		Log.i(this.getClass().getSimpleName(), "Initializing Style -> " + Settings.getStyle());
		enableSettingsForStyle(Settings.getStyle());
	}

	private void redrawPreview() {
		final String style = Settings.getStyle();
		level++;
		if (level > 100) {
			level = 0;
		}
		final Bitmap b = DrawerManager.getIconForDrawerForceDrawNew(style, Settings.getIconSize(), level);
		if (b != null) {
			stylePref.setIcon(BitmapHelper.bitmapToIcon(b));
			stylePreview.setImage(b);
			// stylePreview.setTitle("Preview: " + style);
		}
	}

	private void enableSettingsForStyle(final String style) {
		// Ausgewählte Batterie zeichnen
		redrawPreview();

		final IBitmapDrawer drawer = DrawerManager.getDrawer(style);
		stylePref.setSummary(style);
		// was supported der style
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
