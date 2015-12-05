package de.geithonline.abattlwp.settingsnew;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {
	private static SettingsGlobal globalSettings = null;
	private static SettingsLWP lwpSettings = null;

	public static SettingsGlobal getGlobalSettings(final Context context) {
		if (globalSettings == null) {
			globalSettings = new SettingsGlobal(context);
		}
		return globalSettings;
	}

	public static SettingsLWP getLWPSettings(final SharedPreferences prefs, final Context context) {
		if (lwpSettings == null) {
			lwpSettings = new SettingsLWP(prefs, context);
		}
		return lwpSettings;
	}

}
