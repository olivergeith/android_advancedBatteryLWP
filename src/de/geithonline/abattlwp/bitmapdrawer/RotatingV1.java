package de.geithonline.abattlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
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
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelZeigerPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.Skala;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnCirclePart;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;

public class RotatingV1 extends AdvancedBitmapDrawer {

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
		fontSizeArc = maxRadius * 0.08f;
		fontSizeScala = maxRadius * 0.08f;
		fontSizeLevel = maxRadius * 0.25f;
	}

	public RotatingV1() {
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
	public boolean supportsExtraLevelBars() {
		return true;
	}

	@Override
	public boolean supportsLevelNumberFontSizeAdjustment() {
		return false;
	}

	@Override
	public Bitmap drawBitmap(final int level, final Bitmap bitmap) {
		initPrivateMembers();
		drawAll(level);
		return bitmap;
	}

	private void drawAll(final int level) {

		// SkalaBackground
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.0f, PaintProvider.getBackgroundPaint())//
				.draw(bitmapCanvas);

		// Ausen Ring
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.90f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(96), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
				.draw(bitmapCanvas);

		// Mittlerer ring
		new RingPart(center, maxRadius * 0.65f, maxRadius * 0.50f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(96), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(192), strokeWidth / 2))//
				.draw(bitmapCanvas);

		// Skala
		Skala.getLevelScalaCircular(center, maxRadius * 0.51f, maxRadius * 0.55f, -90, LevelLinesStyle.ZehnerFuenferEiner)//
				.setFontAttributesEbene1(new FontAttributes(fontSizeScala))//
				// .setFontAttributesEbene2Default()//
				.setDicke(strokeWidth * 0.75f)//
				.draw(bitmapCanvas);

		// Level
		new LevelPart(center, maxRadius * 0.88f, maxRadius * 0.67f, level, -90, 360, EZColoring.LevelColors)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(Settings.getLevelMode())//
				.draw(bitmapCanvas);

		// Innen Phase
		new RingPart(center, maxRadius * 0.20f, maxRadius * 0.15f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(224), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.draw(bitmapCanvas);

		if (Settings.isShowExtraLevelBars()) {
			// Level
			new LevelPart(center, maxRadius * 0.48f, maxRadius * 0.35f, level, 85, -170, EZColoring.ColorOf100)//
					.setSegemteAbstand(1f)//
					.setStrokeWidth(strokeWidth / 3)//
					.setStyle(EZStyle.segmented_all_alpha)//
					.setMode(EZMode.EinerOnly10Segmente)//
					.draw(bitmapCanvas);
			new LevelZeigerPart(center, level, maxRadius * 0.48f, maxRadius * 0.16f, strokeWidth, 85, -170, EZMode.EinerOnly10Segmente)//
					.setDropShadow(new DropShadow(3 * strokeWidth, Color.BLACK))//
					.overrideColor(Color.WHITE)//
					.draw(bitmapCanvas);
			new LevelPart(center, maxRadius * 0.48f, maxRadius * 0.35f, level, 95, 170, EZColoring.ColorOf100)//
					.setSegemteAbstand(1f)//
					.setStrokeWidth(strokeWidth / 3)//
					.setStyle(EZStyle.segmented_all_alpha)//
					.setMode(EZMode.Zehner)//
					.draw(bitmapCanvas);
			new LevelZeigerPart(center, level, maxRadius * 0.48f, maxRadius * 0.16f, strokeWidth, 95, 170, EZMode.Zehner)//
					.setDropShadow(new DropShadow(3 * strokeWidth, Color.BLACK))//
					.overrideColor(Color.WHITE)//
					.draw(bitmapCanvas);

		}
		if (Settings.isShowZeiger()) {
			// Zeiger
			new LevelZeigerPart(center, level, maxRadius * 0.65f, maxRadius * 0.16f, strokeWidth, -90, 360, EZMode.Einer)//
					.setDropShadow(new DropShadow(3 * strokeWidth, Color.BLACK))//
					.draw(bitmapCanvas);
		}
		// Innen Fläche
		new RingPart(center, maxRadius * 0.15f, maxRadius * 0.00f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(192), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(32), strokeWidth))//
				.draw(bitmapCanvas);

	}

	@Override
	public void drawLevelNumber(final int level) {
		final float winkel = -90 + level * 3.6f;
		new TextOnCirclePart(center, maxRadius * 0.68f, winkel, fontSizeLevel, PaintProvider.getLevelNumberPaint(level, fontSizeLevel))//
				.setAlign(Align.CENTER)//
				.setDropShadow(new DropShadow(strokeWidth * 2, 0, strokeWidth / 2, Color.BLACK))//
				.draw(bitmapCanvas, "" + level);

	}

	@Override
	public void drawChargeStatusText(final int level) {
		final float winkel = 90 + level * 3.6f;
		new TextOnCirclePart(center, maxRadius * 0.70f, winkel, fontSizeArc, new Paint())//
				.setColor(Settings.getChargeStatusColor())//
				.setAlign(Align.CENTER)//
				.draw(bitmapCanvas, Settings.getChargingText());
	}

	@Override
	public void drawBattStatusText() {
		final float winkel = 90 + level * 3.6f;
		new TextOnCirclePart(center, maxRadius * 0.80f, winkel, fontSizeArc, new Paint())//
				.setColor(Settings.getBattStatusColor())//
				.setAlign(Align.CENTER)//
				.draw(bitmapCanvas, Settings.getBattStatusCompleteShort());
	}

}
