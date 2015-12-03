package de.geithonline.abattlwp.bitmapdrawer.shapes;

import android.graphics.Path;
import android.graphics.Point;

public class StarPathInvert extends Path {

	public StarPathInvert(final int arms, final Point center, final float rOuter, final float rInner) {
		super();
		final float angle = (float) (Math.PI / arms);

		for (int i = 0; i <= 2 * arms; i++) {
			final float r = (i & 1) == 0 ? rInner : rOuter;
			// final float r = rInner;

			final Point p = new Point();
			p.x = (int) (center.x + Math.cos(i * angle) * r);
			p.y = (int) (center.y + Math.sin(i * angle) * r);
			// Log.i("Point", "Point " + i + " p=" + p.x + " " + p.y);
			if (i == 0) {
				moveTo(p.x, p.y);
				// addCircle(p.x, p.y, 10, Direction.CCW);
			} else {
				lineTo(p.x, p.y);
				// addCircle(p.x, p.y, 5, Direction.CCW);
			}
		}
		close();
	}
}
