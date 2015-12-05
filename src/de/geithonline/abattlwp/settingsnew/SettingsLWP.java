package de.geithonline.abattlwp.settingsnew;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;
import de.geithonline.abattlwp.BackgroundPreferencesFragment;
import de.geithonline.abattlwp.bitmapdrawer.IBitmapDrawer;
import de.geithonline.abattlwp.settings.DrawerManager;

public class SettingsLWP {
	public SharedPreferences prefs;
	private String style = "aaa";
	private IBitmapDrawer bitmapDrawer;
	public final static int ANIMATION_STYLE_0_TO_100 = 1;
	public final static int ANIMATION_STYLE_0_TO_LEVEL = 2;

	/**
	 * Initializes some preferences on first run with defaults
	 * 
	 * @param preferences
	 */
	public SettingsLWP(final SharedPreferences prefs, final Context context) {
		this.prefs = prefs;
		if (prefs.getBoolean("firstrun", true)) {
			Log.i("GEITH", "FirstRun --> initializing the SharedPreferences with some colors...");
			prefs.edit().putBoolean("firstrun", false).commit();
			// init Something
			prefs.edit().putBoolean("show_status", false).commit();
		}

	}

	public void addPrefsChangeListener(final OnSharedPreferenceChangeListener listner) {
		prefs.registerOnSharedPreferenceChangeListener(listner);
	}

	public boolean isKeepAspectRatio() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("keepAspectRatio", true);
	}

	public int getVerticalPositionOffset(final boolean isPortrait) {
		final int defVal = 0;
		if (prefs == null) {
			return defVal;
		}
		if (isPortrait) {
			return prefs.getInt("vertical_positionInt", defVal);
		} else {
			return prefs.getInt("vertical_position_landscapeInt", defVal);
		}
	}

	public boolean isCenteredBattery() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("centerBattery", true);
	}

	// #######################################################################

	public boolean isAnimationEnabled() {

		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("animation_enable", true);
	}

	public int getAnimationDelaý() {
		if (prefs == null) {
			return 50;
		}
		final int thr = prefs.getInt("animation_delayInt", 50);
		return thr;
	}

	public int getAnimationDelaýOnCurrentLevel() {
		if (prefs == null) {
			return 2500;
		}
		final int thr = prefs.getInt("animation_delay_levelInt", 2500);
		return thr;
	}

	public int getAnimationStyle() {
		if (prefs == null) {
			return 1;
		}
		final int size = Integer.valueOf(prefs.getString("animationStyle", "1"));
		return size;
	}

	// #####################################################################################
	// Styles
	// #####################################################################################
	public IBitmapDrawer getBatteryStyle() {
		// wenns den drawer noch nicht gibt, oder der style sich geändert hat
		if (bitmapDrawer == null || !style.equals(getStyle())) {
			// getting Style from Settings
			style = getStyle();
			// returning the right Style
			bitmapDrawer = DrawerManager.getDrawer(style);
			return bitmapDrawer;
		}
		return bitmapDrawer;
	}

	public String getStyle() {
		if (prefs == null) {
			return "ClockV3";
		}
		return prefs.getString("batt_style", "ClockV3");
	}

	// #####################################################################################
	// Custom Background
	// #####################################################################################

	/**
	 * @return Bitmap or null...
	 */
	public String getCustomBackgroundFilePath() {
		if (prefs == null) {
			return "aaa";
		}
		final String filePath = prefs.getString(BackgroundPreferencesFragment.BACKGROUND_PICKER_KEY, "aaa");
		return filePath;
	}

}
