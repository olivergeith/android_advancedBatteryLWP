package de.geithonline.abattlwp.utils;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;

public class PathHelper {
	/**
	 * @param x
	 *            Position um die gefreht wird
	 * @param y
	 *            Position um die gefreht wird
	 * @param path
	 *            der pfad der gedreht werden soll
	 * @param rotate
	 *            gradzahl 0-360
	 */
	public static void rotatePath(final float x, final float y, final Path path, final float rotate) {
		final Matrix mMatrix = new Matrix();
		final RectF bounds = new RectF();
		path.computeBounds(bounds, true);
		mMatrix.postRotate(rotate, x, y);
		path.transform(mMatrix);
	}

	/**
	 * Spiegel an der vertikalen y-Achse
	 * 
	 * @param x
	 *            Position um die gefreht wird
	 * @param y
	 *            Position um die gefreht wird
	 * @param path
	 *            der pfad der gemirrored werden soll
	 */
	public static void mirrorPathLeftRight(final float x, final float y, final Path path) {
		final Matrix mMatrix = new Matrix();
		final RectF bounds = new RectF();
		path.computeBounds(bounds, true);
		mMatrix.postScale(-1f, 1f, x, y);
		path.transform(mMatrix);
	}

	/**
	 * Spiegel an der horizontalen x-Achse
	 * 
	 * @param x
	 *            Position um die gefreht wird
	 * @param y
	 *            Position um die gefreht wird
	 * @param path
	 *            der pfad der gemirrored werden soll
	 */
	public static void mirrorPathUpDown(final float x, final float y, final Path path) {
		final Matrix mMatrix = new Matrix();
		final RectF bounds = new RectF();
		path.computeBounds(bounds, true);
		mMatrix.postScale(1f, -1f, x, y);
		path.transform(mMatrix);
	}

}
