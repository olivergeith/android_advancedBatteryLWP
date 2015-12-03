package de.geithonline.abattlwp.bitmapdrawer.parts;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import de.geithonline.abattlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.abattlwp.bitmapdrawer.data.FontAttributes;
import de.geithonline.abattlwp.utils.GeometrieHelper;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;

public class TextOnCirclePart {

	private final float winkel;
	private final PointF center;
	private final float radius;

	private final Paint paint;
	private FontAttributes attr;
	private boolean invert = false;

	public TextOnCirclePart(final PointF center, final float radius, final float winkel, final float fontsize, final Paint paint) {
		this.winkel = winkel;
		this.center = center;
		this.radius = radius;
		this.paint = paint;
		attr = new FontAttributes(Align.LEFT, Typeface.DEFAULT_BOLD, fontsize);
		attr.setupPaint(this.paint);
	}

	public TextOnCirclePart setColor(final int color) {
		paint.setColor(color);
		return this;
	}

	public TextOnCirclePart setAlpha(final int alpha) {
		paint.setAlpha(alpha);
		return this;
	}

	public TextOnCirclePart setAlign(final Align align) {
		attr.setAlign(align);
		attr.setupPaint(paint);
		return this;
	}

	public TextOnCirclePart setTypeface(final Typeface typeFace) {
		attr.setTypeFace(typeFace);
		attr.setupPaint(paint);
		return this;
	}

	public TextOnCirclePart setDropShadow(final DropShadow dropShadow) {
		if (dropShadow != null) {
			dropShadow.setUpPaint(paint);
		}
		return this;
	}

	public TextOnCirclePart setTextStyle(final FontAttributes attr) {
		this.attr = attr;
		attr.setupPaint(paint);
		return this;
	}

	public TextOnCirclePart invert(final boolean invert) {
		this.invert = invert;
		return this;
	}

	public TextOnCirclePart draw(final Canvas canvas, final String text) {
		final Path mArc = new Path();
		final RectF oval = GeometrieHelper.getCircle(center, radius);
		int faktor = 1;
		if (invert) {
			faktor = -1;
		}
		switch (attr.getAlign()) {
		default:
		case LEFT:
			mArc.addArc(oval, winkel, 340 * faktor);
			break;
		case CENTER:
			mArc.addArc(oval, winkel - 170 * faktor, 340 * faktor);
			break;
		case RIGHT:
			mArc.addArc(oval, winkel - 340 * faktor, 340 * faktor);
			break;
		}
		canvas.drawTextOnPath(text, mArc, 0, 0, paint);
		return this;
	}

}
