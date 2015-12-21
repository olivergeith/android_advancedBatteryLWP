package de.geithonline.abattlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import de.geithonline.abattlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.abattlwp.bitmapdrawer.data.FontAttributes;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient.GRAD_STYLE;
import de.geithonline.abattlwp.bitmapdrawer.data.Outline;
import de.geithonline.abattlwp.bitmapdrawer.data.SkalaLines.LevelLinesStyle;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZColoring;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZMode;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZStyle;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelPart;
//import de.geithonline.abattlwp.bitmapdrawer.parts.SkalaLinePart;
//import de.geithonline.abattlwp.bitmapdrawer.parts.SkalaTextPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelZeigerPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.Skala;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnCirclePart;
import de.geithonline.abattlwp.bitmapdrawer.shapes.ZeigerShapePath.ZEIGER_TYP;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;
import de.geithonline.abattlwp.utils.GeometrieHelper;

public class ClockV4 extends AdvancedBitmapDrawer {

	private float strokeWidth;

	private float fontSizeLevel;
	private float fontSizeArc;
	private float fontSizeScala;

	private float maxRadius;

	private final PointF center = new PointF();

	private void initPrivateMembers() {
		center.x = bmpWidth / 2;
		center.y = bmpHeight / 2;
		maxRadius = bmpWidth / 2;
		// Strokes
		strokeWidth = maxRadius * 0.02f;
		// fontsizes
		fontSizeArc = maxRadius * 0.07f;
		fontSizeScala = maxRadius * 0.1f;
		fontSizeLevel = maxRadius * 0.2f;

	}

	public ClockV4() {
	}

	@Override
	public boolean supportsPointerColor() {
		return true;
	}

	@Override
	public boolean supportsGlowScala() {
		return true;
	}

	@Override
	public boolean supportsExtraLevelBars() {
		return true;
	}

	@Override
	public boolean isPremiumDrawer() {
		return true;
	}

	@Override
	public Bitmap drawBitmap(final int level, final Bitmap bitmap) {
		initPrivateMembers();

		drawAll(level);
		return bitmap;
	}

	private void drawAll(final int level) {

		new RingPart(center, maxRadius * 0.98f, maxRadius * 0.88f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(160), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(224), strokeWidth * 0.75f))//
				.draw(bitmapCanvas);
		// scala
		new RingPart(center, maxRadius * 0.88f, 0, PaintProvider.getBackgroundPaint())//
				// .setOutline(new Outline(Color.WHITE, strokeWidth))//
				.draw(bitmapCanvas);

		// level ring
		new LevelPart(center, maxRadius * 0.86f, maxRadius * 0.72f, level, -90, 360, EZColoring.LevelColors)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setMode(EZMode.Einer)//
				.setStyle(EZStyle.sweep)//
				.draw(bitmapCanvas);

		// Gegenläufiger Level
		new LevelPart(center, maxRadius * 0.70f, maxRadius * 0.66f, 100 - level, -90, -360, EZColoring.ColorOf100)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setMode(EZMode.Einer)//
				.setStyle(EZStyle.segmented_onlyactive)//
				.draw(bitmapCanvas);

		Skala.getLevelScalaCircular(center, maxRadius * 0.86f, maxRadius * 0.90f, -90, LevelLinesStyle.ZehnerFuenferEiner)//
				.setDicke(strokeWidth * 0.75f)//
				.setFontAttributesEbene1(new FontAttributes(fontSizeScala))//
				.setFontRadiusEbene1(maxRadius * 0.75f)//
				.setFontAttributesEbene2Default()//
				.setupLineRadienAllTheSame()//
				.draw(bitmapCanvas);

		// SkalaGlow
		if (Settings.isShowGlowScala()) {
			new RingPart(center, maxRadius * 0.20f, 0f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 20, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
			new RingPart(center, maxRadius * 0.20f, 0f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 10, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
			new RingPart(center, maxRadius * 0.20f, 0f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 3, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
		}

		// Timer
		if (Settings.isShowExtraLevelBars()) {
			final float maxScala = 120f;
			final float ri = maxRadius * 0.54f;
			new LevelPart(center, maxRadius * 0.64f, ri, level, -225, maxScala, EZColoring.ColorOf100)//
					.setSegemteAbstand(1.5f)//
					.setStrokeWidth(strokeWidth / 3)//
					.setMode(EZMode.Zehner)//
					.setStyle(EZStyle.segmented_all)//
					.draw(bitmapCanvas);
			new LevelZeigerPart(center, level, ri, maxRadius * 0.20f, strokeWidth * 1.0f, -225, maxScala, EZMode.Zehner)//
					.setDropShadow(new DropShadow(1.5f * strokeWidth, 0, 1.5f * strokeWidth, Color.BLACK))//
					.setZeigerType(ZEIGER_TYP.rect)//
					.draw(bitmapCanvas);

			new LevelPart(center, maxRadius * 0.64f, ri, level, 45, -maxScala, EZColoring.ColorOf100)//
					.setSegemteAbstand(1.5f)//
					.setStrokeWidth(strokeWidth / 3)//
					.setMode(EZMode.EinerOnly10Segmente)//
					.setStyle(EZStyle.segmented_all)//
					.draw(bitmapCanvas);
			new LevelZeigerPart(center, level, ri, maxRadius * 0.20f, strokeWidth * 1.0f, 45, -maxScala, EZMode.EinerOnly10Segmente)//
					.setDropShadow(new DropShadow(1.5f * strokeWidth, 0, 1.5f * strokeWidth, Color.BLACK))//
					.setZeigerType(ZEIGER_TYP.rect)//
					.draw(bitmapCanvas);
		}
		// Zeiger
		new LevelZeigerPart(center, level, maxRadius * 0.78f, maxRadius * 0.20f, strokeWidth * 1.5f, -90, 360, EZMode.Einer)//
				.setDropShadow(new DropShadow(1.5f * strokeWidth, 0, 1.5f * strokeWidth, Color.BLACK))//
				.setZeigerType(ZEIGER_TYP.rect)//
				// .overrideColor(Color.RED)//
				.draw(bitmapCanvas);
		// innere fläche
		new RingPart(center, maxRadius * 0.20f, 0f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(160), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				// .setColor(Color.WHITE)//
				.setOutline(new Outline(PaintProvider.getGray(224), strokeWidth * 0.75f))//
				.draw(bitmapCanvas);

	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumber(bitmapCanvas, level, fontSizeLevel * 1.5f, new PointF(center.x, center.x + maxRadius * 0.55f));
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = 272 + Math.round(level * 3.6f);
		new TextOnCirclePart(center, maxRadius * 0.91f, winkel, fontSizeArc, new Paint())//
				.setColor(Settings.getChargeStatusColor())//
				.setAlign(Align.LEFT)//
				.draw(bitmapCanvas, Settings.getChargingText());
	}

	@Override
	public void drawBattStatusText(final int level) {
		final Path mArc = new Path();
		final RectF oval = GeometrieHelper.getCircle(center, maxRadius * 0.4f);
		mArc.addArc(oval, 180, 180);
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = PaintProvider.getTextBattStatusPaint(fontSizeArc, Align.CENTER, true);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

}
