package de.geithonline.abattlwp.bitmapdrawer.shapes;

import android.graphics.Path;
import android.graphics.Point;

public class BubbleCirclePath extends Path {

	public BubbleCirclePath(final int bubbles, final Point center, final float rOuter, final float rBubble) {
		super();
		final float angle = (float) (Math.PI / bubbles) * 2;

		for (int i = 0; i < bubbles; i = i + 1) {

			final float r = rOuter;

			final Point p = new Point();
			p.x = (int) (center.x + Math.sin(i * angle) * r);
			p.y = (int) (center.y + Math.cos(i * angle) * r);
			// Log.i("Point", "Point " + i + " p=" + p.x + " " + p.y);
			addCircle(p.x, p.y, rBubble, Direction.CCW);
		}
	}
}
