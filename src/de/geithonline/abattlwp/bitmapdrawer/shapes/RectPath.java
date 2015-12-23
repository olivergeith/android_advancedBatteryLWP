package de.geithonline.abattlwp.bitmapdrawer.shapes;

import android.graphics.Path;
import android.graphics.RectF;

public class RectPath extends Path {

	public enum RECT_STYLE {
		ROUNDED, NORMAL;
	}

	public RectPath(final RectF rect) {
		super();
		draw(rect, RECT_STYLE.NORMAL);
	}

	public RectPath(final RectF rect, final RECT_STYLE variant) {
		super();
		draw(rect, variant);
	}

	private void draw(final RectF rect, final RECT_STYLE variant) {
		final Direction dOuter = Direction.CW;
		boolean rounded = false;
		switch (variant) {
			case ROUNDED:
				rounded = true;
				break;
			case NORMAL:
				rounded = false;
				break;
		}

		if (!rounded) {
			addRect(rect, dOuter);
		} else {
			final float cornerRad = rect.height() * 0.15f;
			addRoundRect(rect, cornerRad, cornerRad, dOuter);
		}
	}
}
