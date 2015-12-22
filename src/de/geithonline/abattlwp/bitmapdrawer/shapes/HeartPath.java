package de.geithonline.abattlwp.bitmapdrawer.shapes;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

public class HeartPath extends Path {

	/**
	 * @param center
	 *            center Point
	 * @param rOuter
	 *            Radius von 1f bis 5f....
	 */
	public HeartPath(final PointF center, final float rOuter) {
		super();
		drawHeartLovely(center, rOuter);
	}

	private void drawHeartLovely(final PointF center, final float radius) {
		final float raster = radius / 2;
		final RectF oval = new RectF();
		oval.left = center.x - 2 * raster;
		oval.right = center.x - 0 * raster;
		oval.top = center.y - 2 * raster;
		oval.bottom = center.y + 0 * raster;
		arcTo(oval, -180, 180);

		oval.left = center.x - 0 * raster;
		oval.right = center.x + 2 * raster;
		oval.top = center.y - 2 * raster;
		oval.bottom = center.y + 0 * raster;
		arcTo(oval, -180, 180);

		cubicTo(center.x + 2 * raster, center.y + 0 * raster, // CP1
				center.x + 1 * raster, center.y + 1.0f * raster, // CP2
				center.x + 0 * raster, center.y + 1.5f * raster);
		cubicTo(center.x - 1 * raster, center.y + 1.0f * raster, // CP1
				center.x - 2 * raster, center.y + 0 * raster, // CP2
				center.x - 2 * raster, center.y - 1 * raster);
		close();
	}

}
