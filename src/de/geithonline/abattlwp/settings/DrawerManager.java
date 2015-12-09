package de.geithonline.abattlwp.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import de.geithonline.abattlwp.bitmapdrawer.BitmapDrawerAsymetricV1;
import de.geithonline.abattlwp.bitmapdrawer.BitmapDrawerClockV1;
import de.geithonline.abattlwp.bitmapdrawer.BitmapDrawerClockV2;
import de.geithonline.abattlwp.bitmapdrawer.BitmapDrawerClockV3;
import de.geithonline.abattlwp.bitmapdrawer.BitmapDrawerClockV4;
import de.geithonline.abattlwp.bitmapdrawer.BitmapDrawerClockV5;
import de.geithonline.abattlwp.bitmapdrawer.BitmapDrawerClockV6;
import de.geithonline.abattlwp.bitmapdrawer.BitmapDrawerFancyV1;
import de.geithonline.abattlwp.bitmapdrawer.BitmapDrawerFancyV2;
import de.geithonline.abattlwp.bitmapdrawer.BitmapDrawerNewSimpleCircleV1;
import de.geithonline.abattlwp.bitmapdrawer.BitmapDrawerNewTachoV1;
import de.geithonline.abattlwp.bitmapdrawer.BitmapDrawerNewTachoV3;
import de.geithonline.abattlwp.bitmapdrawer.BitmapDrawerRotatingV1;
import de.geithonline.abattlwp.bitmapdrawer.BitmapDrawerRotatingV2;
import de.geithonline.abattlwp.bitmapdrawer.IBitmapDrawer;

public class DrawerManager {
	private static Map<String, IBitmapDrawer> drawer = new HashMap<String, IBitmapDrawer>();
	private static Map<String, Bitmap> iconCache = new HashMap<String, Bitmap>();

	static {
		drawer.put("TachoV1", new BitmapDrawerNewTachoV1());
		drawer.put("TachoV3", new BitmapDrawerNewTachoV3());
		drawer.put("ClockV1", new BitmapDrawerClockV1());
		drawer.put("ClockV2", new BitmapDrawerClockV2());
		drawer.put("ClockV3", new BitmapDrawerClockV3());
		drawer.put("ClockV4", new BitmapDrawerClockV4());
		drawer.put("ClockV5", new BitmapDrawerClockV5());
		drawer.put("ClockV6", new BitmapDrawerClockV6());
		drawer.put("NewSimpleCircleV1", new BitmapDrawerNewSimpleCircleV1());
		drawer.put("NewSimpleCircleV1 (smaller)", new BitmapDrawerNewSimpleCircleV1(0.88f, 0.82f, 0.97f, 0.73f));
		drawer.put("AsymetricV1", new BitmapDrawerAsymetricV1());
		drawer.put("RotatingV1", new BitmapDrawerRotatingV1());
		drawer.put("RotatingV2", new BitmapDrawerRotatingV2());
		drawer.put("FancyV1", new BitmapDrawerFancyV1());
		drawer.put("FancyV2", new BitmapDrawerFancyV2());
	}

	public static IBitmapDrawer getDrawer(final String name) {
		IBitmapDrawer d = drawer.get(name);
		if (d == null) {
			d = drawer.get("ClockV3");
		}
		return d;
	}

	public static Bitmap getIconForDrawer(final String name, final int size, final int level) {
		Bitmap b = iconCache.get(name);
		if (b == null) {
			final IBitmapDrawer drawer = getDrawer(name);
			b = drawer.drawIcon(level, size);
			iconCache.put(name, b);
		}
		return b;
	}

	public static void clearIconCache() {
		iconCache.clear();
	}

	public static Bitmap getIconForDrawerForceDrawNew(final String drawerName, final int size, final int level) {
		final IBitmapDrawer drawer = getDrawer(drawerName);
		final Bitmap b = drawer.drawIcon(level, size);
		return b;
	}

	public static List<String> getStyleNames() {
		final List<String> keySet = new ArrayList<String>(drawer.keySet());
		Collections.sort(keySet);
		return keySet;
	}

	public static int getPostionOfSelectedStyleInList() {
		final List<String> styleNames = getStyleNames();
		return styleNames.indexOf(Settings.getStyle());
	}

	public static String[] getDrawerNames() {
		final List<String> keySet = new ArrayList<String>(drawer.keySet());
		Collections.sort(keySet);
		final String[] array = new String[keySet.size()];
		int i = 0;
		for (final String string : keySet) {
			array[i] = string;
			i++;
		}
		return array;
	}
}
