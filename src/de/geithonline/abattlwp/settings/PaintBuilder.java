package de.geithonline.abattlwp.settings;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import de.geithonline.abattlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.abattlwp.bitmapdrawer.data.FontAttributes;

public class PaintBuilder {
	private final Paint paint;

	public PaintBuilder(final Paint paint) {
		this.paint = paint;
	}

	public PaintBuilder() {
		paint = new Paint();
		paint.setStyle(Style.FILL);
	}

	public Paint toPaint() {
		return paint;
	}

	public PaintBuilder setColor(final int color) {
		paint.setColor(color);
		return this;
	}

	public PaintBuilder setAlpha(final int alpha) {
		paint.setAlpha(alpha);
		return this;
	}

	public PaintBuilder setStyleFill() {
		paint.setStyle(Style.FILL);
		return this;
	}

	public PaintBuilder setStyleStroke(final int strokeWidth) {
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(strokeWidth);
		return this;
	}

	public PaintBuilder setStyleFillAndStroke(final int strokeWidth) {
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setStrokeWidth(strokeWidth);
		return this;
	}

	public PaintBuilder setEraser() {
		paint.setColor(Color.TRANSPARENT);
		final PorterDuffXfermode xfermode = new PorterDuffXfermode(Mode.CLEAR);
		paint.setXfermode(xfermode);
		return this;
	}

	public PaintBuilder setFontAttributes(final FontAttributes attr) {
		attr.setupPaint(paint);
		return this;
	}

	public PaintBuilder setDropShadow(final DropShadow dropshadow) {
		dropshadow.setUpPaint(paint);
		return this;
	}

}
