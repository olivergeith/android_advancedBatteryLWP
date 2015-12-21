package de.geithonline.abattlwp.bitmapdrawer;

import java.util.Locale;

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
import de.geithonline.abattlwp.bitmapdrawer.parts.ArchPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelZeigerPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.Skala;
import de.geithonline.abattlwp.bitmapdrawer.parts.SkalaPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnCirclePart;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;
import de.geithonline.abattlwp.utils.ColorHelper;
import de.geithonline.abattlwp.utils.GeometrieHelper;

public class FancyV2 extends AdvancedBitmapDrawer {

	private float strokeWidth;

	private float fontSizeLevel;
	private float fontSizeArc;
	private float fontSizeScala;
	private Outline outline;
	private float maxRadius;

	private final PointF center = new PointF();

	private void initPrivateMembers() {
		center.x = bmpWidth / 2;
		center.y = bmpHeight * 0.75f;

		maxRadius = bmpWidth * 0.5f;
		// Strokes
		strokeWidth = maxRadius * 0.02f;
		// fontsizes
		fontSizeArc = maxRadius * 0.08f;
		fontSizeScala = maxRadius * 0.08f;
		fontSizeLevel = maxRadius * 0.2f;
		outline = new Outline(PaintProvider.getGray(32), strokeWidth * 0.7f);

	}

	public FancyV2() {
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
	public boolean supportsThermometer() {
		return true;
	}

	@Override
	public boolean supportsVoltmeter() {
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

	private final float halfWinkel = 110f;
	private final float sweep = 2 * halfWinkel;
	private final float startWinkel = -90 - halfWinkel;

	private void drawAll(final int level) {
		// SkalaBackground
		new ArchPart(center, maxRadius * 0.75f, maxRadius * 0f, startWinkel - 7, sweep + 2 * 7, PaintProvider.getBackgroundPaint())//
				.setOutline(outline)//
				.draw(bitmapCanvas);
		new ArchPart(center, maxRadius * 0.48f, maxRadius * 0f, startWinkel - 7, -(360 - sweep - 2 * 7), PaintProvider.getBackgroundPaint())//
				.setOutline(outline)//
				.draw(bitmapCanvas);

		// Ausen Ring
		new ArchPart(center, maxRadius * 0.95f, maxRadius * 0.75f, startWinkel - 7, sweep + 2 * 7, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(160), GRAD_STYLE.top2bottom))//
				.setOutline(outline)//
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
			new RingPart(center, maxRadius * 0.25f, maxRadius * 0.0f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 10, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
			new RingPart(center, maxRadius * 0.25f, maxRadius * 0.0f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 5, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
		}
		// Zeiger
		new LevelZeigerPart(center, level, maxRadius * 0.80f, maxRadius * 0.26f, strokeWidth, startWinkel, sweep, EZMode.Einer)//
				.setDropShadow(new DropShadow(1.5f * strokeWidth, 0, 1.5f * strokeWidth, Color.BLACK))//
				.draw(bitmapCanvas);

		// Innen Fläche
		new RingPart(center, maxRadius * 0.25f, maxRadius * 0.00f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(160), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(outline)//
				.draw(bitmapCanvas);

		Skala.getLevelScalaArch(center, maxRadius * 0.76f, maxRadius * 0.82f, startWinkel, sweep, LevelLinesStyle.ZehnerFuenferEiner)//
				.setFontAttributesEbene1(new FontAttributes(fontSizeScala))//
				.setFontAttributesEbene2Default()//
				.setDicke(strokeWidth * 0.5f)//
				.draw(bitmapCanvas);

		drawVoltMeter();
		drawThermoMeter();

	}

	private void drawVoltMeter() {
		if (Settings.isShowVoltmeter()) {
			// SkalaBackground
			new ArchPart(center, maxRadius * 1.2f, maxRadius * 0.95f, -90, -55, PaintProvider.getBackgroundPaint())//
					.setOutline(outline)//
					.draw(bitmapCanvas);
			new ArchPart(center, maxRadius * 1.35f, maxRadius * 1.2f, -90, -55 / 2f, PaintProvider.getBackgroundPaint())//
					.setOutline(outline)//
					.draw(bitmapCanvas);

			final SkalaPart s = Skala.getDefaultVoltmeterPart(center, maxRadius * 1.07f, maxRadius * 1.10f, -142, 49, VoltLinesStyle.style_500_100_50)//
					.setFontAttributesEbene1(new FontAttributes(Align.CENTER, Typeface.DEFAULT, fontSizeScala * 0.85f))//
					.setupDefaultBaseLineRadius()//
					.setDicke(strokeWidth * 0.5f)//
					.draw(bitmapCanvas);
			Skala.getZeigerPart(center, Settings.getBattVoltage(), maxRadius * 1.11f, maxRadius * 0.96f, s.getScala())//
					.setDicke(strokeWidth)//
					.overrideColor(ColorHelper.changeBrightness(Settings.getZeigerColor(), -32))//
					.setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
					.draw(bitmapCanvas);

			// Innen Fläche
			new TextOnCirclePart(center, maxRadius * 1.25f, -94, fontSizeArc * 1.2f, new Paint())//
					.setColor(Settings.getBattStatusColor())//
					.setAlign(Align.RIGHT)//
					.setDropShadow(new DropShadow(strokeWidth * 2, Color.BLACK))//
					.draw(bitmapCanvas, String.format(Locale.US, "%.2f V", Settings.getBattVoltage()));
		}
	}

	private void drawThermoMeter() {
		if (Settings.isShowThermometer()) {
			// SkalaBackground
			new ArchPart(center, maxRadius * 1.2f, maxRadius * 0.95f, -90, 55, PaintProvider.getBackgroundPaint())//
					.setOutline(outline)//
					.draw(bitmapCanvas);
			new ArchPart(center, maxRadius * 1.35f, maxRadius * 1.2f, -90, +55 / 2f, PaintProvider.getBackgroundPaint())//
					.setOutline(outline)//
					.draw(bitmapCanvas);

			final SkalaPart s = Skala.getDefaultThermometerPart(center, maxRadius * 1.07f, maxRadius * 1.10f, -87, 49, LevelLinesStyle.ZehnerFuenfer)//
					.setFontAttributesEbene1(new FontAttributes(Align.CENTER, Typeface.DEFAULT, fontSizeScala * 0.85f))//
					.setupDefaultBaseLineRadius()//
					.setDicke(strokeWidth * 0.5f)//
					.draw(bitmapCanvas);

			Skala.getZeigerPart(center, Settings.getBattTemperature(), maxRadius * 1.11f, maxRadius * 0.96f, s.getScala())//
					.setDicke(strokeWidth)//
					.overrideColor(ColorHelper.changeBrightness(Settings.getZeigerColor(), -32))//
					.setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
					.draw(bitmapCanvas);

			// Innen Fläche
			new TextOnCirclePart(center, maxRadius * 1.25f, -86, fontSizeArc * 1.2f, new Paint())//
					.setColor(Settings.getBattStatusColor())//
					.setAlign(Align.LEFT)//
					.draw(bitmapCanvas, Settings.getBattTemperature() + " °C");
		}
	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberCenteredInRect(bitmapCanvas, level, "" + level, fontSizeLevel, GeometrieHelper.getCircle(center, maxRadius * 0.25f));
	}

	@Override
	public void drawChargeStatusText(final int level) {
		new TextOnCirclePart(center, maxRadius * 0.50f, -90, fontSizeArc, new Paint())//
				.setColor(Settings.getChargeStatusColor())//
				.setAlign(Align.CENTER)//
				.draw(bitmapCanvas, Settings.getChargingText());
	}

	@Override
	public void drawBattStatusText(final int level) {
		new TextOnCirclePart(center, maxRadius * 0.40f, -90, fontSizeArc, new Paint())//
				.setColor(Settings.getBattStatusColor())//
				.setAlign(Align.CENTER)//
				.draw(bitmapCanvas, Settings.getBattStatusCompleteShort());
	}

}
