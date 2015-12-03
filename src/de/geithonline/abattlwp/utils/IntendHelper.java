package de.geithonline.abattlwp.utils;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class IntendHelper {

	public static boolean isAvailable(final Context ctx, final Intent intent) {
		final PackageManager mgr = ctx.getPackageManager();
		final List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}
}
