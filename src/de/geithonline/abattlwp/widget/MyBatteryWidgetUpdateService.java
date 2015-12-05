package de.geithonline.abattlwp.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import de.geithonline.abattlwp.R;
import de.geithonline.abattlwp.settings.DrawerManager;
import de.geithonline.abattlwp.settings.Settings;

public class MyBatteryWidgetUpdateService extends Service {
	private static final String LOG = "BatteryWidgetUpdateService";

	private int level = 0;
	private SharedPreferences prefs;

	public MyBatteryWidgetUpdateService() {
	}

	@Override
	public void onCreate() {
		Log.i(LOG, "onCreate Called");
		super.onCreate();
		registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		prefs = getSharedPreferences(Settings.LWP_PREFERENCE_FILE, MODE_PRIVATE);
		Settings.initPrefs(prefs, getApplicationContext());
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mBatInfoReceiver);
		super.onDestroy();
	}

	private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(final Context arg0, final Intent intent) {
			level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			Log.i(LOG, "onReceive Level Called - Level =" + level);

			// final int scale =
			// intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			final int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
			// Are we charging charged?
			Settings.isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
			// How are we charging?
			final int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
			Settings.isChargeUSB = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
			Settings.isChargeWireless = chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS;
			Settings.isChargeAC = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
			Settings.battTemperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
			Settings.battHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
			Settings.battVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
		}
	};

	@Override
	public void onStart(final Intent intent, final int startId) {
		Log.i(LOG, "onStart Called");

		final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());

		final int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		// final ComponentName thisWidget = new ComponentName(getApplicationContext(), MyBatteryWidgetProvider.class);

		// final int[] allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);
		// Log.i(LOG, "Direct" + String.valueOf(allWidgetIds2.length));
		// for (final int widgetId : allWidgetIds2) {
		// Log.i(LOG, "WidgetID=" + widgetId);
		// }

		// Log.i(LOG, "From Intent" + String.valueOf(allWidgetIds.length));
		for (final int widgetId : allWidgetIds) {
			Log.i(LOG, "WidgetID=" + widgetId);
			// Get the RemoteView
			final RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget_layout);
			final String style = Settings.getStyle();
			final Bitmap bitmap = DrawerManager.getIconForDrawerForceDrawNew(style, 500, level);
			// Set the Bitmap
			remoteViews.setImageViewBitmap(R.id.update, bitmap);

			// Register an onClickListener
			final Intent clickIntent = new Intent(getApplicationContext(), MyBatteryWidgetProvider.class);
			clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

			final PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
		stopSelf();
	}

	@Override
	public IBinder onBind(final Intent intent) {
		return null;
	}
}
