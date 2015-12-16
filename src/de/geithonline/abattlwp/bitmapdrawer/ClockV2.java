package de.geithonline.abattlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import de.geithonline.abattlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient.GRAD_STYLE;
import de.geithonline.abattlwp.bitmapdrawer.data.Outline;
import de.geithonline.abattlwp.bitmapdrawer.data.SkalaLines.LevelLinesStyle;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZColoring;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZMode;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZStyle;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelZeigerPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.Skala;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnCirclePart;
import de.geithonline.abattlwp.bitmapdrawer.shapes.ZeigerShapePath.ZEIGER_TYP;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;
import de.geithonline.abattlwp.utils.GeometrieHelper;

public class ClockV2 extends AdvancedBitmapDrawer {

	private float strokeWidth;

	private float fontSizeLevel;
	private float fontSizeArc;

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
		fontSizeLevel = maxRadius * 0.50f;
		// Radiusses

		radiusChangeText = maxRadius * 0.72f;// - fontSizeArc;
		radiusBattStatus = maxRadius * 0.5f;

	}

	public ClockV2() {
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

		final EZMode levelMode = Settings.getLevelMode();

		new LevelPart(center, maxRadius * 0.98f, maxRadius * 0.92f, level, -90, 360, EZColoring.LevelColors)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(levelMode)//
				.draw(bitmapCanvas);

		// new LevelPart(center, maxRadius * 0.98f, maxRadius * 0.92f, level, -90, 360)//
		// .setStyle(Settings.getLevelStyle())//
		// .configureSegemte(20, 1.0f, strokeWidth / 3)//
		// .draw(bitmapCanvas);

		new RingPart(center, maxRadius * 0.90f, maxRadius * 0.70f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(200), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(64), strokeWidth))//
				.draw(bitmapCanvas);
		new RingPart(center, maxRadius * 0.70f, maxRadius * 0.60f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(180), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(96), strokeWidth))//
				.draw(bitmapCanvas);

		Skala.getLevelScalaCircular(center, maxRadius * 0.63f, maxRadius * 0.67f, -90, LevelLinesStyle.ZehnerFuenferEiner)//
				.setDicke(strokeWidth * 0.75f)//
				.draw(bitmapCanvas);

		new RingPart(center, maxRadius * 0.58f, 0, PaintProvider.getBackgroundPaint())//
				.setOutline(new Outline(PaintProvider.getGray(192, 128), strokeWidth))//
				.draw(bitmapCanvas);

		// Zeiger
		new LevelZeigerPart(center, level, maxRadius * 0.87f, maxRadius * 0.73f, strokeWidth * 3, -90, 360, EZMode.Einer)//
				.setDropShadow(new DropShadow(2 * strokeWidth, Color.BLACK))//
				.setZeigerType(ZEIGER_TYP.raute)//
				.draw(bitmapCanvas);

		if (Settings.isShowExtraLevelBars()) {
			// Timer
			new LevelPart(center, maxRadius * 0.56f, maxRadius * 0.48f, level, 60, -90, EZColoring.Custom)//
					.setColor(Settings.getScaleColor())//
					.setStyle(EZStyle.segmented_all)//
					.setMode(EZMode.EinerOnly10Segmente)//
					.setSegemteAbstand(3f)//
					.setStrokeWidth(strokeWidth / 3)//
					.draw(bitmapCanvas);
			new LevelPart(center, maxRadius * 0.56f, maxRadius * 0.48f, level, 120, 90, EZColoring.Custom)//
					.setColor(Settings.getScaleColor())//
					.setStyle(EZStyle.segmented_all)//
					.setMode(EZMode.Zehner)//
					.setSegemteAbstand(3f)//
					.setStrokeWidth(strokeWidth / 3)//
					.draw(bitmapCanvas);
		}
	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberCentered(bitmapCanvas, level, fontSizeLevel);
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final float winkel = 276 + Math.round(level * 3.6f);
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
