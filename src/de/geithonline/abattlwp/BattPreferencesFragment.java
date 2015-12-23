package de.geithonline.abattlwp;

import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;
import de.geithonline.abattlwp.bitmapdrawer.IBitmapDrawer;
import de.geithonline.abattlwp.settings.DrawerManager;
import de.geithonline.abattlwp.settings.Settings;
import de.geithonline.abattlwp.stylelistrecycler.StyleListRecyclerActivity;
import de.geithonline.android.basics.preferences.CoolListPreference;
import de.geithonline.android.basics.preferences.IconOnlyPreference;

/**
 * This fragment shows the preferences for the first header.
 */
public class BattPreferencesFragment extends MyAbstractPreferenceFragment {
	private static final String NOT_AVAILABLE_FOR_THIS_BATTERY_STYLE = "Not available for this battery-style";
	private static final int REQUESTCODE_START_STYL_RECYCLER = 987;
	// private ListPreference stylePref;
	private IconOnlyPreference stylePreview;
	private int level = 66;
	// private SharedPreferences prefs;
	private CoolListPreference styleVariante;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences_style);

		styleVariante = (CoolListPreference) findPreference(Settings.KEY_BATT_STYLE_VARIANTE);
		styleVariante.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(final Preference preference, final Object newValue) {
				// TODO ?
				return true;
			}
		});

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
		// initialize Properties
		Log.i(this.getClass().getSimpleName(), "Initializing Style -> " + Settings.getStyle());
		enableSettingsForStyle(Settings.getStyle(), Settings.KEY_BATT_STYLE);
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		if (requestCode == REQUESTCODE_START_STYL_RECYCLER) {
			Log.i("MENU", "Coming beack from Style Selection with recyclerview");
			enableSettingsForStyle(Settings.getStyle(), Settings.KEY_BATT_STYLE);
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
			stylePreview.setImage(b);
			stylePreview.setTitle(style);
		}
	}

	private void enableSettingsForStyle(final String style, final String key) {
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

		handleAvailability(findPreference("fontsizeInt"), drawer.supportsLevelNumberFontSizeAdjustment());
		handleAvailability(findPreference("fontsize100Int"), drawer.supportsLevelNumberFontSizeAdjustment());

		if (key.equals(Settings.KEY_BATT_STYLE_VARIANTE)) {
			handleStyleVariante(style, Settings.getStyleVariante());
		} else {
			handleStyleVariante(style, null);
		}
	}

	private void handleStyleVariante(final String style, final String newVariante) {
		final IBitmapDrawer drawer = DrawerManager.getDrawer(style);
		if (drawer.supportsVariants()) {
			// Erstmal die Variantenbox füllen
			final List<String> list = drawer.getVariants();
			styleVariante.setEntries(getVariantsArray(list));
			styleVariante.setEntryValues(getVariantsArray(list));
			styleVariante.setEnabled(true);
			getPreferenceScreen().addPreference(styleVariante);
			// übergbebene Variante null?
			if (newVariante == null) {
				// dann schauen wir, ob wir für diesen Drawer schon eine Variante gespeichert haben
				final String currentVarianteInSettings = Settings.getStyleVariante(drawer.getClass().getSimpleName());
				if (list.contains(currentVarianteInSettings)) {
					styleVariante.setValue(currentVarianteInSettings);
				} else {
					styleVariante.setValueIndex(0);
				}
			} else {
				// eine neue Variante wurde selektiert...die speichern wir unter dem Key für den Drawer
				Settings.saveStyleVariante(drawer.getClass().getSimpleName(), newVariante);
				// styleVariante.setValue(newVariante);
			}
		} else {
			styleVariante.setEntries(new CharSequence[] { NOT_AVAILABLE_FOR_THIS_BATTERY_STYLE });
			styleVariante.setEntryValues(new CharSequence[] { NOT_AVAILABLE_FOR_THIS_BATTERY_STYLE });
			styleVariante.setValueIndex(0);
			styleVariante.setEnabled(false);
			getPreferenceScreen().removePreference(styleVariante);
		}
	}

	private static void handleAvailability(final Preference preference, final boolean available) {
		if (available) {
			// Nothing so far ---- preference.setSummary("");
		} else {
			preference.setSummary(NOT_AVAILABLE_FOR_THIS_BATTERY_STYLE);
		}
		preference.setEnabled(available);
	}

	@Override
	public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
		enableSettingsForStyle(Settings.getStyle(), key);
	}

	private CharSequence[] getVariantsArray(final List<String> list) {
		final CharSequence[] array = new CharSequence[list.size()];
		int i = 0;
		for (final String l : list) {
			array[i++] = l;
		}
		return array;
	}

}
