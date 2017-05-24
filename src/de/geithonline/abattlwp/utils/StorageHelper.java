package de.geithonline.abattlwp.utils;

import java.io.File;

import android.os.Environment;

public class StorageHelper {
	public final static String DIR_SDCARD = Environment.getExternalStorageDirectory().toString();
	public final static File PATH_SDCARD = Environment.getExternalStorageDirectory();

	private final static String muimerpDir = DIR_SDCARD + File.separator + "data" + File.separator + "muimerp.txt";
	private final static File muimerpFile = new File(muimerpDir);

	private final static String superUserDir = DIR_SDCARD + File.separator + "data" + File.separator + "superuser.txt";
	private final static File superUserFile = new File(superUserDir);

	public static boolean muimerpExists() {
		return muimerpFile.exists();
	}

	public static boolean superUserExists() {
		return superUserFile.exists();
	}

}
