package de.geithonline.abattlwp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import de.geithonline.abattlwp.settings.Settings;
import de.geithonline.abattlwp.utils.BitmapHelper;
import de.geithonline.abattlwp.utils.Toaster;
import de.geithonline.abattlwp.utils.URIHelper;

/**
 * This fragment shows the preferences for the second header.
 */
public class BackgroundPreferencesFragment extends PreferenceFragment {
	private final int PICK_IMAGE = 1;
	public static final String BACKGROUND_PICKER_KEY = "backgroundPicker";
	private Preference backgroundPicker;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_background);
		// connection the backgroundpicker with an intent
		backgroundPicker = findPreference(BACKGROUND_PICKER_KEY);
		backgroundPicker.setEnabled(PermissionRequester.isReadWritePermission());

		// Dürfen wir ins Filesystem?
		if (PermissionRequester.isReadWritePermission()) {
			backgroundPicker.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(final Preference preference) {
					final Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(Intent.createChooser(intent, "Select Background Picture"), PICK_IMAGE);
					return true;
				}
			});
			setBackgroundPickerData();
		} else { // nein wir dürfen nicht ins filesystem
			backgroundPicker.setSummary("Custom background selection not possible, because the Read/Write Permission to sdcard was NOT granted!");
		}
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent resultData) {
		super.onActivityResult(requestCode, resultCode, resultData);
		if (resultData == null) {
			Log.e(this.getClass().getSimpleName(), "onActivityResult: Data Recieved was null !!");
			return;
		}
		Log.i(this.getClass().getSimpleName(), "onActivityResult: Data Recieved: " + resultData.toString());

		if (resultCode != Activity.RESULT_OK) {
			Log.i(this.getClass().getSimpleName(), "No ImagePath Received -> Cancel");
			return;
		}
		if (requestCode != PICK_IMAGE) {
			Log.i(this.getClass().getSimpleName(), "No ImagePath Received -> RequestCode wrong...: " + requestCode);
			return;
		}

		final Uri selectedImage = resultData.getData();

		// Pfad zum Image suchen
		final String filePath = URIHelper.getPath(getActivity().getApplicationContext(), selectedImage);
		Log.i(this.getClass().getSimpleName(), "ImagePath Received via URIHelper! " + filePath);

		// und in die SharedPreferences schreiben
		final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

		if (sharedPref == null) {
			Log.e(this.getClass().getSimpleName(), "SharedPreferences were null!!");
			Toaster.showErrorToast(getActivity(), "Could not save imagepath " + filePath
					+ "Sharedfreferences not found!!! (null). Make sure you set the Wallpaper at least once before editing preferences of it (SystemSettings->Display->Wallpaper->LiveWallpaper->Choose BatteryLWP and set it!");
			return;
		}
		final SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(BACKGROUND_PICKER_KEY, filePath);
		Log.i(this.getClass().getSimpleName(), "ImagePath written to preferences: " + filePath);
		editor.commit();
		if (Settings.isDebuggingMessages()) {
			Toaster.showInfoToast(getActivity(), "SetBG to " + filePath);
		}

		// Summaries usw updaten
		setBackgroundPickerData();
	}

	private void setBackgroundPickerData() {
		final String filePath = Settings.getCustomBackgroundFilePath();
		final Bitmap b = BitmapHelper.getCustomImageSampled(filePath, 128, 128);
		if (b != null) {
			final Drawable dr = BitmapHelper.resizeToIcon128(b);
			backgroundPicker.setSummary(filePath);
			backgroundPicker.setIcon(dr);
		} else {
			backgroundPicker.setSummary(R.string.choose_background_summary);
			backgroundPicker.setIcon(R.drawable.icon); // TODO anderes Icon
		}
	}

}
