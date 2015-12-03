package de.geithonline.abattlwp.bitmapdrawer.data;

import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;

public class FontAttributes {

	private Align align = Align.LEFT;
	private Typeface typeFace = Typeface.DEFAULT;
	private float fontSize = 10f;

	public FontAttributes(final Align align, final Typeface typeFace, final float fontSize) {
		super();
		this.align = align;
		this.typeFace = typeFace;
		this.fontSize = fontSize;
	}

	public Align getAlign() {
		return align;
	}

	public void setAlign(final Align align) {
		this.align = align;
	}

	public Typeface getTypeFace() {
		return typeFace;
	}

	public void setTypeFace(final Typeface typeFace) {
		this.typeFace = typeFace;
	}

	public float getFontSize() {
		return fontSize;
	}

	public void setFontSize(final float fontSize) {
		this.fontSize = fontSize;
	}

	public Paint setupPaint(final Paint paint) {
		paint.setTextSize(fontSize);
		paint.setTypeface(typeFace);
		paint.setTextAlign(align);
		return paint;
	}

}
