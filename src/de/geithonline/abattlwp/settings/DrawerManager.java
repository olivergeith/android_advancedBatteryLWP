package de.geithonline.abattlwp.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import de.geithonline.abattlwp.bitmapdrawer.AppIconDrawer;
import de.geithonline.abattlwp.bitmapdrawer.AsymetricV1;
import de.geithonline.abattlwp.bitmapdrawer.AsymetricV2;
import de.geithonline.abattlwp.bitmapdrawer.AsymetricV3;
import de.geithonline.abattlwp.bitmapdrawer.BinaryBarsV1;
import de.geithonline.abattlwp.bitmapdrawer.BrickMadnessV1;
import de.geithonline.abattlwp.bitmapdrawer.BrickV2;
import de.geithonline.abattlwp.bitmapdrawer.ClockV1;
import de.geithonline.abattlwp.bitmapdrawer.ClockV2;
import de.geithonline.abattlwp.bitmapdrawer.ClockV3;
import de.geithonline.abattlwp.bitmapdrawer.ClockV4;
import de.geithonline.abattlwp.bitmapdrawer.ClockV5;
import de.geithonline.abattlwp.bitmapdrawer.ClockV6;
import de.geithonline.abattlwp.bitmapdrawer.ClockV7;
import de.geithonline.abattlwp.bitmapdrawer.DarkV1;
import de.geithonline.abattlwp.bitmapdrawer.DarkV2;
import de.geithonline.abattlwp.bitmapdrawer.DarkV3;
import de.geithonline.abattlwp.bitmapdrawer.DarkV4;
import de.geithonline.abattlwp.bitmapdrawer.FancyV1;
import de.geithonline.abattlwp.bitmapdrawer.FancyV2;
import de.geithonline.abattlwp.bitmapdrawer.IBitmapDrawer;
import de.geithonline.abattlwp.bitmapdrawer.LabyrinthV1;
import de.geithonline.abattlwp.bitmapdrawer.LabyrinthV2;
import de.geithonline.abattlwp.bitmapdrawer.LabyrinthV3;
import de.geithonline.abattlwp.bitmapdrawer.LevelBar;
import de.geithonline.abattlwp.bitmapdrawer.OutlineV1;
import de.geithonline.abattlwp.bitmapdrawer.OutlineV2;
import de.geithonline.abattlwp.bitmapdrawer.RotatingV1;
import de.geithonline.abattlwp.bitmapdrawer.RotatingV2;
import de.geithonline.abattlwp.bitmapdrawer.RotatingV3;
import de.geithonline.abattlwp.bitmapdrawer.SimpleCircleV1;
import de.geithonline.abattlwp.bitmapdrawer.SimpleCircleV2;
import de.geithonline.abattlwp.bitmapdrawer.SimpleCircleV3;
import de.geithonline.abattlwp.bitmapdrawer.SimpleCircleV4;
import de.geithonline.abattlwp.bitmapdrawer.SimpleCircleV5;
import de.geithonline.abattlwp.bitmapdrawer.SuperSimpleDrawer;
import de.geithonline.abattlwp.bitmapdrawer.TachoV1;
import de.geithonline.abattlwp.bitmapdrawer.TachoV2;
import de.geithonline.abattlwp.bitmapdrawer.ZoopaV1;

public class DrawerManager {
	public static final String DEFAULT_DRAWER_NAME_CLOCK_V3 = "Clock V3";
	public static final String APP_ICON_DRAWER_NAME = "App Icon";
	private static Map<String, IBitmapDrawer> drawer = new HashMap<>();
	private static Map<String, Bitmap> iconCache = new HashMap<>();

	static {
		drawer.put(APP_ICON_DRAWER_NAME, new AppIconDrawer());
		drawer.put("Asymetric V1", new AsymetricV1());
		drawer.put("Asymetric V2", new AsymetricV2());
		drawer.put("Asymetric V3", new AsymetricV3());
		drawer.put("Clock V1", new ClockV1());
		drawer.put("Clock V2", new ClockV2());
		drawer.put(DEFAULT_DRAWER_NAME_CLOCK_V3, new ClockV3());
		drawer.put("Clock V4", new ClockV4());
		drawer.put("Clock V5", new ClockV5());
		drawer.put("Clock V6", new ClockV6());
		drawer.put("Clock V7", new ClockV7());
		drawer.put("Dark V1", new DarkV1());
		drawer.put("Dark V2", new DarkV2());
		drawer.put("Dark V3", new DarkV3());
		drawer.put("Dark V4", new DarkV4());
		drawer.put("Fancy V1", new FancyV1());
		drawer.put("Fancy V2", new FancyV2());
		drawer.put("Labyrinth V1", new LabyrinthV1());
		drawer.put("Labyrinth V2", new LabyrinthV2());
		drawer.put("Labyrinth V3", new LabyrinthV3());
		drawer.put("Tacho V1", new TachoV1());
		drawer.put("Tacho V2", new TachoV2());
		drawer.put("Rotating V1", new RotatingV1());
		drawer.put("Rotating V2", new RotatingV2());
		drawer.put("Rotating V3", new RotatingV3());
		drawer.put("SimpleCircle V1", new SimpleCircleV1());
		drawer.put("SimpleCircle V2", new SimpleCircleV2());
		drawer.put("SimpleCircle V3", new SimpleCircleV3());
		drawer.put("SimpleCircle V4", new SimpleCircleV4());
		drawer.put("SimpleCircle V5", new SimpleCircleV5());
		drawer.put("Outline V1", new OutlineV1());
		drawer.put("Outline V2", new OutlineV2());
		drawer.put("Bricks V1", new BrickMadnessV1());
		drawer.put("Bricks V2", new BrickV2());
		drawer.put("Binary Bars V1", new BinaryBarsV1());
		drawer.put("SuperSimple V1", new SuperSimpleDrawer());
		drawer.put("LevelBar V1", new LevelBar());
		drawer.put("Zoopa V1", new ZoopaV1());
		// drawer.put("BrickClock V1", new BrickClockV1()); // is h��lich, aber als Pattern f�r einen andere Klasse vielleicht irgendwann mal interessant!
	}

	public static IBitmapDrawer getDrawer(final String name) {
		IBitmapDrawer d = drawer.get(name);
		if (d == null) {
			d = drawer.get(DEFAULT_DRAWER_NAME_CLOCK_V3);
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
		final List<String> keySet = new ArrayList<>(drawer.keySet());
		Collections.sort(keySet);
		return keySet;
	}

	public static int getPostionOfSelectedStyleInList() {
		final List<String> styleNames = getStyleNames();
		return styleNames.indexOf(Settings.getStyle());
	}

	public static String[] getDrawerNames() {
		final List<String> keySet = new ArrayList<>(drawer.keySet());
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
