package de.geithonline.abattlwp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;

public class Alerter {
	private static final int TYPE_ERROR = 0;
	private static final int TYPE_INFO = 1;

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

	public static void alertYesNo(final Activity activity, final String msg, final String title, final DialogInterface.OnClickListener yesListener) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setIcon(android.R.drawable.ic_menu_help);
		builder.setPositiveButton("YES", yesListener);
		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				dialog.dismiss();
			}
		});

		final AlertDialog alert = builder.create();
		alert.show();
	}

	public static void alertOneButton(final Activity activity, final String msg, final String title, final String buttonText,
			final DialogInterface.OnClickListener clickListener) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setIcon(android.R.drawable.ic_menu_help);
		builder.setNegativeButton(buttonText, clickListener);
		final AlertDialog alert = builder.create();
		alert.show();
	}

	public static void alertYesNo(final Activity activity, final String msg, final String title, final DialogInterface.OnClickListener yesListener,
			final DialogInterface.OnClickListener noListener) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setIcon(android.R.drawable.ic_menu_help);
		builder.setPositiveButton("YES", yesListener);
		builder.setNegativeButton("NO", noListener);

		final AlertDialog alert = builder.create();
		alert.show();
	}

	public static void alertYesNoUrgent(final Activity activity, final String msg, final String title, final DialogInterface.OnClickListener yesListener) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setPositiveButton("YES", yesListener);
		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				dialog.dismiss();
			}
		});

		final AlertDialog alert = builder.create();
		alert.show();
	}

}
