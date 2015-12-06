package de.geithonline.abattlwp.widget;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import de.geithonline.abattlwp.R;
import de.geithonline.abattlwp.settings.DrawerManager;
import de.geithonline.abattlwp.settings.Settings;

public class MyBatteryWidgetProvider extends AppWidgetProvider {
	private static final String INTERNAL_BATTERY_UPDATE = "de.geithonline.abattlwp.action.UPDATE";
	private static int batteryLevel = 0;
	private static int i = 0;
	private static int millies;

	private static void log(final String s) {
		Log.i("MyBatteryWidgetProvider", s);
	}

	@Override
	public void onAppWidgetOptionsChanged(final Context context, final AppWidgetManager appWidgetManager, final int appWidgetId, final Bundle newOptions) {
		log("onAppWidgetOptionsChanged()");
		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
	}

	@Override
	public void onEnabled(final Context context) {
		super.onEnabled(context);
		log("onEnabled()");
		turnAlarmOnOff(context, true);
		context.startService(new Intent(context, MyBatteryWidgetUpdateService.class));
	}

	@Override
	public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		log("onUpdate()");
		context.startService(new Intent(context, MyBatteryWidgetUpdateService.class));
		// Sometimes when the phone is booting, onUpdate method gets called before onEnabled()
		updateViews(context);
	}

	@Override
	public void onDeleted(final Context context, final int[] appWidgetIds) {
		log("onDeleted()");
		super.onDeleted(context, appWidgetIds);
	}

	public static void turnAlarmOnOff(final Context context, final boolean turnOn) {
		log("turnAlarmOnOff =" + turnOn);
		final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		final Intent intent = new Intent(INTERNAL_BATTERY_UPDATE);
		final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		if (turnOn) { // Add extra 1 sec because sometimes ACTION_BATTERY_CHANGED is called after the first alarm
			alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, 300 * 1000, pendingIntent);
			log("Alarm set");
		} else {
			alarmManager.cancel(pendingIntent);
			log("Alarm disabled");
		}
	}

	public static synchronized void triggerUpdate(final Context context) {
		log("triggerUpdate");
		final Intent intent = new Intent(INTERNAL_BATTERY_UPDATE);
		context.sendBroadcast(intent);
	}

	private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
	private ScheduledFuture<?> future;

	public void triggerAnimationUpdate(final Context context, final long millies) {
		log("triggerAnimationUpdate ");
		future = scheduledExecutorService.schedule(new Runnable() {
			@Override
			public void run() {
				triggerUpdate(context);
			}
		}, millies, TimeUnit.MILLISECONDS);
	}

	@Override
	public void onReceive(final Context context, final Intent intent) {
		super.onReceive(context, intent);
		log("onReceive - Action=" + intent.getAction());
		// Battery Update
		if (intent.getAction().equals(INTERNAL_BATTERY_UPDATE)) {
			updateViews(context);
		}
	}

	@Override
	public void onDisabled(final Context context) {
		super.onDisabled(context);
		log("onDisabled");
		turnAlarmOnOff(context, false);
		context.stopService(new Intent(context, MyBatteryWidgetUpdateService.class));
	}

	private void calculateBatteryLevelInfo(final Intent intent) {
		batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		log("calculateBatteryLevelInfo Called - Level =" + batteryLevel);

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

	private void calculateBatteryLevel(final Context context) {
		final Intent intent = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		calculateBatteryLevelInfo(intent);
	}

	private synchronized void updateViews(final Context context) {
		calculateBatteryLevel(context);
		log("updateViews - Level =" + batteryLevel);
		// if (!Settings.isCharging || Settings.isAnimationEnabled() == false) {
		// if (future != null) {
		// future.cancel(true);
		// }
		draw(context, batteryLevel);
	}

	private void draw(final Context context, final int level) {
		final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		final String style = Settings.getStyle();
		final Bitmap bitmap = DrawerManager.getIconForDrawerForceDrawNew(style, 500, level);
		// Set the Bitmap
		remoteViews.setImageViewBitmap(R.id.update, bitmap);
		final ComponentName componentName = new ComponentName(context, MyBatteryWidgetProvider.class);
		final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		appWidgetManager.updateAppWidget(componentName, remoteViews);
	}
}