package de.geithonline.abattlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import android.graphics.Typeface;
import de.geithonline.abattlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.abattlwp.bitmapdrawer.data.FontAttributes;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient.GRAD_STYLE;
import de.geithonline.abattlwp.bitmapdrawer.data.Outline;
import de.geithonline.abattlwp.bitmapdrawer.data.SkalaLines.LevelLinesStyle;
import de.geithonline.abattlwp.bitmapdrawer.data.SkalaLines.VoltLinesStyle;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZColoring;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZMode;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZStyle;
import de.geithonline.abattlwp.bitmapdrawer.parts.ArchPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelZeigerPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.Skala;
import de.geithonline.abattlwp.bitmapdrawer.parts.SkalaPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnCirclePart;
import de.geithonline.abattlwp.bitmapdrawer.shapes.ZeigerShapePath.ZEIGER_TYP;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;

public class RotatingV2 extends AdvancedBitmapDrawer {

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
		fontSizeScala = maxRadius * 0.07f;
		fontSizeLevel = maxRadius * 0.15f;
	}

	public RotatingV2() {
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
	public boolean supportsGlowScala() {
		return true;
	}

	@Override
	public boolean supportsVoltmeter() {
		return true;
	}

	@Override
	public boolean supportsThermometer() {
		return true;
	}

	@Override
	public boolean isPremiumDrawer() {
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
		new RingPart(center, maxRadius * 0.95f, maxRadius * 0.0f, PaintProvider.getBackgroundPaint())//
				.draw(bitmapCanvas);

		// Ausen Ring
		new RingPart(center, maxRadius * 0.95f, maxRadius * 0.85f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(96), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
				.draw(bitmapCanvas);

		// Extra Level Bars
		drawExtraLevelBars(level);

		// Level
		new LevelPart(center, maxRadius * 0.83f, maxRadius * 0.60f, level, -225, 270, EZColoring.LevelColors)//
				.setSegemteAbstand(0.75f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(Settings.getLevelMode())//
				.draw(bitmapCanvas);

		// Skala
		Skala.getLevelScalaArch(center, maxRadius * 0.58f, maxRadius * 0.52f, -225, 270, LevelLinesStyle.ZehnerFuenferEiner)//
				.setFontAttributesEbene1(new FontAttributes(fontSizeScala))//
				.setFontRadiusEbene1(maxRadius * 0.45f)//
				.setDicke(strokeWidth * 0.75f)//
				.draw(bitmapCanvas);

		// SkalaGlow
		if (Settings.isShowGlowScala()) {
			new RingPart(center, maxRadius * 0.20f, maxRadius * 0.15f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 30, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
			new RingPart(center, maxRadius * 0.20f, maxRadius * 0.15f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 10, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
			new RingPart(center, maxRadius * 0.20f, maxRadius * 0.15f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 3, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
		}
		// Innen Phase
		new RingPart(center, maxRadius * 0.20f, maxRadius * 0.15f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(224), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.draw(bitmapCanvas);

		// Voltmeter
		drawMeter();

		if (Settings.isShowZeiger()) {
			// Zeiger
			new LevelZeigerPart(center, level, maxRadius * 0.65f, maxRadius * 0.16f, strokeWidth, -225, 270, EZMode.Einer)//
					.setDropShadow(new DropShadow(3 * strokeWidth, Color.BLACK))//
					.draw(bitmapCanvas);
		}
		// Innen Fl�che
		new RingPart(center, maxRadius * 0.15f, maxRadius * 0.00f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(192), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(32), strokeWidth))//
				.draw(bitmapCanvas);
	}

	private void drawMeter() {
		if (Settings.isShowThermometer()) {
			final PointF center2 = new PointF(center.x, center.y + maxRadius * 0.95f);
			final SkalaPart s = Skala.getDefaultThermometerPart(center2, maxRadius * 0.35f, maxRadius * 0.40f, -135, 90, LevelLinesStyle.ZehnerFuenfer)//
					.setFontAttributesEbene1(new FontAttributes(Align.CENTER, Typeface.DEFAULT, fontSizeScala * 0.85f))//
					.setupDefaultBaseLineRadius()//
					.setDicke(strokeWidth * 0.5f)//
					.draw(bitmapCanvas);

			Skala.getZeigerPart(center2, Settings.getBattTemperature(), maxRadius * 0.37f, maxRadius * 0.05f, s.getScala())//
					.setDicke(strokeWidth)//
					.setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
					.draw(bitmapCanvas);

			new ArchPart(center, maxRadius * 0.99f, maxRadius * 0.80f, 75, 30, new Paint())//
					.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(96), GRAD_STYLE.top2bottom))//
					.setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
					.setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
					.draw(bitmapCanvas);
		}
		if (Settings.isShowVoltmeter()) {
			final PointF center2 = new PointF(center.x, center.y + maxRadius * 0.95f);
			final SkalaPart s = Skala.getDefaultVoltmeterPart(center2, maxRadius * 0.50f, maxRadius * 0.55f, -135, 90, VoltLinesStyle.style_500_100_50)//
					.setFontAttributesEbene1(new FontAttributes(Align.CENTER, Typeface.DEFAULT, fontSizeScala * 0.85f))//
					.setupDefaultBaseLineRadius()//
					.setDicke(strokeWidth * 0.5f)//
					.draw(bitmapCanvas);
			Skala.getZeigerPart(center2, Settings.getBattVoltage(), maxRadius * 0.52f, maxRadius * 0.05f, s.getScala())//
					.setDicke(strokeWidth)//
					.setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
					.draw(bitmapCanvas);

			new ArchPart(center, maxRadius * 0.99f, maxRadius * 0.80f, 75, 30, new Paint())//
					.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(96), GRAD_STYLE.top2bottom))//
					.setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
					.setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
					.draw(bitmapCanvas);
			new TextOnCirclePart(center, maxRadius * 0.92f, 90, fontSizeArc, new Paint())//
					.setColor(Settings.getBattStatusColor())//
					.setAlign(Align.CENTER)//
					.invert(true)//
					.draw(bitmapCanvas, Settings.getBattVoltage() + " Volt");
		}
	}

	private void drawExtraLevelBars(final int level) {
		if (Settings.isShowExtraLevelBars()) {
			new LevelPart(center, maxRadius * 0.92f, maxRadius * 0.88f, level, 70, -45, EZColoring.ColorOf100)//
					.setSegemteAbstand(1f)//
					.setStrokeWidth(strokeWidth / 3)//
					.setStyle(EZStyle.segmented_all_alpha)//
					.setMode(EZMode.EinerOnly10Segmente)//
					.draw(bitmapCanvas);
			new LevelPart(center, maxRadius * 0.92f, maxRadius * 0.88f, level, 110, 45, EZColoring.ColorOf100)//
					.setSegemteAbstand(1f)//
					.setStrokeWidth(strokeWidth / 3)//
					.setStyle(EZStyle.segmented_all_alpha)//
					.setMode(EZMode.Zehner)//
					.draw(bitmapCanvas);
		}
	}

	@Override
	public void drawLevelNumber(final int level) {
		// Rotierendes Feld f�r Levelnummer
		new LevelZeigerPart(center, level, maxRadius * 0.98f, maxRadius * 0.70f, 25, -225, 270, EZMode.Einer)//
				.setZeigerType(ZEIGER_TYP.inward_triangle)//
				.setDropShadow(new DropShadow(strokeWidth * 2, Color.BLACK))//
				.draw(bitmapCanvas);
		new LevelZeigerPart(center, level, maxRadius * 0.98f, maxRadius * 0.70f, 25, -225, 270, EZMode.Einer)//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(96), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
				.setZeigerType(ZEIGER_TYP.inward_triangle)//
				.setDropShadow(new DropShadow(strokeWidth * 3, Settings.getGlowScalaColor()))//
				.draw(bitmapCanvas);
		// die nummer
		final float winkel = -226 + level * 2.7f;
		new TextOnCirclePart(center, maxRadius * 0.85f, winkel, fontSizeLevel, PaintProvider.getLevelNumberPaint(level, fontSizeLevel))//
				.setAlign(Align.CENTER)//
				.setDropShadow(new DropShadow(strokeWidth * 2, 0, strokeWidth / 2, Color.BLACK))//
				.draw(bitmapCanvas, "" + level);

	}

	@Override
	public void drawChargeStatusText(final int level) {
		new TextOnCirclePart(center, maxRadius * 0.75f, -90, fontSizeArc, new Paint())//
				.setColor(Settings.getChargeStatusColor())//
				.setAlign(Align.CENTER)//
				.draw(bitmapCanvas, Settings.getChargingText());
	}

	@Override
	public void drawBattStatusText(final int level) {
		new TextOnCirclePart(center, maxRadius * 0.62f, -90, fontSizeArc, new Paint())//
				.setColor(Settings.getBattStatusColor())//
				.setAlign(Align.CENTER)//
				.draw(bitmapCanvas, Settings.getBattStatusCompleteShort());
	}

}
