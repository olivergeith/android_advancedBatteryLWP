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
import de.geithonline.abattlwp.bitmapdrawer.data.RadiusData;
import de.geithonline.abattlwp.bitmapdrawer.data.SkalaLines.LevelLinesStyle;
import de.geithonline.abattlwp.bitmapdrawer.data.SkalaLines.VoltLinesStyle;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZColoring;
import de.geithonline.abattlwp.bitmapdrawer.parts.ArchPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.Skala;
import de.geithonline.abattlwp.bitmapdrawer.parts.SkalaPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnCirclePart;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnLinePart;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;

public class DarkV4 extends AdvancedBitmapDrawer {

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
		fontSizeLevel = maxRadius * 0.12f;

	}

	public DarkV4() {
	}

	@Override
	public boolean isPremiumDrawer() {
		return true;
	}

	@Override
	public boolean supportsPointerColor() {
		return true;
	}

	@Override
	public boolean supportsShowPointer() {
		return true;
	}

	@Override
	public boolean supportsLevelStyle() {
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
	public Bitmap drawBitmap(final int level, final Bitmap bitmap) {
		initPrivateMembers();
		drawAll(level);
		return bitmap;
	}

	private final float startWinkel = -225;
	private final float sweep = 270;

	private void drawAll(final int level) {
		// Ausen Ring
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.94f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(96), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(64), strokeWidth / 2))//
				.draw(bitmapCanvas);
		new RingPart(center, maxRadius * 0.94f, maxRadius * 0.85f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(64), PaintProvider.getGray(64), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(64), strokeWidth / 2))//
				.draw(bitmapCanvas);
		new RingPart(center, maxRadius * 0.85f, maxRadius * 0.80f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(96), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(64), strokeWidth / 2))//
				.draw(bitmapCanvas);
		// innere fl�che
		new RingPart(center, maxRadius * 0.80f, maxRadius * 0.0f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(64), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(32), strokeWidth / 2))//
				.draw(bitmapCanvas);

		// Level
		new LevelPart(center, maxRadius * 0.80f, maxRadius * 0.50f, level, startWinkel, sweep, EZColoring.LevelColors)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(Settings.getLevelMode())//
				.draw(bitmapCanvas);

		final SkalaPart s = Skala.getLevelScalaArch(center, maxRadius * 0.51f, maxRadius * 0.56f, startWinkel, sweep, LevelLinesStyle.ZehnerFuenferEiner)//
				.setFontAttributesEbene1(new FontAttributes(fontSizeScala))//
				.setDicke(strokeWidth * 0.75f)//
				.setupDefaultBaseLineRadius()//
				.setDickeBaseLineDefault()//
				.draw(bitmapCanvas);
		if (Settings.isShowZeiger()) {
			Skala.getZeigerPart(center, level, maxRadius * 0.80f, maxRadius * 0.50f, s.getScala())//
					.setDicke(strokeWidth * 0.7f)//
					.setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
					.draw(bitmapCanvas);
		}

		drawVoltMeter();
		drawThermoMeter();

	}

	private void drawVoltMeter() {
		if (Settings.isShowVoltmeter()) {
			final SkalaPart s = Skala.getDefaultVoltmeterPart(center, maxRadius * 0.31f, maxRadius * 0.33f, -170, 160, VoltLinesStyle.style_500_100_50)//
					.setFontAttributesEbene1(new FontAttributes(Align.CENTER, Typeface.DEFAULT, fontSizeScala * 0.85f))//
					.setupDefaultBaseLineRadius()//
					.setDicke(strokeWidth * 0.5f)//
					.draw(bitmapCanvas);
			Skala.getZeigerPart(center, Settings.getBattVoltage(), maxRadius * 0.33f, maxRadius * 0.25f, s.getScala())//
					.setSweepRadiusData(new RadiusData(maxRadius * 0.30f, maxRadius * 0.25f))//
					.setDicke(strokeWidth * 0.7f)//
					.setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
					.draw(bitmapCanvas);

			new TextOnLinePart(center, maxRadius * 0.05f, -90, fontSizeArc, new Paint())//
					.setColor(Settings.getBattStatusColor())//
					.setAlign(Align.CENTER)//
					.draw(bitmapCanvas, String.format("%.2f V", Settings.getBattVoltage()));
		}
	}

	private void drawThermoMeter() {
		if (Settings.isShowThermometer()) {
			final PointF center = new PointF();

			center.x = bmpWidth / 2;
			center.y = bmpHeight * 0.85f;
			final SkalaPart s = Skala.getDefaultThermometerPart(center, maxRadius * 0.31f, maxRadius * 0.33f, -170, 160, LevelLinesStyle.ZehnerFuenferEiner)//
					.setFontAttributesEbene1(new FontAttributes(Align.CENTER, Typeface.DEFAULT, fontSizeScala * 0.85f))//
					.setupDefaultBaseLineRadius()//
					.setDicke(strokeWidth * 0.5f)//
					.draw(bitmapCanvas);
			Skala.getZeigerPart(center, Settings.getBattTemperature(), maxRadius * 0.33f, maxRadius * 0.25f, s.getScala())//
					.setSweepRadiusData(new RadiusData(maxRadius * 0.30f, maxRadius * 0.25f))//
					.setDicke(strokeWidth * 0.7f)//
					.setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
					.draw(bitmapCanvas);

			new TextOnLinePart(center, maxRadius * 0.05f, -90, fontSizeArc, new Paint())//
					.setColor(Settings.getBattStatusColor())//
					.setAlign(Align.CENTER)//
					.draw(bitmapCanvas, Settings.getBattTemperature() + " °C");
		}
	}

	@Override
	public void drawLevelNumber(final int level) {
		final float winkel = -225 + level * 2.7f;
		new ArchPart(center, maxRadius * 0.85f, maxRadius * 0.69f, winkel - 12, 24, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(96), strokeWidth / 2))//
				.setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
				.draw(bitmapCanvas);

		new TextOnCirclePart(center, maxRadius * 0.72f, winkel, fontSizeLevel, PaintProvider.getLevelNumberPaint(level, fontSizeLevel))//
				.setAlign(Align.CENTER)//
				.draw(bitmapCanvas, "" + level);

		// drawLevelNumberCentered(bitmapCanvas, level, fontSizeLevel);
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = -90;
		new TextOnCirclePart(center, maxRadius * 0.86f, winkel, fontSizeArc, new Paint())//
				.setColor(Settings.getChargeStatusColor())//
				.setAlign(Align.CENTER)//
				.draw(bitmapCanvas, Settings.getChargingText());
	}

	@Override
	public void drawBattStatusText(final int level) {
		new TextOnCirclePart(center, maxRadius * 0.96f, 90, fontSizeArc, new Paint())//
				.setColor(Settings.getBattStatusColor())//
				.setAlign(Align.CENTER)//
				.invert(true)//
				.draw(bitmapCanvas, Settings.getBattStatusCompleteShort());
	}

}
