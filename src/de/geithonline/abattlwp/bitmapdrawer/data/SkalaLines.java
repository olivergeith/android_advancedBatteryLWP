package de.geithonline.abattlwp.bitmapdrawer.data;

import java.util.ArrayList;
import java.util.List;

public class SkalaLines {
	public float[] ebene1 = null;
	public float[] ebene2 = null;
	public float[] ebene3 = null;

	public enum LevelLinesStyle {
		ZehnerFuenferEiner, ZehnerEiner, ZehnerFuenfer
	}

	public enum VoltLinesStyle {
		/**
		 * linen alle 500mv, 100mv, 50mv
		 */
		style_500_100_50,
		/**
		 * linien alle 500mv, 250mv, 50mv
		 */
		style_500_250_50
	}

	public SkalaLines() {
		super();
		ebene1 = null;
		ebene2 = null;
		ebene3 = null;
	}

	public SkalaLines(final float[] zehner, final float[] fuenfer) {
		super();
		ebene1 = zehner;
		ebene2 = fuenfer;
		ebene3 = null;
	}

	public SkalaLines(final float[] zehner, final float[] fuenfer, final float[] einer) {
		super();
		ebene1 = zehner;
		ebene2 = fuenfer;
		ebene3 = einer;
	}

	public static SkalaLines getLevelLines(final int start, final int stop, final LevelLinesStyle style) {
		switch (style) {
			default:
			case ZehnerFuenferEiner:
				return getLevelLinesZFE(start, stop);
			case ZehnerFuenfer:
				return getLevelLinesZF(start, stop);
			case ZehnerEiner:
				return getLevelLinesZE(start, stop);
		}
	}

	private static SkalaLines getLevelLinesZFE(final int start, final int stop) {
		final List<Integer> list10 = new ArrayList<>();
		final List<Integer> list5 = new ArrayList<>();
		final List<Integer> list1 = new ArrayList<>();
		for (int i = start; i <= stop; i++) {
			if (i % 10 == 0) {
				list10.add(i);
			} else if (i % 5 == 0) {
				list5.add(i);
			} else {
				list1.add(i);
			}
		}

		final SkalaLines lines = new SkalaLines();
		lines.ebene1 = integerToArry(list10);
		lines.ebene2 = integerToArry(list5);
		lines.ebene3 = integerToArry(list1);
		return lines;
	}

	private static SkalaLines getLevelLinesZF(final int start, final int stop) {
		final List<Integer> list10 = new ArrayList<>();
		final List<Integer> list5 = new ArrayList<>();
		final List<Integer> list1 = new ArrayList<>();
		for (int i = start; i <= stop; i++) {
			if (i % 10 == 0) {
				list10.add(i);
			} else if (i % 5 == 0) {
				list5.add(i);
			} else {
				list1.add(i);
			}
		}

		final SkalaLines lines = new SkalaLines();
		lines.ebene1 = integerToArry(list10);
		lines.ebene2 = integerToArry(list5);
		lines.ebene3 = null;
		return lines;
	}

	private static SkalaLines getLevelLinesZE(final int start, final int stop) {
		final List<Integer> list10 = new ArrayList<>();
		final List<Integer> list1 = new ArrayList<>();
		for (int i = start; i <= stop; i++) {
			if (i % 10 == 0) {
				list10.add(i);
			} else {
				list1.add(i);
			}
		}

		final SkalaLines lines = new SkalaLines();
		lines.ebene1 = integerToArry(list10);
		lines.ebene2 = null;
		lines.ebene3 = integerToArry(list1);
		return lines;
	}

	public static SkalaLines getVoltLines(final float startVoltage, final VoltLinesStyle style) {
		switch (style) {
			default:
			case style_500_250_50:
				return getVoltLines_500_250_50(startVoltage);
			case style_500_100_50:
				return getVoltLines_500_100_50(startVoltage);
		}
	}

	private static SkalaLines getVoltLines_500_250_50(final float startVoltage) {
		final float voltageSweep = 1f;
		final float step = voltageSweep / 100; // = 0.01V = 10mV;
		final List<Float> list10 = new ArrayList<>();
		final List<Float> list5 = new ArrayList<>();
		final List<Float> list1 = new ArrayList<>();
		for (int i = 0; i <= 100; i++) {
			if (i % 50 == 0) { // alle 500mV
				list10.add(startVoltage + i * step);
			} else if (i % 25 == 0) { // alle 250mV
				list5.add(startVoltage + i * step);
			} else if (i % 5 == 0) { // alle 50mV
				list1.add(startVoltage + i * step);
			} else { // alle 10mV
				// list1.add(startVoltage + i * step);
			}
		}
		final SkalaLines lines = new SkalaLines();
		lines.ebene1 = floatToArry(list10);
		lines.ebene2 = floatToArry(list5);
		lines.ebene3 = floatToArry(list1);
		return lines;
	}

	public static SkalaLines getVoltLines_500_100_50(final float startVoltage) {
		final float voltageSweep = 1f;
		final float step = voltageSweep / 100; // = 0.01V = 10mV;
		final List<Float> list10 = new ArrayList<>();
		final List<Float> list5 = new ArrayList<>();
		final List<Float> list1 = new ArrayList<>();
		for (int i = 0; i <= 100; i++) {
			if (i % 50 == 0) { // alle 500mV
				list10.add(startVoltage + i * step);
			} else if (i % 10 == 0) { // alle 100mV
				list5.add(startVoltage + i * step);
			} else if (i % 5 == 0) { // alle 50mV
				list1.add(startVoltage + i * step);
			} else { // alle 10mV
				// list1.add(startVoltage + i * step);
			}
		}
		final SkalaLines lines = new SkalaLines();
		lines.ebene1 = floatToArry(list10);
		lines.ebene2 = floatToArry(list5);
		lines.ebene3 = floatToArry(list1);
		return lines;
	}

	// private static void debug(final float[] arr) {
	// if (arr == null) {
	// return;
	// }
	// System.out.println("----------------------------------------");
	// for (final float f : arr) {
	// System.out.print(f + ", ");
	// }
	// }

	private static float[] integerToArry(final List<Integer> list) {
		final int size = list.size();
		final float[] lines = new float[size];
		for (int i = 0; i < size; i++) {
			lines[i] = list.get(i).intValue();
		}
		return lines;
	}

	private static float[] floatToArry(final List<Float> list) {
		final int size = list.size();
		final float[] lines = new float[size];
		for (int i = 0; i < size; i++) {
			lines[i] = list.get(i).floatValue();
		}
		return lines;
	}

}
