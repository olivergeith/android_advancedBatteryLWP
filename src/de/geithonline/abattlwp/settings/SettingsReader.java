package de.geithonline.abattlwp.settings;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import de.geithonline.abattlwp.BackgroundPreferencesFragment;
import de.geithonline.abattlwp.R;

public class SettingsReader {
	private final SharedPreferences prefs;
	public static final int ANIMATION_STYLE_0_TO_100 = 1;
	public static final int ANIMATION_STYLE_0_TO_LEVEL = 2;

	public static final int ORIENTATION_BOTTOM = 0;
	public static final int ORIENTATION_LEFT = 90;
	public static final int ORIENTATION_RIGHT = 270;

	public static final int BATT_STATUS_STYLE_TEMP_VOLT_HEALTH = 0;
	public static final int BATT_STATUS_STYLE_TEMP_VOLT = 1;
	public static final int BATT_STATUS_STYLE_TEMP = 2;
	public static final int BATT_STATUS_STYLE_VOLT = 3;

	/**
	 * Initializes some preferences on first run with defaults
	 * 
	 * @param preferences
	 */
	public SettingsReader(final SharedPreferences preferences) {
		prefs = preferences;
		if (prefs.getBoolean("firstrun", true)) {
			Log.i("GEITH", "FirstRun --> initializing the SharedPreferences with some colors...");
			prefs.edit().putBoolean("firstrun", false).commit();
			// init colors
			prefs.edit().putInt("charge_color", Color.GREEN).commit();
			prefs.edit().putInt("battery_color", Color.WHITE).commit();
			prefs.edit().putInt("background_color", Color.DKGRAY).commit();
			prefs.edit().putInt("battery_color_mid", Color.YELLOW).commit();
			prefs.edit().putInt("battery_color_low", Color.RED).commit();
			prefs.edit().putInt("color_zeiger", Color.WHITE).commit();
		}
	}

	public boolean isShowStatus() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("show_status", true);
	}

	public int getStatusStyle() {
		if (prefs == null) {
			return 0;
		}
		final int stat = Integer.valueOf(prefs.getString("battStatusStyle", "0"));
		return stat;
	}

	public boolean isShowRand() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("show_rand", true);
	}

	public boolean isShowNumber() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("show_number", true);
	}

	public boolean isPremium() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("muimerp", false);
	}

	public int getChargeColor() {
		if (prefs == null) {
			return R.integer.COLOR_GREEN;
		}
		final int col = prefs.getInt("charge_color", R.integer.COLOR_GREEN);
		return col;
	}

	public boolean isUseChargeColors() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("charge_colors_enable", false);
	}

	public boolean isShowChargeState() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("charge_enable", true);
	}

	public boolean isVerticalPositionOffsetOnlyInPortrait() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("vertical_position_only_portrait", true);
	}

	public int getVerticalPositionOffset(final boolean inPortrait) {
		final int defVal = 0;
		if (prefs == null) {
			return defVal;
		}
		if (!inPortrait && isVerticalPositionOffsetOnlyInPortrait()) {
			return 0;
		}
		final int thr = Integer.valueOf(prefs.getString("vertical_position", "0"));
		return thr;
	}

	public boolean isAnimationEnabled() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("animation_enable", true);
	}

	public int getOrientation() {
		if (prefs == null) {
			return 0;
		}
		final int size = Integer.valueOf(prefs.getString("rotation", "0"));
		return size;
	}

	public boolean isShowZeiger() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("show_zeiger", true);
	}

	public int getAnimationDelaý() {
		if (prefs == null) {
			return 50;
		}
		final int thr = Integer.valueOf(prefs.getString("animation_delay", "50"));
		return thr;
	}

	public int getAnimationDelaýOnCurrentLevel() {
		if (prefs == null) {
			return 2500;
		}
		final int thr = Integer.valueOf(prefs.getString("animation_delay_level", "2500"));
		return thr;
	}

	public boolean isDebuggingMessages() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("debug2", false);
	}

	public boolean isDebugging() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("debug", false);
	}

	public int getAnimationStyle() {
		if (prefs == null) {
			return 1;
		}
		final int size = Integer.valueOf(prefs.getString("animationStyle", "1"));
		return size;
	}

	public boolean isCenteredBattery() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("centerBattery", true);
	}

	public int getFontSize() {
		if (prefs == null) {
			return 100;
		}
		final int size = Integer.valueOf(prefs.getString("fontsize", "100"));
		return size;
	}

	public int getFontSize100() {
		if (prefs == null) {
			return 100;
		}
		final int size = Integer.valueOf(prefs.getString("fontsize100", "100"));
		return size;
	}

	public boolean isColoredNumber() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("colored_numbers", false);
	}

	public boolean isGradientColors() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("gradient_colors", false);
	}

	public boolean isGradientColorsMid() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("gradient_colors_mid", false);
	}

	public int getMidThreshold() {
		if (prefs == null) {
			return 30;
		}
		final int thr = Integer.valueOf(prefs.getString("threshold_mid", "30"));
		return thr;
	}

	public int getLowThreshold() {
		if (prefs == null) {
			return 10;
		}
		final int thr = Integer.valueOf(prefs.getString("threshold_low", "10"));
		return thr;
	}

	public int getOpacity() {
		if (prefs == null) {
			return 128;
		}
		final int op = Integer.valueOf(prefs.getString("opacity", "128"));
		return op;
	}

	public int getBackgroundOpacity() {
		if (prefs == null) {
			return 128;
		}
		final int op = Integer.valueOf(prefs.getString("background_opacity", "128"));
		return op;
	}

	public int getBackgroundColor() {
		if (prefs == null) {
			return R.integer.COLOR_DARKGRAY;
		}
		final int col = prefs.getInt("background_color", R.integer.COLOR_DARKGRAY);
		return col;
	}

	public int getBattColor() {
		if (prefs == null) {
			return R.integer.COLOR_WHITE;
		}
		final int col = prefs.getInt("battery_color", R.integer.COLOR_WHITE);
		return col;
	}

	public int getZeigerColor() {
		if (prefs == null) {
			return R.integer.COLOR_WHITE;
		}
		final int col = prefs.getInt("color_zeiger", R.integer.COLOR_WHITE);
		return col;
	}

	public int getBattColorMid() {
		if (prefs == null) {
			return R.integer.COLOR_ORANGE;
		}
		final int col = prefs.getInt("battery_color_mid", R.integer.COLOR_ORANGE);
		return col;
	}

	public int getBattColorLow() {
		if (prefs == null) {
			return R.integer.COLOR_RED;
		}
		final int col = prefs.getInt("battery_color_low", R.integer.COLOR_RED);
		return col;
	}

	public String getStyle() {
		if (prefs == null) {
			return "ZoopaWideV3";
		}
		return prefs.getString("batt_style", "ZoopaWideV3");
	}

	public boolean isLoadCustomBackground() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("customBackground", true);
	}

	public String getCustomBackgroundFilePath() {
		if (prefs == null) {
			return "aaa";
		}
		final String filePath = prefs.getString(BackgroundPreferencesFragment.BACKGROUND_PICKER_KEY, "aaa");
		return filePath;
	}

	public int getBackgroundColor1() {
		if (prefs == null) {
			return R.integer.COLOR_BLACK;
		}
		final int col = prefs.getInt("color_plain_bgrnd", R.integer.COLOR_BLACK);
		return col;
	}

	public int getBackgroundColor2() {
		if (prefs == null) {
			return R.integer.COLOR_WHITE;
		}
		final int col = prefs.getInt("color2_plain_bgrnd", R.integer.COLOR_WHITE);
		return col;
	}

	public boolean isGradientBackground() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("gradientBackground", false);
	}
}
