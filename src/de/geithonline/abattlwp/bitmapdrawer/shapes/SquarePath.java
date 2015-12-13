package de.geithonline.abattlwp.bitmapdrawer.shapes;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

public class SquarePath extends Path {

	public enum SQUARE_STYLE {
		ROUNDED, NORMAL;
	}

	public SquarePath(final PointF center, final float ra, final float ri, final SQUARE_STYLE variant) {
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
			rect.left = center.x - ri;
			rect.right = center.x + ri;
			rect.top = center.y - ri;
			rect.bottom = center.y + ri;
			if (!rounded) {
				addRect(rect, dInner);
			} else {
				final float cornerRad = ri * 0.15f;
				addRoundRect(rect, cornerRad, cornerRad, dInner);
			}
		}
	}
}
