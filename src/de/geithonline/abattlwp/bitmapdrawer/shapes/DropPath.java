package de.geithonline.abattlwp.bitmapdrawer.shapes;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

public class DropPath extends Path {

	public DropPath(final PointF center, final float radius) {
		drawDrop(center, radius);
	}

	private void drawDrop(final PointF center, final float radius) {
		final float raster = radius;
		final RectF oval = new RectF();
		oval.left = center.x - 1 * raster;
		oval.right = center.x + 1 * raster;
		oval.top = center.y - 0 * raster;
		oval.bottom = center.y + 2 * raster;

		moveTo(center.x + 0 * raster, center.y - 2 * raster);
		cubicTo(center.x + 0 * raster, center.y - 0 * raster, // CP1
				center.x + 1 * raster, center.y - 0 * raster, // CP2
				center.x + 1 * raster, center.y + 1 * raster);
		arcTo(oval, 0, 180);
		cubicTo(center.x - 1 * raster, center.y - 0 * raster, // CP1
				center.x + 0 * raster, center.y - 0 * raster, // CP2
				center.x + 0 * raster, center.y - 2 * raster);
		close();

	}

}
