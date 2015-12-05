package de.geithonline.abattlwp.settingsnew;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.BatteryManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import de.geithonline.abattlwp.BackgroundPreferencesFragment;
import de.geithonline.abattlwp.R;
import de.geithonline.abattlwp.bitmapdrawer.IBitmapDrawer;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZMode;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZStyle;
import de.geithonline.abattlwp.settings.DrawerManager;

public class SettingsBatteryOptions {
	public static SharedPreferences prefs;
	private static String style = "aaa";
	private static IBitmapDrawer bitmapDrawer;
	public static final int ANIMATION_STYLE_0_TO_100 = 1;
	public static final int ANIMATION_STYLE_0_TO_LEVEL = 2;

	public static boolean isCharging = false;
	public static boolean isChargeUSB = false;
	public static boolean isChargeAC = false;
	public static boolean isChargeWireless = false;
	public static int battTemperature = -1;
	public static int battHealth = -1;
	public static int battVoltage = -1;
	public static int iconSize;

	public static final int BATT_STATUS_STYLE_TEMP_VOLT_HEALTH = 0;
	public static final int BATT_STATUS_STYLE_TEMP_VOLT = 1;
	public static final int BATT_STATUS_STYLE_TEMP = 2;
	public static final int BATT_STATUS_STYLE_VOLT = 3;

	public static boolean isKeepAspectRatio() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("keepAspectRatio", true);
	}

	public static boolean isScaleTransparent() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("scale_numbers_transparent", false);
	}

	public static int getScaleColor() {
		if (prefs == null) {
			return R.integer.COLOR_WHITE;
		}
		final int col = prefs.getInt("scale_color", R.integer.COLOR_WHITE);
		return col;
	}

	public static int getBattStatusColor() {
		if (prefs == null) {
			return R.integer.COLOR_WHITE;
		}
		final int col = prefs.getInt("status_color", R.integer.COLOR_WHITE);
		return col;
	}

	public static boolean isShowStatus() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("show_status", false);
	}

	private static int getStatusStyle() {
		if (prefs == null) {
			return 0;
		}
		final int stat = Integer.valueOf(prefs.getString("battStatusStyle", "0"));
		return stat;
	}

	public static String getBattStatusCompleteShort() {
		switch (getStatusStyle()) {
		case BATT_STATUS_STYLE_VOLT:
			return "Battery: " + (float) (battVoltage / 10) / 100 + "V";
		case BATT_STATUS_STYLE_TEMP:
			return "Battery: " + (float) battTemperature / 10 + "°C";
		case BATT_STATUS_STYLE_TEMP_VOLT:
			return "Battery: " + (float) battTemperature / 10 + "°C, " + (float) (battVoltage / 10) / 100 + "V";
		default:
		case BATT_STATUS_STYLE_TEMP_VOLT_HEALTH:
			return "Battery: health " + getHealthText(battHealth) + ", " + (float) battTemperature / 10 + "°C, " + (float) (battVoltage / 10) / 100 + "V";
		}
	}

	public static float getBattVoltage() {
		return battVoltage / 1000f;
	}

	public static float getBattTemperature() {
		return battTemperature / 10f;
	}

	public static String getBattTemperatureString() {
		return "Temperature is " + (float) battTemperature / 10 + " °C";
	}

	public static String getBattHealthString() {
		return "Health is " + getHealthText(battHealth);
	}

	public static String getLevelStyleString() {
		if (prefs == null) {
			return "Normal";
		}
		return prefs.getString("levelStyles", "Normal");
	}

	public static String getLevelModeString() {
		if (prefs == null) {
			return "1";
		}
		return prefs.getString("levelMode", "1");
	}

	public static EZMode getLevelMode() {
		switch (getLevelModeString()) {
		default:
		case "1":
			return EZMode.Einer;
		case "5":
			return EZMode.Fuenfer;
		case "10":
			return EZMode.Zehner;
		}
	}

	public static EZStyle getLevelStyle() {
		switch (getLevelStyleString()) {
		default:
		case "Normal":
			return EZStyle.sweep;
		case "Normal (alpha)":
			return EZStyle.sweep_withAplpah;
		case "Normal (outline)":
			return EZStyle.sweep_withOutline;
		case "Only activ segments":
			return EZStyle.segmented_onlyactive;
		case "All segments (outline)":
			return EZStyle.segmented_all;
		case "All segments (alpha)":
			return EZStyle.segmented_all_alpha;
		}
	}

	public static String getBattVoltageString() {
		return "Voltage is " + (float) battVoltage / 1000 + "V";
	}

	private static String getHealthText(final int health) {
		switch (health) {
		case BatteryManager.BATTERY_HEALTH_GOOD:
			return "good";
		case BatteryManager.BATTERY_HEALTH_OVERHEAT:
			return "overheat";
		case BatteryManager.BATTERY_HEALTH_DEAD:
			return "dead";
		case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
			return "overvoltage";
		case BatteryManager.BATTERY_HEALTH_COLD:
			return "cold";
		case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
			return "failure";

		default:
			return "unknown";
		}
	}

	public static boolean isShowRand() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("show_rand", true);
	}

	public static boolean isShowNumber() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("show_number", true);
	}

	public static boolean isPremium() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("muimerp", false);
	}

	public static int getChargeColor() {
		if (prefs == null) {
			return R.color.green_128;
		}
		final int col = prefs.getInt("charge_color", R.color.green_128);
		return col;
	}

	public static boolean isUseChargeColors() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("charge_colors_enable", false);
	}

	public static boolean isShowChargeState() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("showChargeStatus", true);
	}

	public static String getChargingText() {
		String text;
		if (isChargeUSB) {
			text = "Charging on USB";
		} else if (isChargeAC) {
			text = "Charging on AC";
		} else if (isChargeWireless) {
			text = "Charging wireless";
		} else {
			text = "Charging...";
		}
		return text;
	}

	public static int getVerticalPositionOffset(final boolean isPortrait) {
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

	public static boolean isAnimationEnabled() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("animation_enable", true);
	}

	public static boolean isShowZeiger() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("show_zeiger", true);
	}

	public static int getAnimationDelaý() {
		if (prefs == null) {
			return 50;
		}
		final int thr = prefs.getInt("animation_delayInt", 50);
		return thr;
	}

	public static int getAnimationDelaýOnCurrentLevel() {
		if (prefs == null) {
			return 2500;
		}
		final int thr = prefs.getInt("animation_delay_levelInt", 2500);
		return thr;
	}

	public static boolean isDebuggingMessages() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("debug2", false);
	}

	public static boolean isDebugging() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("debug", false);
	}

	public static int getAnimationStyle() {
		if (prefs == null) {
			return 1;
		}
		final int size = Integer.valueOf(prefs.getString("animationStyle", "1"));
		return size;
	}

	public static boolean isCenteredBattery() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("centerBattery", true);
	}

	public static int getFontSize() {
		if (prefs == null) {
			return 100;
		}
		final int size = prefs.getInt("fontsizeInt", 100);
		return size;
	}

	public static int getFontSize100() {
		if (prefs == null) {
			return 100;
		}
		final int size = prefs.getInt("fontsize100Int", 100);
		return size;
	}

	public static boolean isColoredNumber() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("colored_numbers", false);
	}

	public static boolean isGradientColors() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("gradient_colors", false);
	}

	public static boolean isGradientColorsMid() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("gradient_colors_mid", true);
	}

	public static int getMidThreshold() {
		if (prefs == null) {
			return 30;
		}
		final int thr = prefs.getInt("threshold_mid_Int", 30);
		return thr;
	}

	public static int getLowThreshold() {
		if (prefs == null) {
			return 10;
		}
		final int thr = prefs.getInt("threshold_low_Int", 10);
		return thr;
	}

	public static int getBackgroundOpacity() {
		return Color.alpha(getBackgroundColor());
	}

	public static int getBackgroundColor() {
		if (prefs == null) {
			return R.color.primary_128;
		}
		final int col = prefs.getInt("background_color", R.color.primary_128);
		return col;
	}

	public static int getBattColor() {
		if (prefs == null) {
			return R.integer.COLOR_WHITE;
		}
		final int col = prefs.getInt("battery_color", R.integer.COLOR_WHITE);
		return col;
	}

	public static int getZeigerColor() {
		if (prefs == null) {
			return R.integer.COLOR_WHITE;
		}
		final int col = prefs.getInt("color_zeiger", R.integer.COLOR_WHITE);
		return col;
	}

	public static int getBattColorMid() {
		if (prefs == null) {
			return R.integer.COLOR_ORANGE;
		}
		final int col = prefs.getInt("battery_color_mid", R.integer.COLOR_ORANGE);
		return col;
	}

	public static int getBattColorLow() {
		if (prefs == null) {
			return R.integer.COLOR_RED;
		}
		final int col = prefs.getInt("battery_color_low", R.integer.COLOR_RED);
		return col;
	}

	// #####################################################################################
	// Styles
	// #####################################################################################
	public static IBitmapDrawer getBatteryStyle() {
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

	public static String getStyle() {
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
	public static String getCustomBackgroundFilePath() {
		if (prefs == null) {
			return "aaa";
		}
		final String filePath = prefs.getString(BackgroundPreferencesFragment.BACKGROUND_PICKER_KEY, "aaa");
		return filePath;
	}

	/**
	 * Initializes some preferences on first run with defaults
	 * 
	 * @param preferences
	 */
	public static void initPrefs(final SharedPreferences preferences, final Context context) {
		prefs = preferences;
		if (prefs.getBoolean("firstrun", true)) {
			Log.i("GEITH", "FirstRun --> initializing the SharedPreferences with some colors...");
			prefs.edit().putBoolean("firstrun", false).commit();
			// init colors
			prefs.edit().putBoolean("show_status", false).commit();
		}
		iconSize = Math.round(getDisplayWidth(context) * 0.5f);
	}

	public static int getIconSize() {
		return iconSize;
	}

	private static int getDisplayWidth(final Context context) {
		final DisplayMetrics metrics = new DisplayMetrics();
		final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		if (metrics.widthPixels < metrics.heightPixels) {
			return metrics.widthPixels;
		} else {
			return metrics.heightPixels;
		}
	}

	public static float getPortraitResizeFactor() {
		if (prefs == null) {
			return 0.75f;
		}
		final int size = prefs.getInt("resizePortraitInt", 75);
		return size / 100f;
	}

	public static float getLandscapeResizeFactor() {
		if (prefs == null) {
			return 0.75f;
		}
		final int size = prefs.getInt("resizeLandscapeInt", 75);
		return size / 100f;
	}

	public static boolean isShowGlowScala() {
		if (prefs == null) {
			return true;
		}
		return prefs.getBoolean("glowScala", true);
	}

	public static int getGlowScalaColor() {
		if (prefs == null) {
			return R.color.accent;
		}
		final int col = prefs.getInt("glowScalaColor", R.color.accent);
		return col;
	}

	public static boolean isShowExtraLevelBars() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("showExtraLevelBars", false);
	}

	public static boolean isShowVoltmeter() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("showVoltmeter", false);
	}

	public static boolean isShowThermometer() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("showThermometer", false);
	}

	public static void setReadWritePermission(final boolean b) {
		prefs.edit().putBoolean("readWritePermission", b).commit();
	}

	public static boolean isReadWritePermission() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("readWritePermission", false);
	}

}
