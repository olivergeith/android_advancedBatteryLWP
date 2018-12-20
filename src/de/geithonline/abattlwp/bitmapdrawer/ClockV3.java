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
import de.geithonline.abattlwp.bitmapdrawer.enums.EZMode;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelZeigerPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.Skala;
import de.geithonline.abattlwp.bitmapdrawer.parts.SkalaPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnCirclePart;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnLinePart;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;
import de.geithonline.abattlwp.utils.ColorHelper;
import de.geithonline.abattlwp.utils.GeometrieHelper;

public class ClockV3 extends AdvancedBitmapDrawer {

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
		fontSizeLevel = maxRadius * 0.26f;
		// Radiusses

		radiusChangeText = maxRadius * 0.70f;// - fontSizeArc;
		radiusBattStatus = maxRadius * 0.42f;

	}

	public ClockV3() {
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

	private void drawAll(final int level) {

		// Ausen Ring
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.80f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(96), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(64), strokeWidth))//
				.draw(bitmapCanvas);
		// SkalaBackground
		new RingPart(center, maxRadius * 0.79f, maxRadius * 0.0f, PaintProvider.getBackgroundPaint())//
				.draw(bitmapCanvas);

		// Level
		new LevelPart(center, maxRadius * 0.79f, maxRadius * 0.70f, level, -90, 360, EZColoring.LevelColors)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(Settings.getLevelMode())//
				.draw(bitmapCanvas);

		// Innen Phase (with white dropshadow)
		if (Settings.isShowGlowScala()) {
			new RingPart(center, maxRadius * 0.35f, maxRadius * 0.30f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 30, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
			new RingPart(center, maxRadius * 0.35f, maxRadius * 0.30f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 10, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
			new RingPart(center, maxRadius * 0.35f, maxRadius * 0.30f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 3, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
		}
		new RingPart(center, maxRadius * 0.35f, maxRadius * 0.30f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(160), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(32), strokeWidth / 2))//
				.draw(bitmapCanvas);

		// Zeiger
		new LevelZeigerPart(center, level, maxRadius * 0.85f, maxRadius * 0.31f, strokeWidth, -90, 360, EZMode.Einer)//
				.setDropShadow(new DropShadow(3 * strokeWidth, Color.BLACK))//
				.draw(bitmapCanvas);

		// Innen Fl�che
		new RingPart(center, maxRadius * 0.30f, maxRadius * 0.00f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(128), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(32), strokeWidth))//
				.draw(bitmapCanvas);

		Skala.getLevelScalaCircular(center, maxRadius * 0.82f, maxRadius * 0.88f, -90, LevelLinesStyle.ZehnerFuenferEiner)//
				.setFontAttributesEbene1(new FontAttributes(fontSizeScala))//
				.setFontAttributesEbene2Default()//
				.setDicke(strokeWidth * 0.75f)//
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
		final long winkel = 276 + Math.round(level * 3.6f);
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
