package de.geithonline.abattlwp.bitmapdrawer.shapes;

import android.graphics.Path;
import android.graphics.PointF;

public class CirclePath extends Path {

	public CirclePath(final PointF center, final float ra, final float ri) {
		super();
		drawRingV2(center, ra, ri);
	}

	public void drawRingV2(final PointF center, final float ra, final float ri) {
		addCircle(center.x, center.y, ra, Direction.CCW);

		if (ri > 0) {
			addCircle(center.x, center.y, ri, Direction.CW);
		}
	}
}
