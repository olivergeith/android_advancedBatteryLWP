package de.geithonline.abattlwp.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import de.geithonline.abattlwp.bitmapdrawer.AsymetricV1;
import de.geithonline.abattlwp.bitmapdrawer.AsymetricV2;
import de.geithonline.abattlwp.bitmapdrawer.AsymetricV3;
import de.geithonline.abattlwp.bitmapdrawer.ClockV1;
import de.geithonline.abattlwp.bitmapdrawer.ClockV2;
import de.geithonline.abattlwp.bitmapdrawer.ClockV3;
import de.geithonline.abattlwp.bitmapdrawer.ClockV4;
import de.geithonline.abattlwp.bitmapdrawer.ClockV5;
import de.geithonline.abattlwp.bitmapdrawer.ClockV6;
import de.geithonline.abattlwp.bitmapdrawer.DarkV1;
import de.geithonline.abattlwp.bitmapdrawer.FancyV1;
import de.geithonline.abattlwp.bitmapdrawer.FancyV2;
import de.geithonline.abattlwp.bitmapdrawer.IBitmapDrawer;
import de.geithonline.abattlwp.bitmapdrawer.LabyrinthV1;
import de.geithonline.abattlwp.bitmapdrawer.RotatingV1;
import de.geithonline.abattlwp.bitmapdrawer.RotatingV2;
import de.geithonline.abattlwp.bitmapdrawer.RotatingV3;
import de.geithonline.abattlwp.bitmapdrawer.SimpleCircleV1;
import de.geithonline.abattlwp.bitmapdrawer.SimpleCircleV2;
import de.geithonline.abattlwp.bitmapdrawer.TachoV1;
import de.geithonline.abattlwp.bitmapdrawer.TachoV2;
import de.geithonline.abattlwp.bitmapdrawer.TachoV3;

public class DrawerManager {
	private static Map<String, IBitmapDrawer> drawer = new HashMap<String, IBitmapDrawer>();
	private static Map<String, Bitmap> iconCache = new HashMap<String, Bitmap>();

	static {
		drawer.put("AsymetricV1", new AsymetricV1());
		drawer.put("AsymetricV2", new AsymetricV2());
		drawer.put("AsymetricV3", new AsymetricV3());
		drawer.put("ClockV1", new ClockV1());
		drawer.put("ClockV2", new ClockV2());
		drawer.put("ClockV3", new ClockV3());
		drawer.put("ClockV4", new ClockV4());
		drawer.put("ClockV5", new ClockV5());
		drawer.put("ClockV6", new ClockV6());
		drawer.put("FancyV1", new FancyV1());
		drawer.put("FancyV2", new FancyV2());
		drawer.put("LabyrinthV1", new LabyrinthV1());
		drawer.put("TachoV1", new TachoV1());
		drawer.put("TachoV2", new TachoV2());
		drawer.put("TachoV3", new TachoV3());
		drawer.put("RotatingV1", new RotatingV1());
		drawer.put("RotatingV2", new RotatingV2());
		drawer.put("RotatingV3", new RotatingV3());
		drawer.put("SimpleCircleV1", new SimpleCircleV1());
		drawer.put("SimpleCircleV2", new SimpleCircleV2());
		drawer.put("DarkV1", new DarkV1());
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
