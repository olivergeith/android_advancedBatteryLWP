package de.geithonline.abattlwp.bitmapdrawer.shapes;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import de.geithonline.abattlwp.utils.PathHelper;

public class LevelArcPath extends Path {
	public LevelArcPath(final PointF center, final float outerRadius, final float innerRadius, final float startingAngle, float sweep) {
		if (sweep == 360) {
			sweep = 359.999f;
		}

		final Path p = new Path();
		p.moveTo(center.x + innerRadius, center.y);
		p.lineTo(center.x + outerRadius, center.y);
		final RectF oval = new RectF();
		oval.left = center.x - outerRadius;
		oval.right = center.x + outerRadius;
		oval.top = center.y - outerRadius;
		oval.bottom = center.y + outerRadius;
		p.arcTo(oval, 0, sweep);
		oval.left = center.x - innerRadius;
		oval.right = center.x + innerRadius;
		oval.top = center.y - innerRadius;
		oval.bottom = center.y + innerRadius;
		p.arcTo(oval, sweep, -sweep);
		p.close();
		PathHelper.rotatePath(center.x, center.y, p, startingAngle);
		addPath(p);
	}
}
