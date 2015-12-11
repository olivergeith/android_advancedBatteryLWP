package de.geithonline.abattlwp.bitmapdrawer.shapes;

import android.graphics.Path;
import android.graphics.PointF;

public class StarPath extends Path {

	public StarPath(final int arms, final PointF center, final float ra, final float ri) {
		super();
		final float angle = (float) (Math.PI / arms);

		for (int i = 0; i <= 2 * arms; i++) {
			final float r = (i & 1) == 0 ? ra : ri;
			// final float r = rInner;

			final PointF p = new PointF();
			p.x = (float) (center.x + Math.cos(i * angle) * r);
			p.y = (float) (center.y + Math.sin(i * angle) * r);
			if (i == 0) {
				moveTo(p.x, p.y);
			} else {
				lineTo(p.x, p.y);
			}
		}
		close();
	}
}
