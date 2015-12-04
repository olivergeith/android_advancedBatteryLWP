package de.geithonline.abattlwp.utils;

import android.graphics.Color;

public class ColorHelper {

	public static int brighter(final int color) {
		final int a = Color.alpha(color);
		int r = Color.red(color);
		int g = Color.green(color);
		int b = Color.blue(color);

		r += 32;
		if (r > 255) {
			r = 255;
		}
		g += 32;
		if (g > 255) {
			g = 255;
		}
		b += 32;
		if (b > 255) {
			b = 255;
		}

		return Color.argb(a, r, g, b);
	}

	public static int darker(final int color) {
		final int a = Color.alpha(color);
		int r = Color.red(color);
		int g = Color.green(color);
		int b = Color.blue(color);

		r -= 32;
		if (r < 0) {
			r = 0;
		}
		g -= 32;
		if (g < 0) {
			g = 0;
		}
		b -= 32;
		if (b < 0) {
			b = 0;
		}
		return Color.argb(a, r, g, b);
	}

	public static int darker2times(final int color) {
		final int a = Color.alpha(color);
		int r = Color.red(color);
		int g = Color.green(color);
		int b = Color.blue(color);

		r -= 64;
		if (r < 0) {
			r = 0;
		}
		g -= 64;
		if (g > 0) {
			g = 0;
		}
		b -= 64;
		if (b < 0) {
			b = 0;
		}
		return Color.argb(a, r, g, b);
	}

	public static int getRadiantColor(final int col1, final int col2, final int level, final int min, final int max) {
		final int diff = min - max;
		final int diffpercent = min - level;
		final float factor = Math.abs((float) diffpercent / (float) diff);

		final int diffa = Color.alpha(col1) - Color.alpha(col2);
		final int diffr = Color.red(col1) - Color.red(col2);
		final int diffg = Color.green(col1) - Color.green(col2);
		final int diffb = Color.blue(col1) - Color.blue(col2);

		final int a = Math.round(Color.alpha(col1) - diffa * factor);
		final int r = Math.round(Color.red(col1) - diffr * factor);
		final int g = Math.round(Color.green(col1) - diffg * factor);
		final int b = Math.round(Color.blue(col1) - diffb * factor);

		return Color.argb(a, r, g, b);
	}

	/**
	 * @param color
	 *            the color to change
	 * @param amount
	 *            the brigtnes offset (-255 to 255)
	 * @return
	 */
	public static int changeBrightness(final int color, final int amount) {
		final int a = Color.alpha(color);
		int r = Color.red(color);
		int g = Color.green(color);
		int b = Color.blue(color);

		r += amount;
		if (r > 255) {
			r = 255;
		}
		if (r < 0) {
			r = 0;
		}
		g += amount;
		if (g > 255) {
			g = 255;
		}
		if (g < 0) {
			g = 0;
		}
		b += amount;
		if (b > 255) {
			b = 255;
		}
		if (b < 0) {
			b = 0;
		}
		return Color.argb(a, r, g, b);
	}

}
