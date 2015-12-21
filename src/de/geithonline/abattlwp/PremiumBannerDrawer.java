package de.geithonline.abattlwp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import de.geithonline.abattlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.abattlwp.settings.PaintProvider;

public class PremiumBannerDrawer {

	private final Canvas canvas;
	private final int w;
	private final int h;
	private final float strokeWidth;
	private final DropShadow dropShadow;
	// private final Bitmap icon;

	public PremiumBannerDrawer(final Canvas canvas) {
		this.canvas = canvas;
		w = canvas.getWidth();
		h = canvas.getHeight();
		strokeWidth = w * 0.02f;
		dropShadow = new DropShadow(strokeWidth * 2, strokeWidth, strokeWidth, Color.BLACK);
		// final IBitmapDrawer drawer = DrawerManager.getDrawer(DrawerManager.APP_ICON_DRAWER_NAME);
		// icon = drawer.drawIcon(66, (int) (w * 0.1f));

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

	public void drawVerticalBanner() {
		final float randLinksRechts = w * 0.05f;
		final float hightBanner = w * 0.25f;
		final float randObenUnten = (h - hightBanner) / 2;
		final float textsize = w * 0.08f;
		final Paint paint = getPaint();
		// Rechteck malen
		final RectF rect = new RectF();
		rect.left = randLinksRechts;
		rect.right = w - randLinksRechts;
		rect.top = randObenUnten;
		rect.bottom = h - randObenUnten;
		paint.setColor(PaintProvider.getGray(128, 96));
		canvas.drawRect(rect, paint);
		// Outline um rechteck
		paint.setStyle(Style.STROKE);
		paint.setColor(PaintProvider.getGray(160, 224));
		canvas.drawRoundRect(rect, strokeWidth, strokeWidth, paint);

		// Text schreiben
		paint.setStyle(Style.FILL);
		paint.setTextSize(textsize);
		paint.setColor(PaintProvider.getGray(255, 255));
		rect.left = randLinksRechts;
		rect.right = w - randLinksRechts;
		rect.top = randObenUnten;
		rect.bottom = h / 2;
		PointF point = getTextCenterToDraw(rect, paint);
		canvas.drawText("Premium Version Only", point.x, point.y, paint);
		rect.left = randLinksRechts;
		rect.right = w - randLinksRechts;
		rect.top = h / 2;
		rect.bottom = h - randObenUnten;
		paint.setTextSize(textsize * 0.45f);
		point = getTextCenterToDraw(rect, paint);
		canvas.drawText("Get the Premium Version to remove this banner", point.x, point.y, paint);

	}

	public void drawTopLeftDiagonalBanner() {
		final Paint paint = getPaint();
		final float off = strokeWidth / 2;
		final PointF lU = new PointF(off, w / 2);
		final PointF lO = new PointF(off, off);
		final PointF rO = new PointF(w / 2, off);
		final float textsize = w * 0.04f;

		final Path dreieck = new Path();
		dreieck.moveTo(lO.x, lO.y);
		dreieck.lineTo(rO.x, rO.y);
		dreieck.lineTo(lU.x, lU.y);
		dreieck.close();
		// Dreieck malen
		paint.setColor(PaintProvider.getGray(128, 128));
		canvas.drawPath(dreieck, paint);
		// Outline um rechteck
		paint.setStyle(Style.STROKE);
		paint.setColor(PaintProvider.getGray(255, 128));
		canvas.drawPath(dreieck, paint);

		// Text schreiben
		paint.setStyle(Style.FILL);
		final Path line = new Path();
		line.moveTo(lU.x, lU.y);
		line.lineTo(rO.x, rO.y);

		paint.setTextSize(textsize);
		paint.setColor(PaintProvider.getGray(255, 255));
		canvas.drawTextOnPath("Get the", line, 0, -3.3f * textsize, paint);

		paint.setColor(Color.argb(255, 0, 170, 255));
		paint.setTextSize(textsize * 1.3f);
		canvas.drawTextOnPath("Premium Version", line, 0, -2 * textsize, paint);

		paint.setTextSize(textsize);
		paint.setColor(PaintProvider.getGray(255, 255));
		canvas.drawTextOnPath("... to remove this banner ...", line, 0, -1f * textsize, paint);

		// canvas.drawBitmap(icon, 2 * strokeWidth, 2 * strokeWidth, paint);

	}

	private static PointF getTextCenterToDraw(final RectF region, final Paint paint) {
		final Rect textBounds = new Rect();
		paint.getTextBounds("69", 0, 2, textBounds);
		final float x = region.centerX();
		final float y = region.centerY() + textBounds.height() * 0.5f;
		return new PointF(x, y);
	}

}
