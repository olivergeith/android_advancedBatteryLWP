package de.geithonline.abattlwp.bitmapdrawer.shapes;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

public class HalfArcPath extends Path {
	public HalfArcPath(final PointF center, final float outerRadius, final float innerRadius, final float undercutPixel) {

		moveTo(center.x - innerRadius, center.y + undercutPixel);
		lineTo(center.x - outerRadius, center.y + undercutPixel);
		lineTo(center.x - outerRadius, center.y);
		final RectF oval = new RectF();
		oval.left = center.x - outerRadius;
		oval.right = center.x + outerRadius;
		oval.top = center.y - outerRadius;
		oval.bottom = center.y + outerRadius;
		arcTo(oval, -180, 180);

		lineTo(center.x + outerRadius, center.y + undercutPixel);
		// wenn es kein Bogen sondern nur ein Halbkreis ist!
		if (innerRadius > 0f) {
			lineTo(center.x + innerRadius, center.y + undercutPixel);
			lineTo(center.x + innerRadius, center.y);
			oval.left = center.x - innerRadius;
			oval.right = center.x + innerRadius;
			oval.top = center.y - innerRadius;
			oval.bottom = center.y + innerRadius;
			arcTo(oval, 0, -180);
		}
		close();
	}
}
