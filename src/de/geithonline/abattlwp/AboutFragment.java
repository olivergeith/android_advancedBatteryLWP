package de.geithonline.abattlwp;

import android.appwidget.AppWidgetManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import de.geithonline.abattlwp.settings.Settings;

public class AboutFragment extends PreferenceFragment {
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getIdOfCurrentWidget(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences_about);
		setSpecialThings();
	}

	private int getIdOfCurrentWidget(final Bundle savedInstanceState) {

		final Bundle extras = getActivity().getIntent().getExtras();

		int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
		if (extras != null) {
			widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		// If we have a Widget...we switch preferences to its preferences
		if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
			// final PreferenceManager prefMgr = getPreferenceManager();
			// final String prefsName = "Widget" + widgetId;
			// prefMgr.setSharedPreferencesName(prefsName);
			// prefMgr.setSharedPreferencesMode(PreferenceActivity.MODE_PRIVATE);
		}
		return widgetId;
	}

	private void setSpecialThings() {
		final Preference proBox = findPreference("premium");

		if (Settings.isPremium()) {
			proBox.setTitle("This is the Premium Version");
			proBox.setIcon(R.drawable.icon_premium);
		} else {
			proBox.setTitle("This is the Free Version");
			proBox.setIcon(R.drawable.icon);
		}
	}

}
