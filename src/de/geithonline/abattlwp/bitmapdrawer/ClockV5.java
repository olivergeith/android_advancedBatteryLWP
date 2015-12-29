package de.geithonline.abattlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
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
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;
import de.geithonline.abattlwp.utils.GeometrieHelper;

public class ClockV5 extends AdvancedBitmapDrawer {

	private float strokeWidth;

	private float fontSizeLevel;
	private float fontSizeArc;
	private float fontSizeScala;

	private float maxRadius;
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
		fontSizeScala = maxRadius * 0.08f;
		fontSizeLevel = maxRadius * 0.2f;
		// Radiusses

		radiusBattStatus = maxRadius * 0.42f;

	}

	public ClockV5() {
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
		final float sweep = 270;
		final float startWinkel = -225;
		// Ausen Ring
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.75f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(64), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(128), strokeWidth))//
				.draw(bitmapCanvas);
		// SkalaBackground
		new RingPart(center, maxRadius * 0.74f, maxRadius * 0f, PaintProvider.getBackgroundPaint())//
				.draw(bitmapCanvas);

		// Level
		new LevelPart(center, maxRadius * 0.71f, maxRadius * 0.60f, level, startWinkel, sweep, EZColoring.LevelColors)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(Settings.getLevelMode())//
				// .setDropShadow(new DropShadow(strokeWidth * 1, Color.BLACK))//
				.draw(bitmapCanvas);

		// Innen Phase
		if (Settings.isShowGlowScala()) {
			new RingPart(center, maxRadius * 0.32f, maxRadius * 0.25f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 10, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
			new RingPart(center, maxRadius * 0.32f, maxRadius * 0.25f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 5, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
		}
		new RingPart(center, maxRadius * 0.31f, maxRadius * 0.25f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(224), PaintProvider.getGray(48), GRAD_STYLE.top2bottom))//
				.draw(bitmapCanvas);

		// Innen Fläche
		new RingPart(center, maxRadius * 0.25f, maxRadius * 0.00f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(224), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(32), strokeWidth))//
				.draw(bitmapCanvas);

		Skala.getLevelScalaArch(center, maxRadius * 0.76f, maxRadius * 0.82f, startWinkel, sweep, LevelLinesStyle.ZehnerFuenferEiner)//
				.setDicke(strokeWidth * 0.75f)//
				.setFontAttributesEbene1(new FontAttributes(Align.CENTER, Typeface.DEFAULT_BOLD, fontSizeScala))//
				.setFontAttributesEbene2Default()//
				.draw(bitmapCanvas);
		// Zeiger
		new LevelZeigerPart(center, level, maxRadius * 0.80f, maxRadius * 0.26f, strokeWidth, startWinkel, sweep, EZMode.Einer)//
				.setDropShadow(new DropShadow(1.5f * strokeWidth, 0, 1.5f * strokeWidth, Color.BLACK))//
				.draw(bitmapCanvas);

		if (Settings.isShowExtraLevelBars()) {
			// ##############################################
			// Playing
			new LevelPart(center, maxRadius * 0.71f, maxRadius * 0.66f, level, startWinkel - 5, -80, EZColoring.ColorOf100)//
					.setSegemteAbstand(1.5f)//
					.setStrokeWidth(strokeWidth / 3)//
					.setStyle(EZStyle.segmented_all)//
					.setMode(EZMode.Zehner)//
					.draw(bitmapCanvas);

			new LevelPart(center, maxRadius * 0.64f, maxRadius * 0.60f, level, startWinkel - 5, -80, EZColoring.ColorOf100)//
					.setSegemteAbstand(1.5f)//
					.setStrokeWidth(strokeWidth / 3)//
					.setStyle(EZStyle.segmented_all)//
					.setMode(EZMode.EinerOnly10Segmente)//
					.draw(bitmapCanvas);
			new LevelZeigerPart(center, level, maxRadius * 0.66f, maxRadius * 0.33f, strokeWidth, startWinkel - 5, -80, EZMode.Zehner)//
					.setDropShadow(new DropShadow(1.5f * strokeWidth, 0, 1.5f * strokeWidth, Color.BLACK))//
					// .overrideColor(Color.WHITE)//
					.draw(bitmapCanvas);
			new LevelZeigerPart(center, level, maxRadius * 0.60f, maxRadius * 0.33f, strokeWidth, startWinkel - 5, -80, EZMode.EinerOnly10Segmente)//
					.setDropShadow(new DropShadow(1.5f * strokeWidth, 0, 1.5f * strokeWidth, Color.BLACK))//
					// .overrideColor(Color.WHITE)//
					.draw(bitmapCanvas);

			// ##############################################
		}
	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberCentered(bitmapCanvas, level, fontSizeLevel, new DropShadow(strokeWidth * 3, strokeWidth * 2, strokeWidth * 2, Color.BLACK));
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = -225 + Math.round(level * 2.7f);
		new TextOnCirclePart(center, maxRadius * 0.92f, winkel, fontSizeArc, new Paint())//
				.setColor(Settings.getChargeStatusColor())//
				.setAlign(Align.CENTER)//
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
