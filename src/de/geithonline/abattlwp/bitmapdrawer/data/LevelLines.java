package de.geithonline.abattlwp.bitmapdrawer.data;

import java.util.ArrayList;
import java.util.List;

public class LevelLines {

	public enum LevelLinesStyle {
		ZehnerlFuenferEiner, ZehnerEiner, ZehnerFuenfer
	}

	public float[] zehner;
	public float[] fuenfer;
	public float[] einer;

	public static LevelLines getLevelLines(final int start, final int stop, final LevelLinesStyle style) {
		switch (style) {
			default:
			case ZehnerlFuenferEiner:
				return getLevelLinesZFE(start, stop);
			case ZehnerFuenfer:
				return getLevelLinesZF(start, stop);
			case ZehnerEiner:
				return getLevelLinesZE(start, stop);
		}
	}

	private static LevelLines getLevelLinesZFE(final int start, final int stop) {
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

		final LevelLines lines = new LevelLines();
		lines.zehner = toArry(list10);
		lines.fuenfer = toArry(list5);
		lines.einer = toArry(list1);
		return lines;
	}

	private static LevelLines getLevelLinesZF(final int start, final int stop) {
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

		final LevelLines lines = new LevelLines();
		lines.zehner = toArry(list10);
		lines.fuenfer = toArry(list5);
		lines.einer = null;
		return lines;
	}

	private static LevelLines getLevelLinesZE(final int start, final int stop) {
		final List<Integer> list10 = new ArrayList<>();
		final List<Integer> list1 = new ArrayList<>();
		for (int i = start; i <= stop; i++) {
			if (i % 10 == 0) {
				list10.add(i);
			} else {
				list1.add(i);
			}
		}

		final LevelLines lines = new LevelLines();
		lines.zehner = toArry(list10);
		lines.fuenfer = null;
		lines.einer = toArry(list1);
		return lines;
	}

	private static void debug(final float[] arr) {
		if (arr == null) {
			return;
		}
		System.out.println("----------------------------------------");
		for (final float f : arr) {
			System.out.print(f + ", ");
		}
	}

	private static float[] toArry(final List<Integer> list) {
		final int size = list.size();
		final float[] lines = new float[size];
		for (int i = 0; i < size; i++) {
			lines[i] = list.get(i).intValue();
		}
		return lines;
	}

}
