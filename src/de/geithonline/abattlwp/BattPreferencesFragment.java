package de.geithonline.abattlwp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import de.geithonline.abattlwp.bitmapdrawer.IBitmapDrawer;
import de.geithonline.abattlwp.settings.DrawerManager;
import de.geithonline.abattlwp.settings.Settings;
import de.geithonline.abattlwp.utils.BitmapHelper;
import de.geithonline.abattlwp.utils.Toaster;
import de.geithonline.abattlwp.utils.URIHelper;
import de.geithonline.android.basics.preferences.IconOnlyPreference;

/**
 * This fragment shows the preferences for the first header.
 */
public class BattPreferencesFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	private final int PICK_LOGO = 3;
	public static final String STYLE_PICKER_KEY = "batt_style";
	private ListPreference stylePref;
	private IconOnlyPreference stylePreview;
	private int level = 66;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_style);
		// adding an additional imageview
		stylePreview = (IconOnlyPreference) findPreference("stylePreview");
		stylePreview.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(final Preference preference) {
				redrawPreview();

				return false;
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
		// register for preference Changes
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity().getApplicationContext());
		prefs.registerOnSharedPreferenceChangeListener(this);
		// initialize Properties
		Log.i(this.getClass().getSimpleName(), "Initializing Style -> " + Settings.getStyle());
		enableSettingsForStyle(Settings.getStyle());
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent resultData) {
		super.onActivityResult(requestCode, resultCode, resultData);
		if (resultData == null) {
			Log.e(this.getClass().getSimpleName(), "onActivityResult: Data Recieved was null !!");
			return;
		}
		Log.i(this.getClass().getSimpleName(), "onActivityResult: Data Recieved: " + resultData.toString());

		if (resultCode != Activity.RESULT_OK) {
			Log.i(this.getClass().getSimpleName(), "No ImagePath Received -> Cancel");
			return;
		}
		if (requestCode != PICK_LOGO) {
			Log.i(this.getClass().getSimpleName(), "No ImagePath Received -> RequestCode wrong...: " + requestCode);
			return;
		}

		final Uri selectedImage = resultData.getData();

		// Pfad zum Image suchen
		final String filePath = URIHelper.getPath(getActivity().getApplicationContext(), selectedImage);
		Log.i(this.getClass().getSimpleName(), "ImagePath Received via URIHelper! " + filePath);

		// und in die SharedPreferences schreiben
		final SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getActivity().getApplicationContext());

		if (sharedPref == null) {
			Log.e(this.getClass().getSimpleName(), "SharedPreferences were null!!");
			Toaster.showErrorToast(getActivity(), "Could not save imagepath " + filePath
					+ "Sharedfreferences not found!!! (null). Make sure you set the Wallpaper at least once before editing preferences of it (SystemSettings->Display->Wallpaper->LiveWallpaper->Choose BatteryLWP and set it!");
			return;
		}
		final SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("logoPicker", filePath);
		Log.i(this.getClass().getSimpleName(), "ImagePath written to preferences: " + filePath);
		editor.commit();
		if (Settings.isDebuggingMessages()) {
			Toaster.showInfoToast(getActivity(), "SetBG to " + filePath);
		}
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
			preference.setSummary("");
		} else {
			preference.setSummary(R.string.premiumOnly);
		}
		preference.setEnabled(Settings.isPremium());
	}

	private static void handleAvailability(final Preference preference, final boolean available) {
		if (available) {
			// Nothing so far ---- preference.setSummary("");
		} else {
			preference.setSummary("Not available for this style");
		}
		preference.setEnabled(available);
	}

	@Override
	public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
		redrawPreview();

	}

	@Override
	public void onResume() {
		// enableSettingsForStyle(Settings.getStyle());
		super.onResume();
	}
}
