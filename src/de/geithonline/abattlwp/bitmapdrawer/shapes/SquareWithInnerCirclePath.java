package de.geithonline.abattlwp.bitmapdrawer.shapes;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

public class SquareWithInnerCirclePath extends Path {

	public enum SQUARE2_STYLE {
		ROUNDED, NORMAL;
	}

	public SquareWithInnerCirclePath(final PointF center, final float ra, final float ri, final SQUARE2_STYLE variant) {
		super();
		final Direction dOuter = Direction.CW;
		final Direction dInner = Direction.CCW;

		boolean rounded = false;
		switch (variant) {
			case ROUNDED:
				rounded = true;
				break;
			case NORMAL:
				rounded = false;
				break;
		}

		final RectF rect = new RectF();
		rect.left = center.x - ra;
		rect.right = center.x + ra;
		rect.top = center.y - ra;
		rect.bottom = center.y + ra;
		if (!rounded) {
			addRect(rect, dOuter);
		} else {
			final float cornerRad = ra * 0.15f;
			addRoundRect(rect, cornerRad, cornerRad, dOuter);
		}
		if (ri > 0) {
			addCircle(center.x, center.y, ri, dInner);
		}
	}
}
