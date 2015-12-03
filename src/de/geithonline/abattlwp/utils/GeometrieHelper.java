package de.geithonline.abattlwp.utils;

import android.graphics.PointF;
import android.graphics.RectF;

public class GeometrieHelper {
	public static float calcDistance(final PointF p1, final PointF p2) {
		final float dx = p2.x - p1.x;
		final float dy = p2.y - p1.y;

		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	public static RectF getCircle(final PointF center, final float radius) {
		return getOval(center, radius, radius);
	}

	public static RectF getOval(final PointF center, final float radiusX, final float radiusY) {
		final RectF oval = new RectF();
		oval.left = center.x - radiusX;
		oval.top = center.y - radiusY;
		oval.right = center.x + radiusX;
		oval.bottom = center.y + radiusY;
		return oval;
	}

}
