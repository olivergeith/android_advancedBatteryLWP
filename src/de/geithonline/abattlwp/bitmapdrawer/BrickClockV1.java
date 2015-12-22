package de.geithonline.abattlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZColoring;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZMode;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZStyle;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnCirclePart;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;
import de.geithonline.abattlwp.utils.GeometrieHelper;

public class BrickClockV1 extends AdvancedBitmapDrawer {

	private float strokeWidth;

	private float fontSizeLevel;
	private float fontSizeArc;
	private float fontSizeScala;

	private float maxRadius;
	private float radiusChangeText;
	private float radiusBattStatus;

	private final PointF center = new PointF();

	private void initPrivateMembers() {
		center.x = bmpWidth / 2;
		center.y = bmpHeight / 2;
		maxRadius = bmpWidth / 2;
		// Strokes
		strokeWidth = maxRadius * 0.02f;
		// fontsizes
		fontSizeArc = maxRadius * 0.08f;
		fontSizeScala = maxRadius * 0.1f;
		fontSizeLevel = maxRadius * 0.2f;
		// Radiusses

		radiusChangeText = maxRadius - fontSizeArc;
		radiusBattStatus = maxRadius * 0.5f;
	}

	public BrickClockV1() {
	}

	@Override
	public boolean supportsPointerColor() {
		return true;
	}

	@Override
	public boolean supportsExtraLevelBars() {
		return true;
	}

	@Override
	public Bitmap drawBitmap(final int level, final Bitmap bitmap) {
		initPrivateMembers();
		drawAll(level);
		return bitmap;
	}

	private void drawAll(final int level) {
		int zehner = level / 10;
		int einer = level % 10;
		if (einer == 0) {
			zehner = zehner - 1;
			einer = 10;
		}
		for (int z = 0; z < 10; z++) {
			final float aussen = maxRadius * 0.90f;
			final float dicke = maxRadius * 0.07f;
			final float spacing = maxRadius * 0.02f;
			final float ra = aussen - z * dicke;
			final float ri = ra - dicke + spacing;
			// inputvalue für diesen Ring berechnenen
			int input = 0;
			if (zehner == -1) {
				input = 0;
			} else if (zehner == z) {
				input = einer * 10; // mal 10, weil der levelbar im zehnermode ist
			} else if (z < zehner) {
				input = 10 * 10;
			} else {
				input = 0;
			}

			// Level
			new LevelPart(center, ra, ri, input, -90, 360, EZColoring.ColorOf100)//
					.setColor(PaintProvider.getColorForLevel(level))//
					.setSegemteAbstand(2f)//
					.setStrokeWidth(strokeWidth / 3)//
					.setStyle(EZStyle.segmented_all_backgroundPaint)//
					.setMode(EZMode.Zehner)// Zehner, weil nur der von 1 bis zehn zählt
					.draw(bitmapCanvas);
		}

	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberCentered(bitmapCanvas, level, fontSizeLevel);
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final float winkel = 272 + Math.round(level * 3.6f);
		new TextOnCirclePart(center, radiusChangeText, winkel, fontSizeArc, new Paint())//
				.setColor(Settings.getChargeStatusColor())//
				.setAlign(Align.LEFT)//
				.draw(bitmapCanvas, Settings.getChargingText());
	}

	@Override
	public void drawBattStatusText(final int level) {
		final Path mArc = new Path();
		final RectF oval = GeometrieHelper.getCircle(center, radiusBattStatus);
		mArc.addArc(oval, 180, 180);
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = PaintProvider.getTextBattStatusPaint(fontSizeArc, Align.CENTER, true);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

}
