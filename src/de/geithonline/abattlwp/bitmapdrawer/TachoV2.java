package de.geithonline.abattlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import android.graphics.Typeface;
import de.geithonline.abattlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.abattlwp.bitmapdrawer.data.FontAttributes;
import de.geithonline.abattlwp.bitmapdrawer.data.Outline;
import de.geithonline.abattlwp.bitmapdrawer.data.RadiusData;
import de.geithonline.abattlwp.bitmapdrawer.data.SkalaLines.LevelLinesStyle;
import de.geithonline.abattlwp.bitmapdrawer.data.SkalaLines.VoltLinesStyle;
import de.geithonline.abattlwp.bitmapdrawer.enums.BitmapRatio;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZColoring;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZMode;
import de.geithonline.abattlwp.bitmapdrawer.parts.HalfArchPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelZeigerPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.Skala;
import de.geithonline.abattlwp.bitmapdrawer.parts.SkalaPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnCirclePart;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;

public class TachoV2 extends AdvancedBitmapDrawer {

	private float strokeWidth;

	private float fontSizeLevel;
	private float fontSizeArc;

	private float maxRadius;

	private final PointF center = new PointF();

	private float fontSizeScala;

	@Override
	protected BitmapRatio getBitmapRatio() {
		return BitmapRatio.RECTANGULAR;
	}

	@Override
	protected int getBitmapHightRectangular(final int width) {
		return (width * 6) / 10;
	}

	private void initPrivateMembers() {
		center.x = bmpWidth / 2;
		center.y = bmpHeight;

		maxRadius = bmpWidth / 2;
		// Strokes
		strokeWidth = maxRadius * 0.02f;
		// fontsizes
		fontSizeArc = maxRadius * 0.08f;
		fontSizeScala = maxRadius * 0.08f;
		fontSizeLevel = maxRadius * 0.2f;
	}

	public TachoV2() {
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
	public boolean supportsVoltmeter() {
		return true;
	}

	@Override
	public boolean supportsThermometer() {
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
		new HalfArchPart(center, maxRadius * 0.99f, 0f, PaintProvider.getBackgroundPaint())//
				.setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
				.setUndercut(5f)//
				.draw(bitmapCanvas);

		// Level
		new LevelPart(center, maxRadius * 0.97f, maxRadius * 0.82f, level, -180, 180, EZColoring.LevelColors)//
				.setSegemteAbstand(0.9f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(Settings.getLevelMode())//
				.draw(bitmapCanvas);

		// Ausen Ring
		// new HalfArchPart(center, maxRadius * 0.80f, maxRadius * 0.60f, new Paint())//
		// .setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(96), GRAD_STYLE.top2bottom))//
		// .setUndercut(5f)//
		// .setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
		// .draw(bitmapCanvas);

		Skala.getLevelScalaArch(center, maxRadius * 0.79f, maxRadius * 0.85f, -180, 180, LevelLinesStyle.ZehnerFuenferEiner)//
				.setFontAttributesEbene1(new FontAttributes(fontSizeScala))//
				// .setFontRadiusEbene1(maxRadius * 0.64f)//
				.dontWriteOuterNumbers()//
				.setDicke(strokeWidth * 0.5f)//
				.draw(bitmapCanvas);

		if (Settings.isShowZeiger()) {
			// Zeiger
			new LevelZeigerPart(center, level, maxRadius * 0.99f, maxRadius * 0.77f, strokeWidth, -180, 180, EZMode.Einer)//
					.setDropShadow(new DropShadow(2 * strokeWidth, Color.BLACK))//
					.draw(bitmapCanvas);
		}
		drawVoltmeter();
		drawThermometer();
	}

	private void drawVoltmeter() {
		if (Settings.isShowVoltmeter()) {

			// Ausen Ring
			// new HalfArchPart(center, maxRadius * 0.45f, maxRadius * 0.30f, new Paint())//
			// .setGradient(new Gradient(PaintProvider.getGray(96), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
			// .setUndercut(5f)//
			// .setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
			// .draw(bitmapCanvas);

			final SkalaPart s = Skala.getDefaultVoltmeterPart(center, maxRadius * 0.30f, maxRadius * 0.35f, -170, 120, VoltLinesStyle.style_500_100_50)//
					.setFontAttributesEbene1(new FontAttributes(Align.CENTER, Typeface.DEFAULT, fontSizeScala * 0.85f))//
					.setupDefaultBaseLineRadius()//
					.setDicke(strokeWidth * 0.5f)//
					.draw(bitmapCanvas);
			Skala.getDefaultVoltmeterZeigerPart(center, Settings.getBattVoltage(), maxRadius * 0.40f, maxRadius * 0.30f, s.getScala())//
					.setDicke(strokeWidth)//
					.setSweepRadiusData(new RadiusData(maxRadius * 0.30f, maxRadius * 0.33f))//
					.setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
					.draw(bitmapCanvas);

			new TextOnCirclePart(center, maxRadius * 0.32f, -2, fontSizeArc * 0.85f, new Paint())//
					.setColor(Settings.getBattStatusColor())//
					.setAlign(Align.RIGHT)//
					.draw(bitmapCanvas, String.format("%.2f V", Settings.getBattVoltage()));
		}
	}

	private void drawThermometer() {
		if (Settings.isShowThermometer()) {

			// Ausen Ring
			// new HalfArchPart(center, maxRadius * 0.60f, maxRadius * 0.45f, new Paint())//
			// .setGradient(new Gradient(PaintProvider.getGray(96), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
			// .setUndercut(5f)//
			// .setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
			// .draw(bitmapCanvas);

			final SkalaPart s = Skala.getDefaultThermometerPart(center, maxRadius * 0.55f, maxRadius * 0.6f, -175, 140, LevelLinesStyle.ZehnerEiner)//
					.setFontAttributesEbene1(new FontAttributes(Align.CENTER, Typeface.DEFAULT, fontSizeScala * 0.85f))//
					.setupDefaultBaseLineRadius()//
					.setDicke(strokeWidth * 0.5f)//
					.draw(bitmapCanvas);
			Skala.getDefaultVoltmeterZeigerPart(center, Settings.getBattTemperature(), maxRadius * 0.65f, maxRadius * 0.55f, s.getScala())//
					.setDicke(strokeWidth)//
					.setSweepRadiusData(new RadiusData(maxRadius * 0.55f, maxRadius * 0.58f))//
					.setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
					.draw(bitmapCanvas);

			new TextOnCirclePart(center, maxRadius * 0.59f, -2, fontSizeArc * 0.85f, new Paint())//
					.setColor(Settings.getBattStatusColor())//
					.setAlign(Align.RIGHT)//
					.draw(bitmapCanvas, Settings.getBattTemperature() + " °C");
		}
	}

	@Override
	public void drawLevelNumber(final int level) {
		final float winkel = -135 + level * 0.9f;
		new TextOnCirclePart(center, maxRadius * 1.01f, winkel, fontSizeLevel, PaintProvider.getLevelNumberPaint(level, fontSizeLevel))//
				.setAlign(Align.CENTER)//
				.draw(bitmapCanvas, level + "%");
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = 182 + Math.round(level * 1.8f);
		new TextOnCirclePart(center, maxRadius * 0.90f, winkel, fontSizeArc, new Paint())//
				.setColor(Settings.getChargeStatusColor())//
				.setAlign(Align.LEFT)//
				.draw(bitmapCanvas, Settings.getChargingText());
	}

	@Override
	public void drawBattStatusText() {
		final long winkel = -90;
		new TextOnCirclePart(center, maxRadius * 0.90f, winkel, fontSizeArc, PaintProvider.getTextPaint(level, fontSizeArc))//
				.setAlign(Align.CENTER)//
				.draw(bitmapCanvas, Settings.getBattStatusCompleteShort());
	}

}
