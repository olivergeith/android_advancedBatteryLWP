package de.geithonline.abattlwp.bitmapdrawer.shapes;

import android.graphics.Path;
import android.graphics.PointF;

public class GearPath extends Path {

	public GearPath(final int arms, final PointF center, final float ra, final float ri) {
		super();
		final float umfang = (float) (2 * ra * Math.PI);
		final float angle = (float) (2 * Math.PI / (arms * 4));
		final float rInner = ra - umfang / arms / 2;

		int c = 0;
		for (int i = 0; i < arms; i++) {
			for (int j = 0; j < 4; j++) {
				float r;
				switch (j) {
					default:
					case 0:
						r = ra;
						break;
					case 1:
						r = ra;
						break;
					case 2:
						r = rInner;
						break;
					case 3:
						r = rInner;
						break;
				}

				final PointF p = new PointF();
				p.x = (float) (center.x + Math.cos(c * angle - angle / 2) * r);
				p.y = (float) (center.y + Math.sin(c * angle - angle / 2) * r);
				if (c == 0) {
					moveTo(p.x, p.y);
				} else {
					lineTo(p.x, p.y);
				}
				c++;
			}
		}
		if (ri > 0) {
			addCircle(center.x, center.y, ri, Direction.CCW);
		}
		close();
	}
}
