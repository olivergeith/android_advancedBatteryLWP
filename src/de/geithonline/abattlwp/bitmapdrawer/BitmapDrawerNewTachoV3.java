package de.geithonline.abattlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import de.geithonline.abattlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.abattlwp.bitmapdrawer.data.Outline;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZColoring;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZMode;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.SkalaLinePart;
import de.geithonline.abattlwp.bitmapdrawer.parts.SkalaTextPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.ZeigerPart;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;
import de.geithonline.abattlwp.utils.GeometrieHelper;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

public class BitmapDrawerNewTachoV3 extends AdvancedBitmapDrawer {

	private float strokeWidth;

	private float fontSizeLevel;
	private float fontSizeArc;

	private float maxRadius;

	private final PointF center = new PointF();

	private float fontSizeScala;

	private void initPrivateMembers() {
		center.x = bmpWidth / 2;
		center.y = bmpHeight / 2;

		maxRadius = bmpWidth / 2;
		// Strokes
		strokeWidth = maxRadius * 0.02f;
		// fontsizes
		fontSizeArc = maxRadius * 0.08f;
		fontSizeScala = maxRadius * 0.1f;
		fontSizeLevel = maxRadius * 0.54f;
	}

	public BitmapDrawerNewTachoV3() {
	}

	@Override
	public boolean supportsPointerColor() {
		return true;
	}

	@Override
	public boolean supportsLevelStyle() {
		return true;
	}

	@Override
	public boolean supportsShowPointer() {
		return true;
	}

	@Override
	public boolean supportsShowRand() {
		return true;
	}

	@Override
	public Bitmap drawBitmap(final int level, final Bitmap bitmap) {
		initPrivateMembers();
		drawAll(level);
		return bitmap;
	}

	private void drawAll(final int level) {

		// Äußerer Rand
		if (Settings.isShowRand()) {
			// SkalaBackground
			new RingPart(center, maxRadius * 0.90f, maxRadius * 0.6f, PaintProvider.getBackgroundPaint())//
					.setOutline(new Outline(Color.WHITE, strokeWidth / 2))//
					.draw(bitmapCanvas);
		} else {
			new RingPart(center, maxRadius * 0.90f, maxRadius * 0.6f, PaintProvider.getBackgroundPaint())//
					.draw(bitmapCanvas);
		}
		// Level
		new LevelPart(center, maxRadius * 0.88f, maxRadius * 0.60f, level, -90, 360, EZColoring.LevelColors)//
				.setSegemteAbstand(0.9f)//
				.setStrokeWidth(strokeWidth / 2)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(Settings.getLevelMode())//
				.draw(bitmapCanvas);
		// Skala
		new SkalaLinePart(center, maxRadius * 0.65f, maxRadius * 0.6f, -90, 360)//
				.set5erRadiusAussen(maxRadius * 0.63f)//
				.setDicke10er(strokeWidth / 2)//
				.setDicke5er(strokeWidth / 3)//
				.setDraw100(true)//
				.draw(bitmapCanvas);

		new SkalaTextPart(center, maxRadius * 0.67f, fontSizeScala, -90, 360)//
				.setDraw0(true)//
				.draw(bitmapCanvas);
		// innere Fläche
		new RingPart(center, maxRadius * 0.6f, maxRadius * 0, PaintProvider.getBackgroundPaint())//
				.setOutline(new Outline(Color.WHITE, strokeWidth))//
				.draw(bitmapCanvas);
		if (Settings.isShowZeiger()) {
			// Zeiger
			new ZeigerPart(center, level, maxRadius * 1f, maxRadius * 0.6f, strokeWidth, -90, 360, EZMode.Einer)//
					.setDropShadow(new DropShadow(2 * strokeWidth, Color.BLACK))//
					.draw(bitmapCanvas);
		}
	}

	@Override
	public void drawLevelNumber(final int level) {
		// drawLevelNumberBottom(bitmapCanvas, level, fontSizeLevel);
		drawLevelNumberCentered(bitmapCanvas, level, fontSizeLevel);
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = 272 + Math.round(level * 3.6f);

		final Path mArc = new Path();
		final RectF oval = GeometrieHelper.getCircle(center, maxRadius * 0.91f);
		mArc.addArc(oval, winkel, 180);
		final String text = Settings.getChargingText();
		final Paint p = PaintProvider.getTextPaint(level, fontSizeArc);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

	@Override
	public void drawBattStatusText() {
		final Path mArc = new Path();
		final RectF oval = GeometrieHelper.getCircle(center, maxRadius * 0.50f);
		mArc.addArc(oval, 180, 180);
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = PaintProvider.getTextBattStatusPaint(fontSizeArc, Align.CENTER, true);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

}
