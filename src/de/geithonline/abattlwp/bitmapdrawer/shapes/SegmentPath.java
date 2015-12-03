package de.geithonline.abattlwp.bitmapdrawer.shapes;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import de.geithonline.abattlwp.utils.PathHelper;

public class SegmentPath extends Path {
	public SegmentPath(final PointF center, final float outerRadius, final float innerRadius, final float rotateWinkel, final float sweepAngel) {

		final Path p = new Path();
		p.moveTo(center.x + innerRadius, center.y);
		p.lineTo(center.x + outerRadius, center.y);
		final RectF oval = new RectF();
		oval.left = center.x - outerRadius;
		oval.right = center.x + outerRadius;
		oval.top = center.y - outerRadius;
		oval.bottom = center.y + outerRadius;
		p.arcTo(oval, 0, sweepAngel);
		oval.left = center.x - innerRadius;
		oval.right = center.x + innerRadius;
		oval.top = center.y - innerRadius;
		oval.bottom = center.y + innerRadius;
		p.arcTo(oval, sweepAngel, -sweepAngel);
		p.close();
		PathHelper.rotatePath(center.x, center.y, p, rotateWinkel - sweepAngel / 2);
		addPath(p);
	}
}
