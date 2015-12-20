package de.geithonline.abattlwp.bitmapdrawer.shapes;

import android.graphics.Path;
import android.graphics.PointF;

public class PillowPath extends Path {
	/**
	 * @param center
	 *            center Point
	 * @param radius
	 *            Radius von 1f bis 5f....
	 */
	public PillowPath(final int arms, final PointF center, final float radius) {
		super();
		drawPillow(arms, center, radius);
	}

	private void drawPillow(final int arms, final PointF center, final float radius) {
		final float angle = (float) (2 * Math.PI / (arms));
		final float cpRadius = radius * 0.5f;
		for (int i = 0; i <= arms; i++) {
			final PointF cp = new PointF();
			final PointF p = new PointF();
			cp.x = (float) (center.x + Math.cos((i - 0.5f) * angle) * cpRadius);
			cp.y = (float) (center.y + Math.sin((i - 0.5f) * angle) * cpRadius);
			p.x = (float) (center.x + Math.cos((i) * angle) * radius);
			p.y = (float) (center.y + Math.sin((i) * angle) * radius);
			if (i == 0) {
				moveTo(p.x, p.y);
			} else {
				quadTo(cp.x, cp.y, p.x, p.y);
			}
		}
		close();
	}
}
