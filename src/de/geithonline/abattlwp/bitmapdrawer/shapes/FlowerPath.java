package de.geithonline.abattlwp.bitmapdrawer.shapes;

import android.graphics.Path;
import android.graphics.PointF;

public class FlowerPath extends Path {

	public FlowerPath(final int arms, final PointF center, final float ra, final float ri) {
		super();
		drawStar(arms, center, ra, ri, Direction.CW);
	}

	public FlowerPath(final int arms, final PointF center, final float ra, final float ri, final Direction dir) {
		super();
		drawStar(arms, center, ra, ri, dir);
	}

	private void drawStar(final int arms, final PointF center, final float ra, final float ri, final Direction dir) {
		float angle = (float) (Math.PI / arms);
		if (dir.equals(Direction.CCW)) {
			angle = angle * -1;
		}
		final PointF cp = new PointF();
		final float cprad = ri + (ra - ri) * 0.65f;
		int a = 0;
		for (int i = 0; i <= arms; i++) {
			for (int j = 0; j < 2; j++) {
				float r;
				if (j == 0) {
					r = ra;
				} else {
					r = ri;
					cp.x = (float) (center.x + Math.cos(a * angle) * cprad);
					cp.y = (float) (center.y + Math.sin(a * angle) * cprad);
				}
				final PointF p = new PointF();
				p.x = (float) (center.x + Math.cos(a * angle) * r);
				p.y = (float) (center.y + Math.sin(a * angle) * r);
				if (i == 0) {
					moveTo(p.x, p.y);
				} else {
					quadTo(cp.x, cp.y, p.x, p.y);
				}
				a++;
			}
		}
		close();
	}
}
