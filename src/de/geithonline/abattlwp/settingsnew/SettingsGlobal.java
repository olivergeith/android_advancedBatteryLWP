package de.geithonline.abattlwp.settingsnew;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/**
 * Settings die Global für die ganze App gelten
 * 
 * @author Oliver
 *
 */
public class SettingsGlobal {
	public SharedPreferences prefs;

	public boolean isCharging = false;
	public boolean isChargeUSB = false;
	public boolean isChargeAC = false;
	public boolean isChargeWireless = false;
	public int battTemperature = -1;
	public int battHealth = -1;
	public int battVoltage = -1;
	public int iconSize;

	/**
	 * Initializes some preferences on first run with defaults
	 * 
	 * @param preferences
	 */
	public SettingsGlobal(final Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		if (prefs.getBoolean("firstrun", true)) {
			Log.i("GEITH", "FirstRun --> initializing the SharedPreferences with some colors...");
			prefs.edit().putBoolean("firstrun", false).commit();
			// init something
		}
		iconSize = Math.round(getDisplayWidth(context) * 0.5f);
	}

	public void addPrefsChangeListener(final OnSharedPreferenceChangeListener listner) {
		prefs.registerOnSharedPreferenceChangeListener(listner);
	}

	public float getBattVoltage() {
		return battVoltage / 1000f;
	}

	public float getBattTemperature() {
		return battTemperature / 10f;
	}

	public String getBattTemperatureString() {
		return "Temperature is " + (float) battTemperature / 10 + " °C";
	}

	public String getBattHealthString() {
		return "Health is " + getHealthText(battHealth);
	}

	public String getBattVoltageString() {
		return "Voltage is " + (float) battVoltage / 1000 + "V";
	}

	private String getHealthText(final int health) {
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

	public boolean isPremium() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("muimerp", false);
	}

	public String getChargingText() {
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

	private int getDisplayWidth(final Context context) {
		final DisplayMetrics metrics = new DisplayMetrics();
		final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		if (metrics.widthPixels < metrics.heightPixels) {
			return metrics.widthPixels;
		} else {
			return metrics.heightPixels;
		}
	}

	public void setReadWritePermission(final boolean b) {
		prefs.edit().putBoolean("readWritePermission", b).commit();
	}

	public boolean isReadWritePermission() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean("readWritePermission", false);
	}

}
