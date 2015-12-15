package de.geithonline.abattlwp.bitmapdrawer.shapes;

import android.graphics.Path;
import android.graphics.PointF;

public class CirclePath extends Path {

	public CirclePath(final PointF center, final float ra, final float ri) {
		super();
		drawRingV2(center, ra, ri, Direction.CW);
	}

	public CirclePath(final PointF center, final float ra, final float ri, final Direction dir) {
		super();
		drawRingV2(center, ra, ri, dir);
	}

	public void drawRingV2(final PointF center, final float ra, final float ri, final Direction dir) {
		addCircle(center.x, center.y, ra, dir);

		if (ri > 0) {
			if (dir.equals(Direction.CW)) {
				addCircle(center.x, center.y, ri, Direction.CCW);
			} else {
				addCircle(center.x, center.y, ri, Direction.CW);
			}
		}
	}
}
