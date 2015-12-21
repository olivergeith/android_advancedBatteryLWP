package de.geithonline.abattlwp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Typeface;
import de.geithonline.abattlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.abattlwp.settings.PaintProvider;

public class PremiumBannerDrawer {

	private final Canvas canvas;
	private final int w;
	private final int h;
	private final float strokeWidth;
	private final DropShadow dropShadow;

	public PremiumBannerDrawer(final Canvas canvas) {
		this.canvas = canvas;
		w = canvas.getWidth();
		h = canvas.getHeight();
		strokeWidth = w * 0.01f;
		dropShadow = new DropShadow(strokeWidth * 2, strokeWidth, strokeWidth, Color.BLACK);
	}

	private Paint getPaint() {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
		paint.setStrokeWidth(strokeWidth);
		paint.setTextAlign(Align.CENTER);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		dropShadow.setUpPaint(paint);
		return paint;
	}

	public void drawVerticalBow() {
		final float off = strokeWidth / 2;
		final float hightBanner = w * 0.2f;
		final float rand = (h - hightBanner) / 2;
		final float textsize = w * 0.04f;
		final Paint paint = getPaint();
		// Rechteck malen

		final PointF oL = new PointF(off, rand);
		final PointF oR = new PointF(w - off, rand);
		final PointF uL = new PointF(off, h - rand);
		final PointF uR = new PointF(w - off, h - rand);
		final PointF cpO = new PointF(w / 2, rand - hightBanner); // controllpoint oben
		final PointF cpU = new PointF(w / 2, h - rand - hightBanner); // controllpoint unten

		final Path bow = new Path();
		bow.moveTo(uL.x, uL.y);
		bow.quadTo(cpU.x, cpU.y, uR.x, uR.y);
		bow.lineTo(oR.x, oR.y);
		bow.quadTo(cpO.x, cpO.y, oL.x, oL.y);
		bow.close();

		final Path line = new Path();
		line.moveTo(uL.x, uL.y);
		line.quadTo(cpU.x, cpU.y, uR.x, uR.y);

		paint.setStyle(Style.FILL);
		paint.setColor(PaintProvider.getGray(128, 128));
		canvas.drawPath(bow, paint);
		paint.setStyle(Style.STROKE);
		paint.setColor(PaintProvider.getGray(255, 128));
		canvas.drawPath(bow, paint);

		// Text schreiben
		paint.setStyle(Style.FILL);
		paint.setTextSize(textsize);
		paint.setColor(PaintProvider.getGray(255, 255));
		canvas.drawTextOnPath("Get the", line, 0, -3.3f * textsize, paint);

		paint.setColor(Color.argb(255, 0, 170, 255));
		paint.setTextSize(textsize * 1.3f);
		canvas.drawTextOnPath("Premium Version", line, 0, -2 * textsize, paint);

		paint.setTextSize(textsize);
		paint.setColor(PaintProvider.getGray(255, 255));
		canvas.drawTextOnPath("... to remove this banner ...", line, 0, -1f * textsize, paint);
	}
}
