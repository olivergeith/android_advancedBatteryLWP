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
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;

public class DarkV2 extends AdvancedBitmapDrawer {

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
		fontSizeLevel = maxRadius * 0.40f;

	}

	public DarkV2() {
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
	public Bitmap drawBitmap(final int level, final Bitmap bitmap) {
		initPrivateMembers();
		drawAll(level);
		return bitmap;
	}

	private final float startWinkel = -90;
	private final float sweep = 360;

	private void drawAll(final int level) {
		// SkalaBackground
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.0f, PaintProvider.getBackgroundPaint())//
				.setOutline(new Outline(PaintProvider.getGray(32), strokeWidth / 2))//
				.draw(bitmapCanvas);

		// Ausen Ring
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.80f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(64), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
				.draw(bitmapCanvas);
		new RingPart(center, maxRadius * 0.80f, maxRadius * 0.0f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(64), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
				.draw(bitmapCanvas);

		// Hintergrund für Level
		new RingPart(center, maxRadius * 0.80f, maxRadius * 0.60f, PaintProvider.getBackgroundPaint())//
				.setEraseBeforDraw()//
				.setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
				.draw(bitmapCanvas);

		// Level
		new LevelPart(center, maxRadius * 0.80f, maxRadius * 0.60f, level, startWinkel, sweep, EZColoring.LevelColors)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(Settings.getLevelMode())//
				.draw(bitmapCanvas);

		final SkalaPart s = Skala.getLevelScalaArch(center, maxRadius * 0.59f, maxRadius * 0.54f, startWinkel, sweep, LevelLinesStyle.ZehnerFuenferEiner)//
				.setFontAttributesEbene1(new FontAttributes(fontSizeScala))//
				.setFontRadiusEbene1(maxRadius * 0.44f)//
				.setDicke(strokeWidth * 0.75f)//
				// .setupDefaultBaseLineRadius()//
				// .setDickeBaseLineDefault()//
				.draw(bitmapCanvas);
		if (Settings.isShowZeiger()) {
			Skala.getZeigerPart(center, level, maxRadius * 0.80f, maxRadius * 0.60f, s.getScala())//
					.setDicke(strokeWidth)//
					.setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
					.draw(bitmapCanvas);

		}

		for (int i = 0; i < 4; i++) {
			float sw = -65 + i * 90;
			if (Settings.isCharging) {
				sw = sw + level * 3.6f;
			}
			final float swe = 40;
			new ArchPart(center, maxRadius * 0.94f, maxRadius * 0.85f, sw, swe, PaintProvider.getBatteryPaint(level))//
					.setAlpha(192)//
					.setEraseBeforDraw()//
					.draw(bitmapCanvas);
		}

		// drawVoltMeter();

	}

	private void drawVoltMeter() {
		if (Settings.isShowVoltmeter()) {
			final SkalaPart s = Skala.getDefaultVoltmeterPart(center, maxRadius * 0.69f, maxRadius * 0.64f, 105, 60, VoltLinesStyle.style_500_100_50)//
					.setFontAttributesEbene1(new FontAttributes(fontSizeScala * 0.7f))//
					.setFontRadiusEbene1(maxRadius * 0.56f)//
					.setDicke(strokeWidth * 0.6f)//
					.setupDefaultBaseLineRadius()//
					.setDickeBaseLineDefault()//
					.draw(bitmapCanvas);
			Skala.getZeigerPart(center, Settings.getBattVoltage(), maxRadius * 0.75f, maxRadius * 0.64f, s.getScala())//
					.overrideColor(Color.WHITE)//
					.setDicke(strokeWidth / 2)//
					.setSweepRadiusData(new RadiusData(maxRadius * 0.73f, maxRadius * 0.70f))//
					.setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
					.draw(bitmapCanvas);
		}
	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberCentered(bitmapCanvas, level, fontSizeLevel);
	}

	@Override
	public void drawChargeStatusText(final int level) {
		for (int i = 0; i < 4; i++) {
			final float winkel = -90 + i * 90 + level * 3.6f;
			new TextOnCirclePart(center, maxRadius * 0.86f, winkel, fontSizeArc, new Paint())//
					.setColor(Settings.getChargeStatusColor())//
					.setAlign(Align.CENTER)//
					.draw(bitmapCanvas, Settings.getChargingText());
		}
	}

	@Override
	public void drawBattStatusText(final int level) {
		new TextOnCirclePart(center, maxRadius * 0.40f, 90, fontSizeArc, new Paint())//
				.setColor(Settings.getBattStatusColor())//
				.setAlign(Align.CENTER)//
				.invert(true)//
				.draw(bitmapCanvas, Settings.getBattStatusCompleteShort());
	}

}
