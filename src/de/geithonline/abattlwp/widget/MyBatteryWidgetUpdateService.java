package de.geithonline.abattlwp.widget;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import de.geithonline.abattlwp.settings.Settings;

public class MyBatteryWidgetUpdateService extends Service implements OnSharedPreferenceChangeListener {
	private static BroadcastReceiver screenOffReceiver;
	private static BroadcastReceiver screenOnReceiver;
	private static BroadcastReceiver userPresentReceiver;
	private static BroadcastReceiver mBatInfoReceiver;

	private static void log(final String s) {
		Log.i("MyBatteryWidgetUpdateService", s);
	}

	private static SharedPreferences prefs;

	@Override
	public IBinder onBind(final Intent arg0) {
		log("onBind");
		return null;
	}

	@Override
	public void onCreate() {
		log("onCreate");
		super.onCreate();
		registerPreferences();
		registerScreenOffReceiver();
		registerScreenOnReceiver();
		registerUserPresentReceiver();
		registermBatInfoReceiver();
	}

	@Override
	public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
		log("on PreferenceChanged");
		MyBatteryWidgetProvider.triggerUpdate(getApplicationContext());
	}

	@Override
	public void onDestroy() {
		log("onDestroy");
		super.onDestroy();
		prefs.unregisterOnSharedPreferenceChangeListener(this);
		unregisterReceiver(screenOffReceiver);
		unregisterReceiver(screenOnReceiver);
		unregisterReceiver(userPresentReceiver);
		unregisterReceiver(mBatInfoReceiver);
	}

	private void registerPreferences() {
		log("register Preferences");
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		prefs.registerOnSharedPreferenceChangeListener(this);
		Settings.initPrefs(prefs, getApplicationContext());
	}

	private void registermBatInfoReceiver() {
		log("registermBatInfoReceiver");
		mBatInfoReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(final Context context, final Intent intent) {
				log("mBatInfoReceiver.onReceive()");
				MyBatteryWidgetProvider.triggerUpdate(context);
			}
		};
		registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	}

	private void registerScreenOffReceiver() {
		screenOffReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(final Context context, final Intent intent) {
				log("screenOffReceiver.onReceive()");
				MyBatteryWidgetProvider.turnAlarmOnOff(context, false);
			}
		};
		registerReceiver(screenOffReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
	}

	private void registerScreenOnReceiver() {
		screenOnReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(final Context context, final Intent intent) {
				log("screenOnReceiver.onReceive()");

				final KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
				if (!keyguardManager.inKeyguardRestrictedInputMode()) {
					MyBatteryWidgetProvider.turnAlarmOnOff(context, true);
				}
			}
		};
		registerReceiver(screenOnReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
	}

	private void registerUserPresentReceiver() {
		userPresentReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(final Context context, final Intent intent) {
				log("userPresentReceiver.onReceive()");
				MyBatteryWidgetProvider.turnAlarmOnOff(context, true);
			}
		};
		registerReceiver(userPresentReceiver, new IntentFilter(Intent.ACTION_USER_PRESENT));
	}

}
