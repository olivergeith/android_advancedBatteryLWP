package de.geithonline.abattlwp;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import de.geithonline.abattlwp.settings.Settings;
import de.geithonline.abattlwp.utils.IntendHelper;
import de.geithonline.abattlwp.utils.Toaster;

public class LiveWallpaperPreferencesActivity extends AppCompatPreferenceActivity {

	private BillingManager billingManager;
	private static final int REQUEST = 999;
	private Button buttonSetWP;
	private PermissionRequester permissionRequester;
	public static SharedPreferences prefs;

	@Override
	protected boolean isValidFragment(final String fragmentName) {
		Log.i("GEITH", "isValidFragment Called for " + fragmentName);

		return AboutFragment.class.getName().equals(fragmentName) //
				|| BattPreferencesFragment.class.getName().equals(fragmentName) //
				|| BattSizePositionFragment.class.getName().equals(fragmentName) //
				|| BackgroundPreferencesFragment.class.getName().equals(fragmentName);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onRequestPermissionsResult(final int requestCode, final String permissions[], final int[] grantResults) {
		permissionRequester.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Homebutton im Action bar
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Unsere eigenes PreferenceFile für den LWPService
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		// prefs = getSharedPreferences(Settings.LWP_PREFERENCE_FILE, MODE_PRIVATE);
		Settings.initPrefs(prefs, getApplicationContext());

		// getPreferenceManager().setSharedPreferencesName("preference_file_name");

		// Setting up Permission requester
		permissionRequester = new PermissionRequester(this);
		permissionRequester.requestPermission();

		// Setting up Billing
		billingManager = new BillingManager(this);
		final boolean isPremium = billingManager.isPremium();

		// A button to set us as Wallpaper
		buttonSetWP = new Button(this);
		buttonSetWP.setText("Set Wallpaper");
		buttonSetWP.setBackgroundResource(R.color.primary);
		buttonSetWP.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				setWallpaper();
			}
		});
		// A View because there might be another button (billing)
		final LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		ll.setGravity(Gravity.CENTER);
		if (!isMyServiceRunning(LiveWallpaperService.class.getName())) {
			ll.addView(buttonSetWP);
		} else {
		}

		// Add a button to the header list.
		if (!isPremium) {
			final Button button = billingManager.getButton();
			ll.addView(button);
		}
		// set view with buttons to the list footer
		setListFooter(ll);
	}

	/**
	 * Populate the activity with the top-level headers.
	 */
	@Override
	public void onBuildHeaders(final List<Header> target) {
		super.onBuildHeaders(target);
		loadHeadersFromResource(R.xml.preferences_header, target);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		billingManager.onDestroy();
	}

	@SuppressLint("InlinedApi")
	private void setWallpaper() {
		final Intent i = new Intent();
		// Building the set wallpaper intend depending on SDK Version
		if (Build.VERSION.SDK_INT >= 16) {
			i.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
			final String p = LiveWallpaperService.class.getPackage().getName();
			final String c = LiveWallpaperService.class.getCanonicalName();
			i.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(p, c));
		} else {
			i.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
		}
		// Checking if intend is available
		if (IntendHelper.isAvailable(getApplicationContext(), i)) {
			startActivityForResult(i, REQUEST);
		} else {
			Toaster.showErrorToast(this, "Intend not available on your device/rom: " + i);
		}
	}

	private boolean isMyServiceRunning(final String className) {
		final ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (final RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			// Log.i("GEITH", "Sevice = " + service.service.getClassName());
			if (className.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}