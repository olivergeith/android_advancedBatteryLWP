package de.geithonline.abattlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PointF;
import android.graphics.Typeface;
import de.geithonline.abattlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.abattlwp.bitmapdrawer.data.FontAttributes;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient.GRAD_STYLE;
import de.geithonline.abattlwp.bitmapdrawer.data.Outline;
import de.geithonline.abattlwp.bitmapdrawer.data.RadiusData;
import de.geithonline.abattlwp.bitmapdrawer.data.SkalaLines.LevelLinesStyle;
import de.geithonline.abattlwp.bitmapdrawer.data.SkalaLines.VoltLinesStyle;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZColoring;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZMode;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZStyle;
import de.geithonline.abattlwp.bitmapdrawer.parts.AnyPathPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.ArchPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.EraserPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelZeigerPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.Skala;
import de.geithonline.abattlwp.bitmapdrawer.parts.SkalaPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnCirclePart;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnLinePart;
import de.geithonline.abattlwp.bitmapdrawer.shapes.CirclePath;
import de.geithonline.abattlwp.bitmapdrawer.shapes.ZeigerShapePath.ZEIGER_TYP;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;

public class AsymetricV2 extends AdvancedBitmapDrawer {

	private float strokeWidth;

	private float fontSizeLevel;
	private float fontSizeArc;
	private float fontSizeScala;

	private float maxRadius;

	private final PointF center = new PointF();
	private final PointF center2 = new PointF();
	private final PointF center3 = new PointF();

	private void initPrivateMembers() {
		center.x = bmpWidth / 2;
		center.y = bmpHeight / 2;

		center2.x = bmpWidth / 2;
		center2.y = bmpHeight * 0.85f;

		center3.x = bmpWidth / 2;
		center3.y = bmpHeight * 0.27f;

		maxRadius = bmpWidth / 2;
		// Strokes
		strokeWidth = maxRadius * 0.02f;
		// fontsizes
		fontSizeArc = maxRadius * 0.07f;
		fontSizeScala = maxRadius * 0.15f;
		fontSizeLevel = maxRadius * 0.4f;
		// Radiusses

	}

	public AsymetricV2() {
	}

	@Override
	public boolean supportsPointerColor() {
		return true;
	}

	// @Override
	// public boolean supportsLevelStyle() {
	// return true;
	// }

	@Override
	public boolean supportsThermometer() {
		return true;
	}

	@Override
	public boolean supportsVoltmeter() {
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
		// SkalaBackground
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.0f, PaintProvider.getBackgroundPaint())//
				.setEraseBeforDraw()//
				.setOutline(new Outline(Color.WHITE, strokeWidth / 2))//
				.draw(bitmapCanvas);
		// Skala
		final float winkel = -90 - level * 3.6f;
		Skala.getLevelScalaCircular(center2, maxRadius * 1.12f, maxRadius * 1.20f, winkel, LevelLinesStyle.ZehnerFuenferEiner)//
				.setFontAttributesEbene1(new FontAttributes(fontSizeScala))//
				.setFontAttributesEbene2Default()//
				// .setFontAttributesEbene3(new FontAttributes(fontSizeArc))//
				.setupDefaultBaseLineRadius()//
				// .setFontRadiusEbene3Default()//
				.setDickeBaseLine(strokeWidth)//
				.setDicke(strokeWidth)//
				.draw(bitmapCanvas);
		new LevelZeigerPart(center2, level, maxRadius * 1.16f, 0, strokeWidth * 10, winkel, 360, EZMode.Einer)//
				.setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
				.setZeigerType(ZEIGER_TYP.raute).draw(bitmapCanvas);

		// aussen alles weg radieren
		new EraserPart(new CirclePath(center, maxRadius * 1.99f, maxRadius * 0.98f)).draw(bitmapCanvas);

		// Ausen Ring
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.89f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(80), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(0), strokeWidth / 2))//
				.draw(bitmapCanvas);
		new AnyPathPart(center, new Paint(), getCenterPath())//
				.setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(100), GRAD_STYLE.top2bottom), maxRadius * 0.89f, 0)//
				.setOutline(new Outline(PaintProvider.getGray(0), strokeWidth / 2))//
				.draw(bitmapCanvas);
		new RingPart(center3, maxRadius * 0.39f, maxRadius * 0.3f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(80), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(0), strokeWidth / 2))//
				.draw(bitmapCanvas);
		drawExtraLevels(level);
		drawVoltMeter();
		drawThermoMeter();
	}

	private Path getCenterPath() {
		final Path p = new Path();
		final Path aussen = new CirclePath(center, maxRadius * 0.89f, 0f);
		final Path innnen = new CirclePath(center3, maxRadius * 0.3f, 0f, Direction.CCW);
		p.addPath(aussen);
		p.addPath(innnen);
		return p;
	}

	private void drawExtraLevels(final int level) {
		if (Settings.isShowExtraLevelBars()) {
			// Timer
			new LevelPart(center3, maxRadius * 0.5f, maxRadius * 0.45f, level, 120, 90, EZColoring.ColorOf100)//
					.setSegemteAbstand(2f)//
					.setStrokeWidth(strokeWidth / 3)//
					.setStyle(EZStyle.segmented_all)//
					.setMode(EZMode.Zehner)//
					.draw(bitmapCanvas);
			new LevelPart(center3, maxRadius * 0.5f, maxRadius * 0.45f, level, 60, -90, EZColoring.ColorOf100)//
					.setSegemteAbstand(2f)//
					.setStrokeWidth(strokeWidth / 3)//
					.setStyle(EZStyle.segmented_all)//
					.setMode(EZMode.EinerOnly10Segmente)//
					.draw(bitmapCanvas);
		}
	}

	private void drawVoltMeter() {
		if (Settings.isShowVoltmeter()) {
			final SkalaPart s = Skala.getDefaultVoltmeterPart(center, maxRadius * 0.60f, maxRadius * 0.66f, 100, 90, VoltLinesStyle.style_500_100_50)//
					.setFontAttributesEbene1(new FontAttributes(Align.CENTER, Typeface.DEFAULT, fontSizeScala * 0.5f))//
					.setupDefaultBaseLineRadius()//
					.setDickeBaseLine(strokeWidth)//
					.setDicke(strokeWidth)//
					.draw(bitmapCanvas);
			Skala.getZeigerPart(center, Settings.getBattVoltage(), maxRadius * 0.66f, maxRadius * 0.48f, s.getScala())//
					.setDicke(strokeWidth)//
					.setSweepRadiusData(new RadiusData(maxRadius * 0.57f, maxRadius * 0.50f))//
					// .setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
					.draw(bitmapCanvas);

			new ArchPart(center, maxRadius * 1.02f, maxRadius * 0.80f, 130, 30, new Paint())//
					.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(96), GRAD_STYLE.top2bottom))//
					.setOutline(new Outline(PaintProvider.getGray(0), strokeWidth / 2))//
					// .setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
					.draw(bitmapCanvas);
			new TextOnCirclePart(center, maxRadius * 0.93f, 145, fontSizeArc, new Paint())//
					.setColor(Settings.getBattStatusColor())//
					.setAlign(Align.CENTER)//
					.invert(true)//
					.draw(bitmapCanvas, Settings.getBattVoltage() + " Volt");
		}
	}

	private void drawThermoMeter() {
		if (Settings.isShowThermometer()) {
			final SkalaPart s = Skala.getDefaultThermometerPart(center, maxRadius * 0.60f, maxRadius * 0.66f, 80, -90, LevelLinesStyle.ZehnerFuenfer)//
					.setFontAttributesEbene1(new FontAttributes(Align.CENTER, Typeface.DEFAULT, fontSizeScala * 0.5f))//
					.setupDefaultBaseLineRadius()//
					.setDickeBaseLine(strokeWidth)//
					.setDicke(strokeWidth)//
					.draw(bitmapCanvas);
			Skala.getZeigerPart(center, Settings.getBattTemperature(), maxRadius * 0.66f, maxRadius * 0.48f, s.getScala())//
					.setDicke(strokeWidth)//
					.setSweepRadiusData(new RadiusData(maxRadius * 0.57f, maxRadius * 0.50f))//
					// .setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
					.draw(bitmapCanvas);

			new ArchPart(center, maxRadius * 1.02f, maxRadius * 0.80f, 20, 30, new Paint())//
					.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(96), GRAD_STYLE.top2bottom))//
					.setOutline(new Outline(PaintProvider.getGray(0), strokeWidth / 2))//
					// .setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
					.draw(bitmapCanvas);
			new TextOnCirclePart(center, maxRadius * 0.93f, 35, fontSizeArc, new Paint())//
					.setColor(Settings.getBattStatusColor())//
					.setAlign(Align.CENTER)//
					.invert(true)//
					.draw(bitmapCanvas, Settings.getBattTemperature() + " °C");
		}
	}

	@Override
	public void drawLevelNumber(final int level) {
		new TextOnLinePart(center, maxRadius * 0.35f, 90, fontSizeLevel, PaintProvider.getLevelNumberPaint(level, fontSizeLevel))//
				.setAlign(Align.CENTER)//
				.setDropShadow(new DropShadow(strokeWidth * 3, strokeWidth * 3, strokeWidth * 3, Color.BLACK))//
				.invert(true)//
				.draw(bitmapCanvas, "" + level);
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = 276 + Math.round(level * 3.6f);
		new TextOnCirclePart(center3, maxRadius * 0.33f, winkel, fontSizeArc, new Paint())//
				.setColor(Settings.getChargeStatusColor())//
				.setAlign(Align.LEFT)//
				.draw(bitmapCanvas, Settings.getChargingText());
	}

	@Override
	public void drawBattStatusText(final int level) {
		new TextOnCirclePart(center, maxRadius * 0.96f, 90, fontSizeArc, new Paint())//
				.setColor(Settings.getBattStatusColor())//
				.invert(true)//
				.setAlign(Align.CENTER)//
				.draw(bitmapCanvas, Settings.getBattStatusCompleteShort());
	}

}
