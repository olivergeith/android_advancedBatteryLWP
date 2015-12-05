package de.geithonline.abattlwp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBatteryWidgetProvider extends AppWidgetProvider {

	private static final String LOG = "BatteryWidget";

	@Override
	public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {

		Log.i(LOG, "onUpdate method called");
		// Get all ids
		final ComponentName thisWidget = new ComponentName(context, MyBatteryWidgetProvider.class);
		final int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		// Build the intent to call the service
		final Intent intent = new Intent(context.getApplicationContext(), MyBatteryWidgetUpdateService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

		// Update the widgets via the service
		context.startService(intent);
	}

}