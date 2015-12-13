package de.geithonline.abattlwp.bitmapdrawer;

import java.util.Locale;

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
import de.geithonline.abattlwp.bitmapdrawer.data.SkalaLines.VoltLinesStyle;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZColoring;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart.RingType;
import de.geithonline.abattlwp.bitmapdrawer.parts.Skala;
import de.geithonline.abattlwp.bitmapdrawer.parts.SkalaPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnCirclePart;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnLinePart;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;
import de.geithonline.abattlwp.utils.ColorHelper;
import de.geithonline.abattlwp.utils.GeometrieHelper;

public class SquareV1 extends AdvancedBitmapDrawer {

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
		fontSizeScala = maxRadius * 0.08f;
		fontSizeLevel = maxRadius * 0.20f;
		// Radiusses

		radiusChangeText = maxRadius * 0.70f;// - fontSizeArc;
		radiusBattStatus = maxRadius * 0.42f;

	}

	public SquareV1() {
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
	public Bitmap drawBitmap(final int level, final Bitmap bitmap) {
		initPrivateMembers();
		drawAll(level);
		return bitmap;
	}

	private final float startWinkel = -90;
	private final float sweep = 270;

	private void drawAll(final int level) {
		// SkalaBackground
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.0f, PaintProvider.getBackgroundPaint(), RingType.SquareRoundet)//
				.draw(bitmapCanvas);

		// Ausen Ring
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.90f, new Paint(), RingType.SquareRoundet)//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(96), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(192), strokeWidth / 2))//
				.draw(bitmapCanvas);

		// Level
		new LevelPart(center, maxRadius * 0.79f, maxRadius * 0.70f, level, startWinkel, sweep, EZColoring.LevelColors)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(Settings.getLevelMode())//
				.draw(bitmapCanvas);

		// Innen Phase (with white dropshadow)
		if (Settings.isShowGlowScala()) {
			new RingPart(center, maxRadius * 0.25f, maxRadius * 0.20f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 30, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
			new RingPart(center, maxRadius * 0.25f, maxRadius * 0.20f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 10, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
			new RingPart(center, maxRadius * 0.25f, maxRadius * 0.20f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 3, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
		}
		new RingPart(center, maxRadius * 0.25f, maxRadius * 0.20f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(160), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(32), strokeWidth / 2))//
				.draw(bitmapCanvas);

		// Innen Fläche
		new RingPart(center, maxRadius * 0.20f, maxRadius * 0.00f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(128), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(32), strokeWidth))//
				.draw(bitmapCanvas);

		final SkalaPart s = Skala.getLevelScalaArch(center, maxRadius * 0.69f, maxRadius * 0.64f, startWinkel, sweep, LevelLinesStyle.ZehnerFuenferEiner)//
				.setFontAttributesEbene1(new FontAttributes(fontSizeScala))//
				.setFontRadiusEbene1(maxRadius * 0.54f)//
				.setDicke(strokeWidth * 0.75f)//
				.draw(bitmapCanvas);
		Skala.getZeigerPart(center, level, maxRadius * 0.80f, maxRadius * 0.21f, s.getScala())//
				.setDicke(strokeWidth)//
				.setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
				.draw(bitmapCanvas);

		drawMeter();

	}

	private void drawMeter() {
		if (Settings.isShowVoltmeter()) {
			final SkalaPart s = Skala.getDefaultVoltmeterPart(center, maxRadius * 0.50f, maxRadius * 0.55f, -135, 90, VoltLinesStyle.style_500_100_50)//
					.setFontAttributesEbene1(new FontAttributes(Align.CENTER, Typeface.DEFAULT, fontSizeScala * 0.85f))//
					// .setFontAttributesEbene2Default()//
					.setupDefaultBaseLineRadius()//
					.setDicke(strokeWidth * 0.5f)//
					.draw(bitmapCanvas);
			Skala.getZeigerPart(center, Settings.getBattVoltage(), maxRadius * 0.52f, maxRadius * 0.31f, s.getScala())//
					.setDicke(strokeWidth)//
					.overrideColor(ColorHelper.changeBrightness(Settings.getZeigerColor(), -32))//
					.setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
					.draw(bitmapCanvas);

			new TextOnLinePart(center, maxRadius * 0.17f, -90, fontSizeArc, new Paint())//
					.setColor(Settings.getBattStatusColor())//
					.setAlign(Align.CENTER)//
					.setDropShadow(new DropShadow(strokeWidth * 2, Color.BLACK))//
					.draw(bitmapCanvas, String.format(Locale.US, "%.2f V", Settings.getBattVoltage()));
		}
		if (Settings.isShowThermometer()) {
			final SkalaPart s = Skala.getDefaultThermometerPart(center, maxRadius * 0.50f, maxRadius * 0.55f, 135, -90, LevelLinesStyle.ZehnerFuenfer)//
					.setFontAttributesEbene1(new FontAttributes(Align.CENTER, Typeface.DEFAULT, fontSizeScala * 0.85f))//
					.setFontRadiusEbene1(maxRadius * 0.62f)//
					.invertText(true)//
					.setupDefaultBaseLineRadius()//
					.setDicke(strokeWidth * 0.5f)//
					.draw(bitmapCanvas);

			Skala.getZeigerPart(center, Settings.getBattTemperature(), maxRadius * 0.52f, maxRadius * 0.31f, s.getScala())//
					.setDicke(strokeWidth)//
					.overrideColor(ColorHelper.changeBrightness(Settings.getZeigerColor(), -32))//
					.setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
					.draw(bitmapCanvas);

			new TextOnLinePart(center, maxRadius * 0.22f, 90, fontSizeArc, new Paint())//
					.setColor(Settings.getBattStatusColor())//
					.setDropShadow(new DropShadow(strokeWidth * 2, Color.BLACK))//
					.setAlign(Align.CENTER)//
					.invert(true)//
					.draw(bitmapCanvas, Settings.getBattTemperature() + " °C");
		}

	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberCentered(bitmapCanvas, level, fontSizeLevel);
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = -88 + Math.round(level * 2.0f);
		new TextOnCirclePart(center, maxRadius * 0.82f, winkel, fontSizeArc, new Paint())//
				.setColor(Settings.getChargeStatusColor())//
				.setAlign(Align.LEFT)//
				.draw(bitmapCanvas, Settings.getChargingText());
	}

	@Override
	public void drawBattStatusText() {
		final Path mArc = new Path();
		final RectF oval = GeometrieHelper.getCircle(center, radiusBattStatus);
		mArc.addArc(oval, 180, 180);
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = PaintProvider.getTextBattStatusPaint(fontSizeArc, Align.CENTER, true);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

}
