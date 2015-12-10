package de.geithonline.abattlwp.bitmapdrawer.parts;

import java.util.ArrayList;
import java.util.List;

import android.graphics.PointF;

public class Skala {

	public static SkalaPart getLevelScalaCircular(//
			final PointF center, //
			final float radiusLineStart, final float radiusLineEnd, //
			final float startWinkel) {

		final float[] linesEbene1 = new float[] { 0, 10, 20, 30, 40, 50, 60, 70, 80, 90 };
		final float[] linesEbene2 = new float[] { 5, 15, 25, 35, 45, 55, 65, 75, 85, 95 };
		final float[] linesEbene3 = getLevelLines1er();
		return new SkalaPart(center, radiusLineStart, radiusLineEnd, startWinkel, 360, 0, 100, linesEbene1)//
				.setLinesEbene2(linesEbene2)//
				.setLinesEbene3(linesEbene3);
	}

	public static SkalaPart getLevelScalaArch(//
			final PointF center, //
			final float radiusLineStart, final float radiusLineEnd, //
			final float startWinkel, final float sweep) {

		final float[] linesEbene1 = new float[] { 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 };
		final float[] linesEbene2 = new float[] { 5, 15, 25, 35, 45, 55, 65, 75, 85, 95 };
		final float[] linesEbene3 = getLevelLines1er();
		return new SkalaPart(center, radiusLineStart, radiusLineEnd, startWinkel, sweep, 0, 100, linesEbene1)//
				.setLinesEbene2(linesEbene2)//
				.setLinesEbene3(linesEbene3);
	}

	private static float[] getLevelLines1er() {
		final List<Integer> list = new ArrayList<>();
		for (int i = 1; i < 100; i++) {
			if (i % 10 == 0) {
				// do noting
			} else if (i % 5 == 0) {
				// do nothing
			} else {
				list.add(i);
			}
		}

		// umsortieren in die Liste
		final int size = list.size();
		final float[] lines = new float[size];
		for (int i = 0; i < size; i++) {
			lines[i] = list.get(i).intValue();
		}
		return lines;
	}

}
