package de.geithonline.abattlwp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import de.geithonline.abattlwp.R;

public class Toaster {

	private static final int TYPE_ERROR = 0;
	private static final int TYPE_INFO = 1;

	public static void showInfoToast(final Activity activity, final String msg) {
		showToast(TYPE_INFO, activity, msg);
	}

	public static void showErrorToast(final Activity activity, final String msg) {
		showToast(TYPE_ERROR, activity, msg);
	}

	private static void showToast(final int typ, final Activity activity, final String msg) {
		final LayoutInflater inflater = activity.getLayoutInflater();
		final View layout = inflater.inflate(R.layout.info_toast_layout, (ViewGroup) activity.findViewById(R.id.toast_layout_root));
		final ImageView image = (ImageView) layout.findViewById(R.id.image);

		if (typ == TYPE_ERROR) {
			image.setImageResource(android.R.drawable.ic_dialog_alert);
		} else {
			image.setImageResource(android.R.drawable.ic_dialog_info);
		}
		final TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(msg);

		final Toast toast = new Toast(activity.getApplicationContext());
		toast.setGravity(Gravity.BOTTOM, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}

	public static void alertError(final Activity activity, final String msg) {
		alert(TYPE_ERROR, activity, msg);
	}

	public static void alertInfo(final Activity activity, final String msg) {
		alert(TYPE_INFO, activity, msg);
	}

	public static void alertInfo(final Activity activity, final String msg, final String buttontext, final String url) {
		alert(TYPE_INFO, activity, msg, buttontext, url);
	}

	private static void alert(final int typ, final Activity activity, final String msg) {
		alert(typ, activity, msg, null, null);
	}

	private static void alert(final int typ, final Activity activity, final String msg, final String buttontext, final String url) {
		final AlertDialog.Builder bld = new AlertDialog.Builder(activity);
		bld.setMessage(msg);
		if (typ == TYPE_ERROR) {
			bld.setIcon(android.R.drawable.ic_dialog_alert);
			bld.setTitle("Error");
		} else {
			bld.setIcon(android.R.drawable.ic_dialog_info);
			bld.setTitle("Info");
		}

		if (url != null && !url.isEmpty()) {
			bld.setNeutralButton(buttontext, new OnClickListener() {

				@Override
				public void onClick(final DialogInterface dialog, final int which) {
					final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					activity.startActivity(intent);
				}
			});
		} else {
			bld.setNeutralButton("OK", null);
		}
		bld.create().show();
	}
}
