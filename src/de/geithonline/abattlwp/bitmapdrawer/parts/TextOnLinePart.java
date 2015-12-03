package de.geithonline.abattlwp.bitmapdrawer.parts;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import de.geithonline.abattlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.abattlwp.bitmapdrawer.data.FontAttributes;
import de.geithonline.abattlwp.utils.PathHelper;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Typeface;

public class TextOnLinePart {

	private final float winkel;
	private final PointF center;
	private final float radius;

	private final Paint paint;
	private FontAttributes attr;
	private boolean invert = false;

	public TextOnLinePart(final PointF center, final float radius, final float winkel, final float fontsize, final Paint paint) {
		this.winkel = winkel;
		this.center = center;
		this.radius = radius;
		this.paint = paint;
		attr = new FontAttributes(Align.LEFT, Typeface.DEFAULT_BOLD, fontsize);
		attr.setupPaint(this.paint);
	}

	public TextOnLinePart setColor(final int color) {
		paint.setColor(color);
		return this;
	}

	public TextOnLinePart setAlpha(final int alpha) {
		paint.setAlpha(alpha);
		return this;
	}

	public TextOnLinePart setAlign(final Align align) {
		attr.setAlign(align);
		attr.setupPaint(paint);
		return this;
	}

	public TextOnLinePart setTypeface(final Typeface typeFace) {
		attr.setTypeFace(typeFace);
		attr.setupPaint(paint);
		return this;
	}

	public TextOnLinePart setDropShadow(final DropShadow dropShadow) {
		if (dropShadow != null) {
			dropShadow.setUpPaint(paint);
		}
		return this;
	}

	public TextOnLinePart setTextStyle(final FontAttributes attr) {
		this.attr = attr;
		attr.setupPaint(paint);
		return this;
	}

	public TextOnLinePart invert(final boolean invert) {
		this.invert = invert;
		return this;
	}

	public TextOnLinePart draw(final Canvas canvas, final String text) {
		final Path p = new Path();
		int faktor = 1;
		if (invert) {
			faktor = -1;
		}

		switch (attr.getAlign()) {
		default:
		case LEFT:
			p.moveTo(center.x + radius, center.y);
			p.lineTo(center.x + radius, center.y + 200 * faktor);
			break;
		case CENTER:
			p.moveTo(center.x + radius, center.y - 100 * faktor);
			p.lineTo(center.x + radius, center.y + 100 * faktor);
			break;
		case RIGHT:
			p.moveTo(center.x + radius, center.y);
			p.lineTo(center.x + radius, center.y - 200 * faktor);
			break;
		}
		PathHelper.rotatePath(center.x, center.y, p, winkel);
		canvas.drawTextOnPath(text, p, 0, 0, paint);
		return this;
	}

}
