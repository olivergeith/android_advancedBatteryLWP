package de.geithonline.abattlwp;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import de.geithonline.abattlwp.utils.Alerter;

public class PermissionRequester {

	private static final int MY_PERMISSIONS_REQUEST_INT = 99;

	private static boolean readWritePermission = false;

	private final Activity activity;

	public PermissionRequester(final Activity activity) {
		this.activity = activity;
	}

	/**
	 * Requests the {@link android.Manifest.permission#WRITE_EXTERNAL_STORAGE} permission. If an additional rationale should be displayed, the user has to
	 * launch the request from a SnackBar that includes additional information.
	 */
	public void requestPermission() {
		if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			// Permission has not been granted and must be requested.
			if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
				// Provide an additional rationale to the user if the permission was not granted
				// and the user would benefit from additional context for the use of the permission.
				// Display a SnackBar with a button to request the missing permission.
				Alerter.alertYesNo(activity,
						"Access to external storage is needed, to load and save custom background images for this livewallapaper...please grant this permission!",
						"Permission Request", //
						new DialogInterface.OnClickListener() { // yes clicked...now we open the request dialog

							@Override
							public void onClick(final DialogInterface dialog, final int which) {
								requestWritePersission();
							}
						}, // No clicked
						new DialogInterface.OnClickListener() {// No clicked
							@Override
							public void onClick(final DialogInterface dialog, final int which) {
								onNotGrantedPermission();
							}

						});

			} else {
				requestWritePersission();
			}
		} else {
			onGrantedPermission();
		}
	}

	private void requestWritePersission() {
		ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, MY_PERMISSIONS_REQUEST_INT);
	}

	public void onRequestPermissionsResult(final int requestCode, final String permissions[], final int[] grantResults) {
		switch (requestCode) {
		case MY_PERMISSIONS_REQUEST_INT: {
			// If request is cancelled, the result arrays are empty.
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				onGrantedPermission();
			} else {
				onNotGrantedPermission();
			}
			break;
		}
		}
	}

	private void onGrantedPermission() {
		setReadWritePermission(true);
	}

	private void onNotGrantedPermission() {
		setReadWritePermission(false);
	}

	private static void setReadWritePermission(final boolean b) {
		readWritePermission = b;
	}

	public static boolean isReadWritePermission() {
		return readWritePermission;
	}

}
